import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ExerciseFormService, ExerciseFormGroup } from './exercise-form.service';
import { IExercise } from '../exercise.model';
import { ExerciseService } from '../service/exercise.service';
import { IAssociation } from 'app/entities/association/association.model';
import { AssociationService } from 'app/entities/association/service/association.service';
import { StatutExercice } from 'app/entities/enumerations/statut-exercice.model';

@Component({
  selector: 'jhi-exercise-update',
  templateUrl: './exercise-update.component.html',
})
export class ExerciseUpdateComponent implements OnInit {
  isSaving = false;
  exercise: IExercise | null = null;
  statutExerciceValues = Object.keys(StatutExercice);

  associationsSharedCollection: IAssociation[] = [];

  editForm: ExerciseFormGroup = this.exerciseFormService.createExerciseFormGroup();

  constructor(
    protected exerciseService: ExerciseService,
    protected exerciseFormService: ExerciseFormService,
    protected associationService: AssociationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAssociation = (o1: IAssociation | null, o2: IAssociation | null): boolean => this.associationService.compareAssociation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exercise }) => {
      this.exercise = exercise;
      if (exercise) {
        this.updateForm(exercise);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exercise = this.exerciseFormService.getExercise(this.editForm);
    if (exercise.id !== null) {
      this.subscribeToSaveResponse(this.exerciseService.update(exercise));
    } else {
      this.subscribeToSaveResponse(this.exerciseService.create(exercise));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExercise>>): void {
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

  protected updateForm(exercise: IExercise): void {
    this.exercise = exercise;
    this.exerciseFormService.resetForm(this.editForm, exercise);

    this.associationsSharedCollection = this.associationService.addAssociationToCollectionIfMissing<IAssociation>(
      this.associationsSharedCollection,
      exercise.association
    );
  }

  protected loadRelationshipsOptions(): void {
    this.associationService
      .query()
      .pipe(map((res: HttpResponse<IAssociation[]>) => res.body ?? []))
      .pipe(
        map((associations: IAssociation[]) =>
          this.associationService.addAssociationToCollectionIfMissing<IAssociation>(associations, this.exercise?.association)
        )
      )
      .subscribe((associations: IAssociation[]) => (this.associationsSharedCollection = associations));
  }
}
