import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { BanqueFormService, BanqueFormGroup } from './banque-form.service';
import { IBanque } from '../banque.model';
import { BanqueService } from '../service/banque.service';

@Component({
  selector: 'jhi-banque-update',
  templateUrl: './banque-update.component.html',
})
export class BanqueUpdateComponent implements OnInit {
  isSaving = false;
  banque: IBanque | null = null;

  editForm: BanqueFormGroup = this.banqueFormService.createBanqueFormGroup();

  constructor(
    protected banqueService: BanqueService,
    protected banqueFormService: BanqueFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ banque }) => {
      this.banque = banque;
      if (banque) {
        this.updateForm(banque);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const banque = this.banqueFormService.getBanque(this.editForm);
    if (banque.id !== null) {
      this.subscribeToSaveResponse(this.banqueService.update(banque));
    } else {
      this.subscribeToSaveResponse(this.banqueService.create(banque));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBanque>>): void {
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

  protected updateForm(banque: IBanque): void {
    this.banque = banque;
    this.banqueFormService.resetForm(this.editForm, banque);
  }
}
