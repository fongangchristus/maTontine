import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PaiementSanctionFormService, PaiementSanctionFormGroup } from './paiement-sanction-form.service';
import { IPaiementSanction } from '../paiement-sanction.model';
import { PaiementSanctionService } from '../service/paiement-sanction.service';
import { ISanction } from 'app/entities/sanction/sanction.model';
import { SanctionService } from 'app/entities/sanction/service/sanction.service';

@Component({
  selector: 'jhi-paiement-sanction-update',
  templateUrl: './paiement-sanction-update.component.html',
})
export class PaiementSanctionUpdateComponent implements OnInit {
  isSaving = false;
  paiementSanction: IPaiementSanction | null = null;

  sanctionsSharedCollection: ISanction[] = [];

  editForm: PaiementSanctionFormGroup = this.paiementSanctionFormService.createPaiementSanctionFormGroup();

  constructor(
    protected paiementSanctionService: PaiementSanctionService,
    protected paiementSanctionFormService: PaiementSanctionFormService,
    protected sanctionService: SanctionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSanction = (o1: ISanction | null, o2: ISanction | null): boolean => this.sanctionService.compareSanction(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paiementSanction }) => {
      this.paiementSanction = paiementSanction;
      if (paiementSanction) {
        this.updateForm(paiementSanction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paiementSanction = this.paiementSanctionFormService.getPaiementSanction(this.editForm);
    if (paiementSanction.id !== null) {
      this.subscribeToSaveResponse(this.paiementSanctionService.update(paiementSanction));
    } else {
      this.subscribeToSaveResponse(this.paiementSanctionService.create(paiementSanction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaiementSanction>>): void {
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

  protected updateForm(paiementSanction: IPaiementSanction): void {
    this.paiementSanction = paiementSanction;
    this.paiementSanctionFormService.resetForm(this.editForm, paiementSanction);

    this.sanctionsSharedCollection = this.sanctionService.addSanctionToCollectionIfMissing<ISanction>(
      this.sanctionsSharedCollection,
      paiementSanction.sanction
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sanctionService
      .query()
      .pipe(map((res: HttpResponse<ISanction[]>) => res.body ?? []))
      .pipe(
        map((sanctions: ISanction[]) =>
          this.sanctionService.addSanctionToCollectionIfMissing<ISanction>(sanctions, this.paiementSanction?.sanction)
        )
      )
      .subscribe((sanctions: ISanction[]) => (this.sanctionsSharedCollection = sanctions));
  }
}
