import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IExercise, NewExercise } from '../exercise.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExercise for edit and NewExerciseFormGroupInput for create.
 */
type ExerciseFormGroupInput = IExercise | PartialWithRequiredKeyOf<NewExercise>;

type ExerciseFormDefaults = Pick<NewExercise, 'id'>;

type ExerciseFormGroupContent = {
  id: FormControl<IExercise['id'] | NewExercise['id']>;
  libele: FormControl<IExercise['libele']>;
  observation: FormControl<IExercise['observation']>;
  dateDebut: FormControl<IExercise['dateDebut']>;
  dateFin: FormControl<IExercise['dateFin']>;
  statut: FormControl<IExercise['statut']>;
  association: FormControl<IExercise['association']>;
};

export type ExerciseFormGroup = FormGroup<ExerciseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExerciseFormService {
  createExerciseFormGroup(exercise: ExerciseFormGroupInput = { id: null }): ExerciseFormGroup {
    const exerciseRawValue = {
      ...this.getFormDefaults(),
      ...exercise,
    };
    return new FormGroup<ExerciseFormGroupContent>({
      id: new FormControl(
        { value: exerciseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      libele: new FormControl(exerciseRawValue.libele),
      observation: new FormControl(exerciseRawValue.observation),
      dateDebut: new FormControl(exerciseRawValue.dateDebut),
      dateFin: new FormControl(exerciseRawValue.dateFin),
      statut: new FormControl(exerciseRawValue.statut),
      association: new FormControl(exerciseRawValue.association),
    });
  }

  getExercise(form: ExerciseFormGroup): IExercise | NewExercise {
    return form.getRawValue() as IExercise | NewExercise;
  }

  resetForm(form: ExerciseFormGroup, exercise: ExerciseFormGroupInput): void {
    const exerciseRawValue = { ...this.getFormDefaults(), ...exercise };
    form.reset(
      {
        ...exerciseRawValue,
        id: { value: exerciseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ExerciseFormDefaults {
    return {
      id: null,
    };
  }
}
