import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { GestionnaireBanqueFormService, GestionnaireBanqueFormGroup } from './gestionnaire-banque-form.service';
import { IGestionnaireBanque } from '../gestionnaire-banque.model';
import { GestionnaireBanqueService } from '../service/gestionnaire-banque.service';
import { IBanque } from 'app/entities/banque/banque.model';
import { BanqueService } from 'app/entities/banque/service/banque.service';

@Component({
  selector: 'jhi-gestionnaire-banque-update',
  templateUrl: './gestionnaire-banque-update.component.html',
})
export class GestionnaireBanqueUpdateComponent implements OnInit {
  isSaving = false;
  gestionnaireBanque: IGestionnaireBanque | null = null;

  banquesSharedCollection: IBanque[] = [];

  editForm: GestionnaireBanqueFormGroup = this.gestionnaireBanqueFormService.createGestionnaireBanqueFormGroup();

  constructor(
    protected gestionnaireBanqueService: GestionnaireBanqueService,
    protected gestionnaireBanqueFormService: GestionnaireBanqueFormService,
    protected banqueService: BanqueService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBanque = (o1: IBanque | null, o2: IBanque | null): boolean => this.banqueService.compareBanque(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gestionnaireBanque }) => {
      this.gestionnaireBanque = gestionnaireBanque;
      if (gestionnaireBanque) {
        this.updateForm(gestionnaireBanque);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gestionnaireBanque = this.gestionnaireBanqueFormService.getGestionnaireBanque(this.editForm);
    if (gestionnaireBanque.id !== null) {
      this.subscribeToSaveResponse(this.gestionnaireBanqueService.update(gestionnaireBanque));
    } else {
      this.subscribeToSaveResponse(this.gestionnaireBanqueService.create(gestionnaireBanque));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGestionnaireBanque>>): void {
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

  protected updateForm(gestionnaireBanque: IGestionnaireBanque): void {
    this.gestionnaireBanque = gestionnaireBanque;
    this.gestionnaireBanqueFormService.resetForm(this.editForm, gestionnaireBanque);

    this.banquesSharedCollection = this.banqueService.addBanqueToCollectionIfMissing<IBanque>(
      this.banquesSharedCollection,
      gestionnaireBanque.banque
    );
  }

  protected loadRelationshipsOptions(): void {
    this.banqueService
      .query()
      .pipe(map((res: HttpResponse<IBanque[]>) => res.body ?? []))
      .pipe(
        map((banques: IBanque[]) => this.banqueService.addBanqueToCollectionIfMissing<IBanque>(banques, this.gestionnaireBanque?.banque))
      )
      .subscribe((banques: IBanque[]) => (this.banquesSharedCollection = banques));
  }
}
