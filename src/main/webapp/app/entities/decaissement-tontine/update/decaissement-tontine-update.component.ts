import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DecaissementTontineFormService, DecaissementTontineFormGroup } from './decaissement-tontine-form.service';
import { IDecaissementTontine } from '../decaissement-tontine.model';
import { DecaissementTontineService } from '../service/decaissement-tontine.service';
import { ISessionTontine } from 'app/entities/session-tontine/session-tontine.model';
import { SessionTontineService } from 'app/entities/session-tontine/service/session-tontine.service';
import { ICompteTontine } from 'app/entities/compte-tontine/compte-tontine.model';
import { CompteTontineService } from 'app/entities/compte-tontine/service/compte-tontine.service';

@Component({
  selector: 'jhi-decaissement-tontine-update',
  templateUrl: './decaissement-tontine-update.component.html',
})
export class DecaissementTontineUpdateComponent implements OnInit {
  isSaving = false;
  decaissementTontine: IDecaissementTontine | null = null;

  sessionTontinesSharedCollection: ISessionTontine[] = [];
  compteTontinesSharedCollection: ICompteTontine[] = [];

  editForm: DecaissementTontineFormGroup = this.decaissementTontineFormService.createDecaissementTontineFormGroup();

  constructor(
    protected decaissementTontineService: DecaissementTontineService,
    protected decaissementTontineFormService: DecaissementTontineFormService,
    protected sessionTontineService: SessionTontineService,
    protected compteTontineService: CompteTontineService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSessionTontine = (o1: ISessionTontine | null, o2: ISessionTontine | null): boolean =>
    this.sessionTontineService.compareSessionTontine(o1, o2);

  compareCompteTontine = (o1: ICompteTontine | null, o2: ICompteTontine | null): boolean =>
    this.compteTontineService.compareCompteTontine(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ decaissementTontine }) => {
      this.decaissementTontine = decaissementTontine;
      if (decaissementTontine) {
        this.updateForm(decaissementTontine);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const decaissementTontine = this.decaissementTontineFormService.getDecaissementTontine(this.editForm);
    if (decaissementTontine.id !== null) {
      this.subscribeToSaveResponse(this.decaissementTontineService.update(decaissementTontine));
    } else {
      this.subscribeToSaveResponse(this.decaissementTontineService.create(decaissementTontine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDecaissementTontine>>): void {
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

  protected updateForm(decaissementTontine: IDecaissementTontine): void {
    this.decaissementTontine = decaissementTontine;
    this.decaissementTontineFormService.resetForm(this.editForm, decaissementTontine);

    this.sessionTontinesSharedCollection = this.sessionTontineService.addSessionTontineToCollectionIfMissing<ISessionTontine>(
      this.sessionTontinesSharedCollection,
      decaissementTontine.tontine
    );
    this.compteTontinesSharedCollection = this.compteTontineService.addCompteTontineToCollectionIfMissing<ICompteTontine>(
      this.compteTontinesSharedCollection,
      decaissementTontine.compteTontine
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sessionTontineService
      .query()
      .pipe(map((res: HttpResponse<ISessionTontine[]>) => res.body ?? []))
      .pipe(
        map((sessionTontines: ISessionTontine[]) =>
          this.sessionTontineService.addSessionTontineToCollectionIfMissing<ISessionTontine>(
            sessionTontines,
            this.decaissementTontine?.tontine
          )
        )
      )
      .subscribe((sessionTontines: ISessionTontine[]) => (this.sessionTontinesSharedCollection = sessionTontines));

    this.compteTontineService
      .query()
      .pipe(map((res: HttpResponse<ICompteTontine[]>) => res.body ?? []))
      .pipe(
        map((compteTontines: ICompteTontine[]) =>
          this.compteTontineService.addCompteTontineToCollectionIfMissing<ICompteTontine>(
            compteTontines,
            this.decaissementTontine?.compteTontine
          )
        )
      )
      .subscribe((compteTontines: ICompteTontine[]) => (this.compteTontinesSharedCollection = compteTontines));
  }
}
