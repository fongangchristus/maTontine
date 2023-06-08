import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PaiementBanqueFormService, PaiementBanqueFormGroup } from './paiement-banque-form.service';
import { IPaiementBanque } from '../paiement-banque.model';
import { PaiementBanqueService } from '../service/paiement-banque.service';

@Component({
  selector: 'jhi-paiement-banque-update',
  templateUrl: './paiement-banque-update.component.html',
})
export class PaiementBanqueUpdateComponent implements OnInit {
  isSaving = false;
  paiementBanque: IPaiementBanque | null = null;

  editForm: PaiementBanqueFormGroup = this.paiementBanqueFormService.createPaiementBanqueFormGroup();

  constructor(
    protected paiementBanqueService: PaiementBanqueService,
    protected paiementBanqueFormService: PaiementBanqueFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paiementBanque }) => {
      this.paiementBanque = paiementBanque;
      if (paiementBanque) {
        this.updateForm(paiementBanque);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paiementBanque = this.paiementBanqueFormService.getPaiementBanque(this.editForm);
    if (paiementBanque.id !== null) {
      this.subscribeToSaveResponse(this.paiementBanqueService.update(paiementBanque));
    } else {
      this.subscribeToSaveResponse(this.paiementBanqueService.create(paiementBanque));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaiementBanque>>): void {
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

  protected updateForm(paiementBanque: IPaiementBanque): void {
    this.paiementBanque = paiementBanque;
    this.paiementBanqueFormService.resetForm(this.editForm, paiementBanque);
  }
}
