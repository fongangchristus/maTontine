import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FonctionFormService, FonctionFormGroup } from './fonction-form.service';
import { IFonction } from '../fonction.model';
import { FonctionService } from '../service/fonction.service';

@Component({
  selector: 'jhi-fonction-update',
  templateUrl: './fonction-update.component.html',
})
export class FonctionUpdateComponent implements OnInit {
  isSaving = false;
  fonction: IFonction | null = null;

  editForm: FonctionFormGroup = this.fonctionFormService.createFonctionFormGroup();

  constructor(
    protected fonctionService: FonctionService,
    protected fonctionFormService: FonctionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fonction }) => {
      this.fonction = fonction;
      if (fonction) {
        this.updateForm(fonction);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fonction = this.fonctionFormService.getFonction(this.editForm);
    if (fonction.id !== null) {
      this.subscribeToSaveResponse(this.fonctionService.update(fonction));
    } else {
      this.subscribeToSaveResponse(this.fonctionService.create(fonction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFonction>>): void {
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

  protected updateForm(fonction: IFonction): void {
    this.fonction = fonction;
    this.fonctionFormService.resetForm(this.editForm, fonction);
  }
}
