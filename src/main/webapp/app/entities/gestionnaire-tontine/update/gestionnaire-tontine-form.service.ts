import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGestionnaireTontine, NewGestionnaireTontine } from '../gestionnaire-tontine.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGestionnaireTontine for edit and NewGestionnaireTontineFormGroupInput for create.
 */
type GestionnaireTontineFormGroupInput = IGestionnaireTontine | PartialWithRequiredKeyOf<NewGestionnaireTontine>;

type GestionnaireTontineFormDefaults = Pick<NewGestionnaireTontine, 'id'>;

type GestionnaireTontineFormGroupContent = {
  id: FormControl<IGestionnaireTontine['id'] | NewGestionnaireTontine['id']>;
  matriculeAdherent: FormControl<IGestionnaireTontine['matriculeAdherent']>;
  codeTontine: FormControl<IGestionnaireTontine['codeTontine']>;
  datePriseFonction: FormControl<IGestionnaireTontine['datePriseFonction']>;
  dateFinFonction: FormControl<IGestionnaireTontine['dateFinFonction']>;
  tontine: FormControl<IGestionnaireTontine['tontine']>;
};

export type GestionnaireTontineFormGroup = FormGroup<GestionnaireTontineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GestionnaireTontineFormService {
  createGestionnaireTontineFormGroup(gestionnaireTontine: GestionnaireTontineFormGroupInput = { id: null }): GestionnaireTontineFormGroup {
    const gestionnaireTontineRawValue = {
      ...this.getFormDefaults(),
      ...gestionnaireTontine,
    };
    return new FormGroup<GestionnaireTontineFormGroupContent>({
      id: new FormControl(
        { value: gestionnaireTontineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      matriculeAdherent: new FormControl(gestionnaireTontineRawValue.matriculeAdherent),
      codeTontine: new FormControl(gestionnaireTontineRawValue.codeTontine),
      datePriseFonction: new FormControl(gestionnaireTontineRawValue.datePriseFonction),
      dateFinFonction: new FormControl(gestionnaireTontineRawValue.dateFinFonction),
      tontine: new FormControl(gestionnaireTontineRawValue.tontine),
    });
  }

  getGestionnaireTontine(form: GestionnaireTontineFormGroup): IGestionnaireTontine | NewGestionnaireTontine {
    return form.getRawValue() as IGestionnaireTontine | NewGestionnaireTontine;
  }

  resetForm(form: GestionnaireTontineFormGroup, gestionnaireTontine: GestionnaireTontineFormGroupInput): void {
    const gestionnaireTontineRawValue = { ...this.getFormDefaults(), ...gestionnaireTontine };
    form.reset(
      {
        ...gestionnaireTontineRawValue,
        id: { value: gestionnaireTontineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GestionnaireTontineFormDefaults {
    return {
      id: null,
    };
  }
}
