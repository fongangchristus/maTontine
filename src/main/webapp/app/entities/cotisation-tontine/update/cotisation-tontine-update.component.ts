import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CotisationTontineFormService, CotisationTontineFormGroup } from './cotisation-tontine-form.service';
import { ICotisationTontine } from '../cotisation-tontine.model';
import { CotisationTontineService } from '../service/cotisation-tontine.service';
import { ISessionTontine } from 'app/entities/session-tontine/session-tontine.model';
import { SessionTontineService } from 'app/entities/session-tontine/service/session-tontine.service';
import { ICompteTontine } from 'app/entities/compte-tontine/compte-tontine.model';
import { CompteTontineService } from 'app/entities/compte-tontine/service/compte-tontine.service';
import { EtatCotisation } from 'app/entities/enumerations/etat-cotisation.model';

@Component({
  selector: 'jhi-cotisation-tontine-update',
  templateUrl: './cotisation-tontine-update.component.html',
})
export class CotisationTontineUpdateComponent implements OnInit {
  isSaving = false;
  cotisationTontine: ICotisationTontine | null = null;
  etatCotisationValues = Object.keys(EtatCotisation);

  sessionTontinesSharedCollection: ISessionTontine[] = [];
  compteTontinesSharedCollection: ICompteTontine[] = [];

  editForm: CotisationTontineFormGroup = this.cotisationTontineFormService.createCotisationTontineFormGroup();

  constructor(
    protected cotisationTontineService: CotisationTontineService,
    protected cotisationTontineFormService: CotisationTontineFormService,
    protected sessionTontineService: SessionTontineService,
    protected compteTontineService: CompteTontineService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSessionTontine = (o1: ISessionTontine | null, o2: ISessionTontine | null): boolean =>
    this.sessionTontineService.compareSessionTontine(o1, o2);

  compareCompteTontine = (o1: ICompteTontine | null, o2: ICompteTontine | null): boolean =>
    this.compteTontineService.compareCompteTontine(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cotisationTontine }) => {
      this.cotisationTontine = cotisationTontine;
      if (cotisationTontine) {
        this.updateForm(cotisationTontine);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cotisationTontine = this.cotisationTontineFormService.getCotisationTontine(this.editForm);
    if (cotisationTontine.id !== null) {
      this.subscribeToSaveResponse(this.cotisationTontineService.update(cotisationTontine));
    } else {
      this.subscribeToSaveResponse(this.cotisationTontineService.create(cotisationTontine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICotisationTontine>>): void {
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

  protected updateForm(cotisationTontine: ICotisationTontine): void {
    this.cotisationTontine = cotisationTontine;
    this.cotisationTontineFormService.resetForm(this.editForm, cotisationTontine);

    this.sessionTontinesSharedCollection = this.sessionTontineService.addSessionTontineToCollectionIfMissing<ISessionTontine>(
      this.sessionTontinesSharedCollection,
      cotisationTontine.sessionTontine
    );
    this.compteTontinesSharedCollection = this.compteTontineService.addCompteTontineToCollectionIfMissing<ICompteTontine>(
      this.compteTontinesSharedCollection,
      cotisationTontine.compteTontine
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
            this.cotisationTontine?.sessionTontine
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
            this.cotisationTontine?.compteTontine
          )
        )
      )
      .subscribe((compteTontines: ICompteTontine[]) => (this.compteTontinesSharedCollection = compteTontines));
  }
}
