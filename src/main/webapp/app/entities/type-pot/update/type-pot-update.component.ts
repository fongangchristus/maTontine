import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TypePotFormService, TypePotFormGroup } from './type-pot-form.service';
import { ITypePot } from '../type-pot.model';
import { TypePotService } from '../service/type-pot.service';

@Component({
  selector: 'jhi-type-pot-update',
  templateUrl: './type-pot-update.component.html',
})
export class TypePotUpdateComponent implements OnInit {
  isSaving = false;
  typePot: ITypePot | null = null;

  editForm: TypePotFormGroup = this.typePotFormService.createTypePotFormGroup();

  constructor(
    protected typePotService: TypePotService,
    protected typePotFormService: TypePotFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typePot }) => {
      this.typePot = typePot;
      if (typePot) {
        this.updateForm(typePot);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typePot = this.typePotFormService.getTypePot(this.editForm);
    if (typePot.id !== null) {
      this.subscribeToSaveResponse(this.typePotService.update(typePot));
    } else {
      this.subscribeToSaveResponse(this.typePotService.create(typePot));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypePot>>): void {
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

  protected updateForm(typePot: ITypePot): void {
    this.typePot = typePot;
    this.typePotFormService.resetForm(this.editForm, typePot);
  }
}
