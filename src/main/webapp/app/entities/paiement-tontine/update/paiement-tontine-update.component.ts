import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PaiementTontineFormService, PaiementTontineFormGroup } from './paiement-tontine-form.service';
import { IPaiementTontine } from '../paiement-tontine.model';
import { PaiementTontineService } from '../service/paiement-tontine.service';
import { ICotisationTontine } from 'app/entities/cotisation-tontine/cotisation-tontine.model';
import { CotisationTontineService } from 'app/entities/cotisation-tontine/service/cotisation-tontine.service';
import { IDecaissementTontine } from 'app/entities/decaissement-tontine/decaissement-tontine.model';
import { DecaissementTontineService } from 'app/entities/decaissement-tontine/service/decaissement-tontine.service';

@Component({
  selector: 'jhi-paiement-tontine-update',
  templateUrl: './paiement-tontine-update.component.html',
})
export class PaiementTontineUpdateComponent implements OnInit {
  isSaving = false;
  paiementTontine: IPaiementTontine | null = null;

  cotisationTontinesSharedCollection: ICotisationTontine[] = [];
  decaissementTontinesSharedCollection: IDecaissementTontine[] = [];

  editForm: PaiementTontineFormGroup = this.paiementTontineFormService.createPaiementTontineFormGroup();

  constructor(
    protected paiementTontineService: PaiementTontineService,
    protected paiementTontineFormService: PaiementTontineFormService,
    protected cotisationTontineService: CotisationTontineService,
    protected decaissementTontineService: DecaissementTontineService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCotisationTontine = (o1: ICotisationTontine | null, o2: ICotisationTontine | null): boolean =>
    this.cotisationTontineService.compareCotisationTontine(o1, o2);

  compareDecaissementTontine = (o1: IDecaissementTontine | null, o2: IDecaissementTontine | null): boolean =>
    this.decaissementTontineService.compareDecaissementTontine(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paiementTontine }) => {
      this.paiementTontine = paiementTontine;
      if (paiementTontine) {
        this.updateForm(paiementTontine);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paiementTontine = this.paiementTontineFormService.getPaiementTontine(this.editForm);
    if (paiementTontine.id !== null) {
      this.subscribeToSaveResponse(this.paiementTontineService.update(paiementTontine));
    } else {
      this.subscribeToSaveResponse(this.paiementTontineService.create(paiementTontine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaiementTontine>>): void {
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

  protected updateForm(paiementTontine: IPaiementTontine): void {
    this.paiementTontine = paiementTontine;
    this.paiementTontineFormService.resetForm(this.editForm, paiementTontine);

    this.cotisationTontinesSharedCollection = this.cotisationTontineService.addCotisationTontineToCollectionIfMissing<ICotisationTontine>(
      this.cotisationTontinesSharedCollection,
      paiementTontine.cotisationTontine
    );
    this.decaissementTontinesSharedCollection =
      this.decaissementTontineService.addDecaissementTontineToCollectionIfMissing<IDecaissementTontine>(
        this.decaissementTontinesSharedCollection,
        paiementTontine.decaissementTontine
      );
  }

  protected loadRelationshipsOptions(): void {
    this.cotisationTontineService
      .query()
      .pipe(map((res: HttpResponse<ICotisationTontine[]>) => res.body ?? []))
      .pipe(
        map((cotisationTontines: ICotisationTontine[]) =>
          this.cotisationTontineService.addCotisationTontineToCollectionIfMissing<ICotisationTontine>(
            cotisationTontines,
            this.paiementTontine?.cotisationTontine
          )
        )
      )
      .subscribe((cotisationTontines: ICotisationTontine[]) => (this.cotisationTontinesSharedCollection = cotisationTontines));

    this.decaissementTontineService
      .query()
      .pipe(map((res: HttpResponse<IDecaissementTontine[]>) => res.body ?? []))
      .pipe(
        map((decaissementTontines: IDecaissementTontine[]) =>
          this.decaissementTontineService.addDecaissementTontineToCollectionIfMissing<IDecaissementTontine>(
            decaissementTontines,
            this.paiementTontine?.decaissementTontine
          )
        )
      )
      .subscribe((decaissementTontines: IDecaissementTontine[]) => (this.decaissementTontinesSharedCollection = decaissementTontines));
  }
}
