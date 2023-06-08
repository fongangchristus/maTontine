import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TypeEvenementFormService, TypeEvenementFormGroup } from './type-evenement-form.service';
import { ITypeEvenement } from '../type-evenement.model';
import { TypeEvenementService } from '../service/type-evenement.service';

@Component({
  selector: 'jhi-type-evenement-update',
  templateUrl: './type-evenement-update.component.html',
})
export class TypeEvenementUpdateComponent implements OnInit {
  isSaving = false;
  typeEvenement: ITypeEvenement | null = null;

  editForm: TypeEvenementFormGroup = this.typeEvenementFormService.createTypeEvenementFormGroup();

  constructor(
    protected typeEvenementService: TypeEvenementService,
    protected typeEvenementFormService: TypeEvenementFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeEvenement }) => {
      this.typeEvenement = typeEvenement;
      if (typeEvenement) {
        this.updateForm(typeEvenement);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeEvenement = this.typeEvenementFormService.getTypeEvenement(this.editForm);
    if (typeEvenement.id !== null) {
      this.subscribeToSaveResponse(this.typeEvenementService.update(typeEvenement));
    } else {
      this.subscribeToSaveResponse(this.typeEvenementService.create(typeEvenement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeEvenement>>): void {
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

  protected updateForm(typeEvenement: ITypeEvenement): void {
    this.typeEvenement = typeEvenement;
    this.typeEvenementFormService.resetForm(this.editForm, typeEvenement);
  }
}
