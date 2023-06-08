import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CotisationBanqueFormService, CotisationBanqueFormGroup } from './cotisation-banque-form.service';
import { ICotisationBanque } from '../cotisation-banque.model';
import { CotisationBanqueService } from '../service/cotisation-banque.service';
import { ICompteBanque } from 'app/entities/compte-banque/compte-banque.model';
import { CompteBanqueService } from 'app/entities/compte-banque/service/compte-banque.service';

@Component({
  selector: 'jhi-cotisation-banque-update',
  templateUrl: './cotisation-banque-update.component.html',
})
export class CotisationBanqueUpdateComponent implements OnInit {
  isSaving = false;
  cotisationBanque: ICotisationBanque | null = null;

  compteBanquesSharedCollection: ICompteBanque[] = [];

  editForm: CotisationBanqueFormGroup = this.cotisationBanqueFormService.createCotisationBanqueFormGroup();

  constructor(
    protected cotisationBanqueService: CotisationBanqueService,
    protected cotisationBanqueFormService: CotisationBanqueFormService,
    protected compteBanqueService: CompteBanqueService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCompteBanque = (o1: ICompteBanque | null, o2: ICompteBanque | null): boolean =>
    this.compteBanqueService.compareCompteBanque(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cotisationBanque }) => {
      this.cotisationBanque = cotisationBanque;
      if (cotisationBanque) {
        this.updateForm(cotisationBanque);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cotisationBanque = this.cotisationBanqueFormService.getCotisationBanque(this.editForm);
    if (cotisationBanque.id !== null) {
      this.subscribeToSaveResponse(this.cotisationBanqueService.update(cotisationBanque));
    } else {
      this.subscribeToSaveResponse(this.cotisationBanqueService.create(cotisationBanque));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICotisationBanque>>): void {
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

  protected updateForm(cotisationBanque: ICotisationBanque): void {
    this.cotisationBanque = cotisationBanque;
    this.cotisationBanqueFormService.resetForm(this.editForm, cotisationBanque);

    this.compteBanquesSharedCollection = this.compteBanqueService.addCompteBanqueToCollectionIfMissing<ICompteBanque>(
      this.compteBanquesSharedCollection,
      cotisationBanque.compteBanque
    );
  }

  protected loadRelationshipsOptions(): void {
    this.compteBanqueService
      .query()
      .pipe(map((res: HttpResponse<ICompteBanque[]>) => res.body ?? []))
      .pipe(
        map((compteBanques: ICompteBanque[]) =>
          this.compteBanqueService.addCompteBanqueToCollectionIfMissing<ICompteBanque>(compteBanques, this.cotisationBanque?.compteBanque)
        )
      )
      .subscribe((compteBanques: ICompteBanque[]) => (this.compteBanquesSharedCollection = compteBanques));
  }
}
