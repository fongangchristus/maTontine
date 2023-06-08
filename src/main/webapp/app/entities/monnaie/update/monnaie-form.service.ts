import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMonnaie, NewMonnaie } from '../monnaie.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMonnaie for edit and NewMonnaieFormGroupInput for create.
 */
type MonnaieFormGroupInput = IMonnaie | PartialWithRequiredKeyOf<NewMonnaie>;

type MonnaieFormDefaults = Pick<NewMonnaie, 'id'>;

type MonnaieFormGroupContent = {
  id: FormControl<IMonnaie['id'] | NewMonnaie['id']>;
  libele: FormControl<IMonnaie['libele']>;
};

export type MonnaieFormGroup = FormGroup<MonnaieFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MonnaieFormService {
  createMonnaieFormGroup(monnaie: MonnaieFormGroupInput = { id: null }): MonnaieFormGroup {
    const monnaieRawValue = {
      ...this.getFormDefaults(),
      ...monnaie,
    };
    return new FormGroup<MonnaieFormGroupContent>({
      id: new FormControl(
        { value: monnaieRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      libele: new FormControl(monnaieRawValue.libele),
    });
  }

  getMonnaie(form: MonnaieFormGroup): IMonnaie | NewMonnaie {
    return form.getRawValue() as IMonnaie | NewMonnaie;
  }

  resetForm(form: MonnaieFormGroup, monnaie: MonnaieFormGroupInput): void {
    const monnaieRawValue = { ...this.getFormDefaults(), ...monnaie };
    form.reset(
      {
        ...monnaieRawValue,
        id: { value: monnaieRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MonnaieFormDefaults {
    return {
      id: null,
    };
  }
}
