import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { MonnaieFormService, MonnaieFormGroup } from './monnaie-form.service';
import { IMonnaie } from '../monnaie.model';
import { MonnaieService } from '../service/monnaie.service';

@Component({
  selector: 'jhi-monnaie-update',
  templateUrl: './monnaie-update.component.html',
})
export class MonnaieUpdateComponent implements OnInit {
  isSaving = false;
  monnaie: IMonnaie | null = null;

  editForm: MonnaieFormGroup = this.monnaieFormService.createMonnaieFormGroup();

  constructor(
    protected monnaieService: MonnaieService,
    protected monnaieFormService: MonnaieFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ monnaie }) => {
      this.monnaie = monnaie;
      if (monnaie) {
        this.updateForm(monnaie);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const monnaie = this.monnaieFormService.getMonnaie(this.editForm);
    if (monnaie.id !== null) {
      this.subscribeToSaveResponse(this.monnaieService.update(monnaie));
    } else {
      this.subscribeToSaveResponse(this.monnaieService.create(monnaie));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMonnaie>>): void {
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

  protected updateForm(monnaie: IMonnaie): void {
    this.monnaie = monnaie;
    this.monnaieFormService.resetForm(this.editForm, monnaie);
  }
}
