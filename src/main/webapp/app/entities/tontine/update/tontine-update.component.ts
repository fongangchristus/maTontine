import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TontineFormService, TontineFormGroup } from './tontine-form.service';
import { ITontine } from '../tontine.model';
import { TontineService } from '../service/tontine.service';
import { TypePenalite } from 'app/entities/enumerations/type-penalite.model';
import { StatutTontine } from 'app/entities/enumerations/statut-tontine.model';

@Component({
  selector: 'jhi-tontine-update',
  templateUrl: './tontine-update.component.html',
})
export class TontineUpdateComponent implements OnInit {
  isSaving = false;
  tontine: ITontine | null = null;
  typePenaliteValues = Object.keys(TypePenalite);
  statutTontineValues = Object.keys(StatutTontine);

  editForm: TontineFormGroup = this.tontineFormService.createTontineFormGroup();

  constructor(
    protected tontineService: TontineService,
    protected tontineFormService: TontineFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tontine }) => {
      this.tontine = tontine;
      if (tontine) {
        this.updateForm(tontine);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tontine = this.tontineFormService.getTontine(this.editForm);
    if (tontine.id !== null) {
      this.subscribeToSaveResponse(this.tontineService.update(tontine));
    } else {
      this.subscribeToSaveResponse(this.tontineService.create(tontine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITontine>>): void {
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

  protected updateForm(tontine: ITontine): void {
    this.tontine = tontine;
    this.tontineFormService.resetForm(this.editForm, tontine);
  }
}
