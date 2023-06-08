import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CompteTontineFormService, CompteTontineFormGroup } from './compte-tontine-form.service';
import { ICompteTontine } from '../compte-tontine.model';
import { CompteTontineService } from '../service/compte-tontine.service';
import { ITontine } from 'app/entities/tontine/tontine.model';
import { TontineService } from 'app/entities/tontine/service/tontine.service';

@Component({
  selector: 'jhi-compte-tontine-update',
  templateUrl: './compte-tontine-update.component.html',
})
export class CompteTontineUpdateComponent implements OnInit {
  isSaving = false;
  compteTontine: ICompteTontine | null = null;

  tontinesSharedCollection: ITontine[] = [];

  editForm: CompteTontineFormGroup = this.compteTontineFormService.createCompteTontineFormGroup();

  constructor(
    protected compteTontineService: CompteTontineService,
    protected compteTontineFormService: CompteTontineFormService,
    protected tontineService: TontineService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTontine = (o1: ITontine | null, o2: ITontine | null): boolean => this.tontineService.compareTontine(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compteTontine }) => {
      this.compteTontine = compteTontine;
      if (compteTontine) {
        this.updateForm(compteTontine);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compteTontine = this.compteTontineFormService.getCompteTontine(this.editForm);
    if (compteTontine.id !== null) {
      this.subscribeToSaveResponse(this.compteTontineService.update(compteTontine));
    } else {
      this.subscribeToSaveResponse(this.compteTontineService.create(compteTontine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompteTontine>>): void {
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

  protected updateForm(compteTontine: ICompteTontine): void {
    this.compteTontine = compteTontine;
    this.compteTontineFormService.resetForm(this.editForm, compteTontine);

    this.tontinesSharedCollection = this.tontineService.addTontineToCollectionIfMissing<ITontine>(
      this.tontinesSharedCollection,
      compteTontine.tontine
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tontineService
      .query()
      .pipe(map((res: HttpResponse<ITontine[]>) => res.body ?? []))
      .pipe(
        map((tontines: ITontine[]) => this.tontineService.addTontineToCollectionIfMissing<ITontine>(tontines, this.compteTontine?.tontine))
      )
      .subscribe((tontines: ITontine[]) => (this.tontinesSharedCollection = tontines));
  }
}
