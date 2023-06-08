import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CompteBanqueFormService, CompteBanqueFormGroup } from './compte-banque-form.service';
import { ICompteBanque } from '../compte-banque.model';
import { CompteBanqueService } from '../service/compte-banque.service';
import { IBanque } from 'app/entities/banque/banque.model';
import { BanqueService } from 'app/entities/banque/service/banque.service';

@Component({
  selector: 'jhi-compte-banque-update',
  templateUrl: './compte-banque-update.component.html',
})
export class CompteBanqueUpdateComponent implements OnInit {
  isSaving = false;
  compteBanque: ICompteBanque | null = null;

  banquesSharedCollection: IBanque[] = [];

  editForm: CompteBanqueFormGroup = this.compteBanqueFormService.createCompteBanqueFormGroup();

  constructor(
    protected compteBanqueService: CompteBanqueService,
    protected compteBanqueFormService: CompteBanqueFormService,
    protected banqueService: BanqueService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBanque = (o1: IBanque | null, o2: IBanque | null): boolean => this.banqueService.compareBanque(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compteBanque }) => {
      this.compteBanque = compteBanque;
      if (compteBanque) {
        this.updateForm(compteBanque);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compteBanque = this.compteBanqueFormService.getCompteBanque(this.editForm);
    if (compteBanque.id !== null) {
      this.subscribeToSaveResponse(this.compteBanqueService.update(compteBanque));
    } else {
      this.subscribeToSaveResponse(this.compteBanqueService.create(compteBanque));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompteBanque>>): void {
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

  protected updateForm(compteBanque: ICompteBanque): void {
    this.compteBanque = compteBanque;
    this.compteBanqueFormService.resetForm(this.editForm, compteBanque);

    this.banquesSharedCollection = this.banqueService.addBanqueToCollectionIfMissing<IBanque>(
      this.banquesSharedCollection,
      compteBanque.banque
    );
  }

  protected loadRelationshipsOptions(): void {
    this.banqueService
      .query()
      .pipe(map((res: HttpResponse<IBanque[]>) => res.body ?? []))
      .pipe(map((banques: IBanque[]) => this.banqueService.addBanqueToCollectionIfMissing<IBanque>(banques, this.compteBanque?.banque)))
      .subscribe((banques: IBanque[]) => (this.banquesSharedCollection = banques));
  }
}
