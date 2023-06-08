import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PaiementAdhesionFormService, PaiementAdhesionFormGroup } from './paiement-adhesion-form.service';
import { IPaiementAdhesion } from '../paiement-adhesion.model';
import { PaiementAdhesionService } from '../service/paiement-adhesion.service';
import { IAdhesion } from 'app/entities/adhesion/adhesion.model';
import { AdhesionService } from 'app/entities/adhesion/service/adhesion.service';

@Component({
  selector: 'jhi-paiement-adhesion-update',
  templateUrl: './paiement-adhesion-update.component.html',
})
export class PaiementAdhesionUpdateComponent implements OnInit {
  isSaving = false;
  paiementAdhesion: IPaiementAdhesion | null = null;

  adhesionsSharedCollection: IAdhesion[] = [];

  editForm: PaiementAdhesionFormGroup = this.paiementAdhesionFormService.createPaiementAdhesionFormGroup();

  constructor(
    protected paiementAdhesionService: PaiementAdhesionService,
    protected paiementAdhesionFormService: PaiementAdhesionFormService,
    protected adhesionService: AdhesionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAdhesion = (o1: IAdhesion | null, o2: IAdhesion | null): boolean => this.adhesionService.compareAdhesion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paiementAdhesion }) => {
      this.paiementAdhesion = paiementAdhesion;
      if (paiementAdhesion) {
        this.updateForm(paiementAdhesion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paiementAdhesion = this.paiementAdhesionFormService.getPaiementAdhesion(this.editForm);
    if (paiementAdhesion.id !== null) {
      this.subscribeToSaveResponse(this.paiementAdhesionService.update(paiementAdhesion));
    } else {
      this.subscribeToSaveResponse(this.paiementAdhesionService.create(paiementAdhesion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaiementAdhesion>>): void {
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

  protected updateForm(paiementAdhesion: IPaiementAdhesion): void {
    this.paiementAdhesion = paiementAdhesion;
    this.paiementAdhesionFormService.resetForm(this.editForm, paiementAdhesion);

    this.adhesionsSharedCollection = this.adhesionService.addAdhesionToCollectionIfMissing<IAdhesion>(
      this.adhesionsSharedCollection,
      paiementAdhesion.adhesion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.adhesionService
      .query()
      .pipe(map((res: HttpResponse<IAdhesion[]>) => res.body ?? []))
      .pipe(
        map((adhesions: IAdhesion[]) =>
          this.adhesionService.addAdhesionToCollectionIfMissing<IAdhesion>(adhesions, this.paiementAdhesion?.adhesion)
        )
      )
      .subscribe((adhesions: IAdhesion[]) => (this.adhesionsSharedCollection = adhesions));
  }
}
