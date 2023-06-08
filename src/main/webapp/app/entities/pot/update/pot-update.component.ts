import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PotFormService, PotFormGroup } from './pot-form.service';
import { IPot } from '../pot.model';
import { PotService } from '../service/pot.service';
import { ITypePot } from 'app/entities/type-pot/type-pot.model';
import { TypePotService } from 'app/entities/type-pot/service/type-pot.service';
import { StatutPot } from 'app/entities/enumerations/statut-pot.model';

@Component({
  selector: 'jhi-pot-update',
  templateUrl: './pot-update.component.html',
})
export class PotUpdateComponent implements OnInit {
  isSaving = false;
  pot: IPot | null = null;
  statutPotValues = Object.keys(StatutPot);

  typePotsSharedCollection: ITypePot[] = [];

  editForm: PotFormGroup = this.potFormService.createPotFormGroup();

  constructor(
    protected potService: PotService,
    protected potFormService: PotFormService,
    protected typePotService: TypePotService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTypePot = (o1: ITypePot | null, o2: ITypePot | null): boolean => this.typePotService.compareTypePot(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pot }) => {
      this.pot = pot;
      if (pot) {
        this.updateForm(pot);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pot = this.potFormService.getPot(this.editForm);
    if (pot.id !== null) {
      this.subscribeToSaveResponse(this.potService.update(pot));
    } else {
      this.subscribeToSaveResponse(this.potService.create(pot));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPot>>): void {
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

  protected updateForm(pot: IPot): void {
    this.pot = pot;
    this.potFormService.resetForm(this.editForm, pot);

    this.typePotsSharedCollection = this.typePotService.addTypePotToCollectionIfMissing<ITypePot>(
      this.typePotsSharedCollection,
      pot.typePot
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typePotService
      .query()
      .pipe(map((res: HttpResponse<ITypePot[]>) => res.body ?? []))
      .pipe(map((typePots: ITypePot[]) => this.typePotService.addTypePotToCollectionIfMissing<ITypePot>(typePots, this.pot?.typePot)))
      .subscribe((typePots: ITypePot[]) => (this.typePotsSharedCollection = typePots));
  }
}
