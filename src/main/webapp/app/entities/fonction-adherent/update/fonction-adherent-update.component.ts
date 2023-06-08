import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FonctionAdherentFormService, FonctionAdherentFormGroup } from './fonction-adherent-form.service';
import { IFonctionAdherent } from '../fonction-adherent.model';
import { FonctionAdherentService } from '../service/fonction-adherent.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { IFonction } from 'app/entities/fonction/fonction.model';
import { FonctionService } from 'app/entities/fonction/service/fonction.service';

@Component({
  selector: 'jhi-fonction-adherent-update',
  templateUrl: './fonction-adherent-update.component.html',
})
export class FonctionAdherentUpdateComponent implements OnInit {
  isSaving = false;
  fonctionAdherent: IFonctionAdherent | null = null;

  personnesSharedCollection: IPersonne[] = [];
  fonctionsSharedCollection: IFonction[] = [];

  editForm: FonctionAdherentFormGroup = this.fonctionAdherentFormService.createFonctionAdherentFormGroup();

  constructor(
    protected fonctionAdherentService: FonctionAdherentService,
    protected fonctionAdherentFormService: FonctionAdherentFormService,
    protected personneService: PersonneService,
    protected fonctionService: FonctionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePersonne = (o1: IPersonne | null, o2: IPersonne | null): boolean => this.personneService.comparePersonne(o1, o2);

  compareFonction = (o1: IFonction | null, o2: IFonction | null): boolean => this.fonctionService.compareFonction(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fonctionAdherent }) => {
      this.fonctionAdherent = fonctionAdherent;
      if (fonctionAdherent) {
        this.updateForm(fonctionAdherent);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fonctionAdherent = this.fonctionAdherentFormService.getFonctionAdherent(this.editForm);
    if (fonctionAdherent.id !== null) {
      this.subscribeToSaveResponse(this.fonctionAdherentService.update(fonctionAdherent));
    } else {
      this.subscribeToSaveResponse(this.fonctionAdherentService.create(fonctionAdherent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFonctionAdherent>>): void {
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

  protected updateForm(fonctionAdherent: IFonctionAdherent): void {
    this.fonctionAdherent = fonctionAdherent;
    this.fonctionAdherentFormService.resetForm(this.editForm, fonctionAdherent);

    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing<IPersonne>(
      this.personnesSharedCollection,
      fonctionAdherent.adherent
    );
    this.fonctionsSharedCollection = this.fonctionService.addFonctionToCollectionIfMissing<IFonction>(
      this.fonctionsSharedCollection,
      fonctionAdherent.fonction
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personneService
      .query()
      .pipe(map((res: HttpResponse<IPersonne[]>) => res.body ?? []))
      .pipe(
        map((personnes: IPersonne[]) =>
          this.personneService.addPersonneToCollectionIfMissing<IPersonne>(personnes, this.fonctionAdherent?.adherent)
        )
      )
      .subscribe((personnes: IPersonne[]) => (this.personnesSharedCollection = personnes));

    this.fonctionService
      .query()
      .pipe(map((res: HttpResponse<IFonction[]>) => res.body ?? []))
      .pipe(
        map((fonctions: IFonction[]) =>
          this.fonctionService.addFonctionToCollectionIfMissing<IFonction>(fonctions, this.fonctionAdherent?.fonction)
        )
      )
      .subscribe((fonctions: IFonction[]) => (this.fonctionsSharedCollection = fonctions));
  }
}
