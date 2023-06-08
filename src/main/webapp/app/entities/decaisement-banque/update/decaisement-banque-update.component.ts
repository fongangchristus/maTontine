import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DecaisementBanqueFormService, DecaisementBanqueFormGroup } from './decaisement-banque-form.service';
import { IDecaisementBanque } from '../decaisement-banque.model';
import { DecaisementBanqueService } from '../service/decaisement-banque.service';
import { ICompteBanque } from 'app/entities/compte-banque/compte-banque.model';
import { CompteBanqueService } from 'app/entities/compte-banque/service/compte-banque.service';

@Component({
  selector: 'jhi-decaisement-banque-update',
  templateUrl: './decaisement-banque-update.component.html',
})
export class DecaisementBanqueUpdateComponent implements OnInit {
  isSaving = false;
  decaisementBanque: IDecaisementBanque | null = null;

  compteBanquesSharedCollection: ICompteBanque[] = [];

  editForm: DecaisementBanqueFormGroup = this.decaisementBanqueFormService.createDecaisementBanqueFormGroup();

  constructor(
    protected decaisementBanqueService: DecaisementBanqueService,
    protected decaisementBanqueFormService: DecaisementBanqueFormService,
    protected compteBanqueService: CompteBanqueService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCompteBanque = (o1: ICompteBanque | null, o2: ICompteBanque | null): boolean =>
    this.compteBanqueService.compareCompteBanque(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ decaisementBanque }) => {
      this.decaisementBanque = decaisementBanque;
      if (decaisementBanque) {
        this.updateForm(decaisementBanque);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const decaisementBanque = this.decaisementBanqueFormService.getDecaisementBanque(this.editForm);
    if (decaisementBanque.id !== null) {
      this.subscribeToSaveResponse(this.decaisementBanqueService.update(decaisementBanque));
    } else {
      this.subscribeToSaveResponse(this.decaisementBanqueService.create(decaisementBanque));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDecaisementBanque>>): void {
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

  protected updateForm(decaisementBanque: IDecaisementBanque): void {
    this.decaisementBanque = decaisementBanque;
    this.decaisementBanqueFormService.resetForm(this.editForm, decaisementBanque);

    this.compteBanquesSharedCollection = this.compteBanqueService.addCompteBanqueToCollectionIfMissing<ICompteBanque>(
      this.compteBanquesSharedCollection,
      decaisementBanque.compteBanque
    );
  }

  protected loadRelationshipsOptions(): void {
    this.compteBanqueService
      .query()
      .pipe(map((res: HttpResponse<ICompteBanque[]>) => res.body ?? []))
      .pipe(
        map((compteBanques: ICompteBanque[]) =>
          this.compteBanqueService.addCompteBanqueToCollectionIfMissing<ICompteBanque>(compteBanques, this.decaisementBanque?.compteBanque)
        )
      )
      .subscribe((compteBanques: ICompteBanque[]) => (this.compteBanquesSharedCollection = compteBanques));
  }
}
