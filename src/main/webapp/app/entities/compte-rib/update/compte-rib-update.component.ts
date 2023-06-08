import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CompteRIBFormService, CompteRIBFormGroup } from './compte-rib-form.service';
import { ICompteRIB } from '../compte-rib.model';
import { CompteRIBService } from '../service/compte-rib.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

@Component({
  selector: 'jhi-compte-rib-update',
  templateUrl: './compte-rib-update.component.html',
})
export class CompteRIBUpdateComponent implements OnInit {
  isSaving = false;
  compteRIB: ICompteRIB | null = null;

  personnesSharedCollection: IPersonne[] = [];

  editForm: CompteRIBFormGroup = this.compteRIBFormService.createCompteRIBFormGroup();

  constructor(
    protected compteRIBService: CompteRIBService,
    protected compteRIBFormService: CompteRIBFormService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePersonne = (o1: IPersonne | null, o2: IPersonne | null): boolean => this.personneService.comparePersonne(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compteRIB }) => {
      this.compteRIB = compteRIB;
      if (compteRIB) {
        this.updateForm(compteRIB);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compteRIB = this.compteRIBFormService.getCompteRIB(this.editForm);
    if (compteRIB.id !== null) {
      this.subscribeToSaveResponse(this.compteRIBService.update(compteRIB));
    } else {
      this.subscribeToSaveResponse(this.compteRIBService.create(compteRIB));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompteRIB>>): void {
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

  protected updateForm(compteRIB: ICompteRIB): void {
    this.compteRIB = compteRIB;
    this.compteRIBFormService.resetForm(this.editForm, compteRIB);

    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing<IPersonne>(
      this.personnesSharedCollection,
      compteRIB.adherent
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personneService
      .query()
      .pipe(map((res: HttpResponse<IPersonne[]>) => res.body ?? []))
      .pipe(
        map((personnes: IPersonne[]) =>
          this.personneService.addPersonneToCollectionIfMissing<IPersonne>(personnes, this.compteRIB?.adherent)
        )
      )
      .subscribe((personnes: IPersonne[]) => (this.personnesSharedCollection = personnes));
  }
}
