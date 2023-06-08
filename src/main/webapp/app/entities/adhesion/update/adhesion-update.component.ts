import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AdhesionFormService, AdhesionFormGroup } from './adhesion-form.service';
import { IAdhesion } from '../adhesion.model';
import { AdhesionService } from '../service/adhesion.service';
import { StatutAdhesion } from 'app/entities/enumerations/statut-adhesion.model';

@Component({
  selector: 'jhi-adhesion-update',
  templateUrl: './adhesion-update.component.html',
})
export class AdhesionUpdateComponent implements OnInit {
  isSaving = false;
  adhesion: IAdhesion | null = null;
  statutAdhesionValues = Object.keys(StatutAdhesion);

  editForm: AdhesionFormGroup = this.adhesionFormService.createAdhesionFormGroup();

  constructor(
    protected adhesionService: AdhesionService,
    protected adhesionFormService: AdhesionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adhesion }) => {
      this.adhesion = adhesion;
      if (adhesion) {
        this.updateForm(adhesion);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const adhesion = this.adhesionFormService.getAdhesion(this.editForm);
    if (adhesion.id !== null) {
      this.subscribeToSaveResponse(this.adhesionService.update(adhesion));
    } else {
      this.subscribeToSaveResponse(this.adhesionService.create(adhesion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdhesion>>): void {
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

  protected updateForm(adhesion: IAdhesion): void {
    this.adhesion = adhesion;
    this.adhesionFormService.resetForm(this.editForm, adhesion);
  }
}
