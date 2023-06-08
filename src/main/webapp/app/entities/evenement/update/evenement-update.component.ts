import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EvenementFormService, EvenementFormGroup } from './evenement-form.service';
import { IEvenement } from '../evenement.model';
import { EvenementService } from '../service/evenement.service';
import { ITypeEvenement } from 'app/entities/type-evenement/type-evenement.model';
import { TypeEvenementService } from 'app/entities/type-evenement/service/type-evenement.service';

@Component({
  selector: 'jhi-evenement-update',
  templateUrl: './evenement-update.component.html',
})
export class EvenementUpdateComponent implements OnInit {
  isSaving = false;
  evenement: IEvenement | null = null;

  typeEvenementsSharedCollection: ITypeEvenement[] = [];

  editForm: EvenementFormGroup = this.evenementFormService.createEvenementFormGroup();

  constructor(
    protected evenementService: EvenementService,
    protected evenementFormService: EvenementFormService,
    protected typeEvenementService: TypeEvenementService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTypeEvenement = (o1: ITypeEvenement | null, o2: ITypeEvenement | null): boolean =>
    this.typeEvenementService.compareTypeEvenement(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evenement }) => {
      this.evenement = evenement;
      if (evenement) {
        this.updateForm(evenement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evenement = this.evenementFormService.getEvenement(this.editForm);
    if (evenement.id !== null) {
      this.subscribeToSaveResponse(this.evenementService.update(evenement));
    } else {
      this.subscribeToSaveResponse(this.evenementService.create(evenement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvenement>>): void {
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

  protected updateForm(evenement: IEvenement): void {
    this.evenement = evenement;
    this.evenementFormService.resetForm(this.editForm, evenement);

    this.typeEvenementsSharedCollection = this.typeEvenementService.addTypeEvenementToCollectionIfMissing<ITypeEvenement>(
      this.typeEvenementsSharedCollection,
      evenement.typeEvenement
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeEvenementService
      .query()
      .pipe(map((res: HttpResponse<ITypeEvenement[]>) => res.body ?? []))
      .pipe(
        map((typeEvenements: ITypeEvenement[]) =>
          this.typeEvenementService.addTypeEvenementToCollectionIfMissing<ITypeEvenement>(typeEvenements, this.evenement?.typeEvenement)
        )
      )
      .subscribe((typeEvenements: ITypeEvenement[]) => (this.typeEvenementsSharedCollection = typeEvenements));
  }
}
