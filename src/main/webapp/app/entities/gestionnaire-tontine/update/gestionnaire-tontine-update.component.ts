import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { GestionnaireTontineFormService, GestionnaireTontineFormGroup } from './gestionnaire-tontine-form.service';
import { IGestionnaireTontine } from '../gestionnaire-tontine.model';
import { GestionnaireTontineService } from '../service/gestionnaire-tontine.service';
import { ITontine } from 'app/entities/tontine/tontine.model';
import { TontineService } from 'app/entities/tontine/service/tontine.service';

@Component({
  selector: 'jhi-gestionnaire-tontine-update',
  templateUrl: './gestionnaire-tontine-update.component.html',
})
export class GestionnaireTontineUpdateComponent implements OnInit {
  isSaving = false;
  gestionnaireTontine: IGestionnaireTontine | null = null;

  tontinesSharedCollection: ITontine[] = [];

  editForm: GestionnaireTontineFormGroup = this.gestionnaireTontineFormService.createGestionnaireTontineFormGroup();

  constructor(
    protected gestionnaireTontineService: GestionnaireTontineService,
    protected gestionnaireTontineFormService: GestionnaireTontineFormService,
    protected tontineService: TontineService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTontine = (o1: ITontine | null, o2: ITontine | null): boolean => this.tontineService.compareTontine(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gestionnaireTontine }) => {
      this.gestionnaireTontine = gestionnaireTontine;
      if (gestionnaireTontine) {
        this.updateForm(gestionnaireTontine);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gestionnaireTontine = this.gestionnaireTontineFormService.getGestionnaireTontine(this.editForm);
    if (gestionnaireTontine.id !== null) {
      this.subscribeToSaveResponse(this.gestionnaireTontineService.update(gestionnaireTontine));
    } else {
      this.subscribeToSaveResponse(this.gestionnaireTontineService.create(gestionnaireTontine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGestionnaireTontine>>): void {
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

  protected updateForm(gestionnaireTontine: IGestionnaireTontine): void {
    this.gestionnaireTontine = gestionnaireTontine;
    this.gestionnaireTontineFormService.resetForm(this.editForm, gestionnaireTontine);

    this.tontinesSharedCollection = this.tontineService.addTontineToCollectionIfMissing<ITontine>(
      this.tontinesSharedCollection,
      gestionnaireTontine.tontine
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tontineService
      .query()
      .pipe(map((res: HttpResponse<ITontine[]>) => res.body ?? []))
      .pipe(
        map((tontines: ITontine[]) =>
          this.tontineService.addTontineToCollectionIfMissing<ITontine>(tontines, this.gestionnaireTontine?.tontine)
        )
      )
      .subscribe((tontines: ITontine[]) => (this.tontinesSharedCollection = tontines));
  }
}
