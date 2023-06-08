import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGestionnaireBanque, NewGestionnaireBanque } from '../gestionnaire-banque.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGestionnaireBanque for edit and NewGestionnaireBanqueFormGroupInput for create.
 */
type GestionnaireBanqueFormGroupInput = IGestionnaireBanque | PartialWithRequiredKeyOf<NewGestionnaireBanque>;

type GestionnaireBanqueFormDefaults = Pick<NewGestionnaireBanque, 'id'>;

type GestionnaireBanqueFormGroupContent = {
  id: FormControl<IGestionnaireBanque['id'] | NewGestionnaireBanque['id']>;
  matriculeMembre: FormControl<IGestionnaireBanque['matriculeMembre']>;
  banque: FormControl<IGestionnaireBanque['banque']>;
};

export type GestionnaireBanqueFormGroup = FormGroup<GestionnaireBanqueFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GestionnaireBanqueFormService {
  createGestionnaireBanqueFormGroup(gestionnaireBanque: GestionnaireBanqueFormGroupInput = { id: null }): GestionnaireBanqueFormGroup {
    const gestionnaireBanqueRawValue = {
      ...this.getFormDefaults(),
      ...gestionnaireBanque,
    };
    return new FormGroup<GestionnaireBanqueFormGroupContent>({
      id: new FormControl(
        { value: gestionnaireBanqueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      matriculeMembre: new FormControl(gestionnaireBanqueRawValue.matriculeMembre),
      banque: new FormControl(gestionnaireBanqueRawValue.banque),
    });
  }

  getGestionnaireBanque(form: GestionnaireBanqueFormGroup): IGestionnaireBanque | NewGestionnaireBanque {
    return form.getRawValue() as IGestionnaireBanque | NewGestionnaireBanque;
  }

  resetForm(form: GestionnaireBanqueFormGroup, gestionnaireBanque: GestionnaireBanqueFormGroupInput): void {
    const gestionnaireBanqueRawValue = { ...this.getFormDefaults(), ...gestionnaireBanque };
    form.reset(
      {
        ...gestionnaireBanqueRawValue,
        id: { value: gestionnaireBanqueRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GestionnaireBanqueFormDefaults {
    return {
      id: null,
    };
  }
}
