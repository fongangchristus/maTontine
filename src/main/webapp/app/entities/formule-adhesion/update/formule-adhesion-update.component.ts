import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FormuleAdhesionFormService, FormuleAdhesionFormGroup } from './formule-adhesion-form.service';
import { IFormuleAdhesion } from '../formule-adhesion.model';
import { FormuleAdhesionService } from '../service/formule-adhesion.service';
import { IAdhesion } from 'app/entities/adhesion/adhesion.model';
import { AdhesionService } from 'app/entities/adhesion/service/adhesion.service';

@Component({
  selector: 'jhi-formule-adhesion-update',
  templateUrl: './formule-adhesion-update.component.html',
})
export class FormuleAdhesionUpdateComponent implements OnInit {
  isSaving = false;
  formuleAdhesion: IFormuleAdhesion | null = null;

  adhesionsSharedCollection: IAdhesion[] = [];

  editForm: FormuleAdhesionFormGroup = this.formuleAdhesionFormService.createFormuleAdhesionFormGroup();

  constructor(
    protected formuleAdhesionService: FormuleAdhesionService,
    protected formuleAdhesionFormService: FormuleAdhesionFormService,
    protected adhesionService: AdhesionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAdhesion = (o1: IAdhesion | null, o2: IAdhesion | null): boolean => this.adhesionService.compareAdhesion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formuleAdhesion }) => {
      this.formuleAdhesion = formuleAdhesion;
      if (formuleAdhesion) {
        this.updateForm(formuleAdhesion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formuleAdhesion = this.formuleAdhesionFormService.getFormuleAdhesion(this.editForm);
    if (formuleAdhesion.id !== null) {
      this.subscribeToSaveResponse(this.formuleAdhesionService.update(formuleAdhesion));
    } else {
      this.subscribeToSaveResponse(this.formuleAdhesionService.create(formuleAdhesion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormuleAdhesion>>): void {
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

  protected updateForm(formuleAdhesion: IFormuleAdhesion): void {
    this.formuleAdhesion = formuleAdhesion;
    this.formuleAdhesionFormService.resetForm(this.editForm, formuleAdhesion);

    this.adhesionsSharedCollection = this.adhesionService.addAdhesionToCollectionIfMissing<IAdhesion>(
      this.adhesionsSharedCollection,
      formuleAdhesion.adhesion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.adhesionService
      .query()
      .pipe(map((res: HttpResponse<IAdhesion[]>) => res.body ?? []))
      .pipe(
        map((adhesions: IAdhesion[]) =>
          this.adhesionService.addAdhesionToCollectionIfMissing<IAdhesion>(adhesions, this.formuleAdhesion?.adhesion)
        )
      )
      .subscribe((adhesions: IAdhesion[]) => (this.adhesionsSharedCollection = adhesions));
  }
}
