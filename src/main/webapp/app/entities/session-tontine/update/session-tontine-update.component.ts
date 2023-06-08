import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SessionTontineFormService, SessionTontineFormGroup } from './session-tontine-form.service';
import { ISessionTontine } from '../session-tontine.model';
import { SessionTontineService } from '../service/session-tontine.service';
import { ITontine } from 'app/entities/tontine/tontine.model';
import { TontineService } from 'app/entities/tontine/service/tontine.service';

@Component({
  selector: 'jhi-session-tontine-update',
  templateUrl: './session-tontine-update.component.html',
})
export class SessionTontineUpdateComponent implements OnInit {
  isSaving = false;
  sessionTontine: ISessionTontine | null = null;

  tontinesSharedCollection: ITontine[] = [];

  editForm: SessionTontineFormGroup = this.sessionTontineFormService.createSessionTontineFormGroup();

  constructor(
    protected sessionTontineService: SessionTontineService,
    protected sessionTontineFormService: SessionTontineFormService,
    protected tontineService: TontineService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTontine = (o1: ITontine | null, o2: ITontine | null): boolean => this.tontineService.compareTontine(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sessionTontine }) => {
      this.sessionTontine = sessionTontine;
      if (sessionTontine) {
        this.updateForm(sessionTontine);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sessionTontine = this.sessionTontineFormService.getSessionTontine(this.editForm);
    if (sessionTontine.id !== null) {
      this.subscribeToSaveResponse(this.sessionTontineService.update(sessionTontine));
    } else {
      this.subscribeToSaveResponse(this.sessionTontineService.create(sessionTontine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISessionTontine>>): void {
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

  protected updateForm(sessionTontine: ISessionTontine): void {
    this.sessionTontine = sessionTontine;
    this.sessionTontineFormService.resetForm(this.editForm, sessionTontine);

    this.tontinesSharedCollection = this.tontineService.addTontineToCollectionIfMissing<ITontine>(
      this.tontinesSharedCollection,
      sessionTontine.tontine
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tontineService
      .query()
      .pipe(map((res: HttpResponse<ITontine[]>) => res.body ?? []))
      .pipe(
        map((tontines: ITontine[]) => this.tontineService.addTontineToCollectionIfMissing<ITontine>(tontines, this.sessionTontine?.tontine))
      )
      .subscribe((tontines: ITontine[]) => (this.tontinesSharedCollection = tontines));
  }
}
