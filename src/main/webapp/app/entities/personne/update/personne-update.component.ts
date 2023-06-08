import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PersonneFormService, PersonneFormGroup } from './personne-form.service';
import { IPersonne } from '../personne.model';
import { PersonneService } from '../service/personne.service';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { TypePersonne } from 'app/entities/enumerations/type-personne.model';

@Component({
  selector: 'jhi-personne-update',
  templateUrl: './personne-update.component.html',
})
export class PersonneUpdateComponent implements OnInit {
  isSaving = false;
  personne: IPersonne | null = null;
  sexeValues = Object.keys(Sexe);
  typePersonneValues = Object.keys(TypePersonne);

  adressesSharedCollection: IAdresse[] = [];

  editForm: PersonneFormGroup = this.personneFormService.createPersonneFormGroup();

  constructor(
    protected personneService: PersonneService,
    protected personneFormService: PersonneFormService,
    protected adresseService: AdresseService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAdresse = (o1: IAdresse | null, o2: IAdresse | null): boolean => this.adresseService.compareAdresse(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personne }) => {
      this.personne = personne;
      if (personne) {
        this.updateForm(personne);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personne = this.personneFormService.getPersonne(this.editForm);
    if (personne.id !== null) {
      this.subscribeToSaveResponse(this.personneService.update(personne));
    } else {
      this.subscribeToSaveResponse(this.personneService.create(personne));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonne>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(personne: IPersonne): void {
    this.personne = personne;
    this.personneFormService.resetForm(this.editForm, personne);

    this.adressesSharedCollection = this.adresseService.addAdresseToCollectionIfMissing<IAdresse>(
      this.adressesSharedCollection,
      personne.adresse
    );
  }

  protected loadRelationshipsOptions(): void {
    this.adresseService
      .query()
      .pipe(map((res: HttpResponse<IAdresse[]>) => res.body ?? []))
      .pipe(map((adresses: IAdresse[]) => this.adresseService.addAdresseToCollectionIfMissing<IAdresse>(adresses, this.personne?.adresse)))
      .subscribe((adresses: IAdresse[]) => (this.adressesSharedCollection = adresses));
  }
}
