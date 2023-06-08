import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AssembleFormService, AssembleFormGroup } from './assemble-form.service';
import { IAssemble } from '../assemble.model';
import { AssembleService } from '../service/assemble.service';
import { NatureAssemble } from 'app/entities/enumerations/nature-assemble.model';

@Component({
  selector: 'jhi-assemble-update',
  templateUrl: './assemble-update.component.html',
})
export class AssembleUpdateComponent implements OnInit {
  isSaving = false;
  assemble: IAssemble | null = null;
  natureAssembleValues = Object.keys(NatureAssemble);

  editForm: AssembleFormGroup = this.assembleFormService.createAssembleFormGroup();

  constructor(
    protected assembleService: AssembleService,
    protected assembleFormService: AssembleFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assemble }) => {
      this.assemble = assemble;
      if (assemble) {
        this.updateForm(assemble);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assemble = this.assembleFormService.getAssemble(this.editForm);
    if (assemble.id !== null) {
      this.subscribeToSaveResponse(this.assembleService.update(assemble));
    } else {
      this.subscribeToSaveResponse(this.assembleService.create(assemble));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssemble>>): void {
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

  protected updateForm(assemble: IAssemble): void {
    this.assemble = assemble;
    this.assembleFormService.resetForm(this.editForm, assemble);
  }
}
