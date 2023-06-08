import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFonction, NewFonction } from '../fonction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFonction for edit and NewFonctionFormGroupInput for create.
 */
type FonctionFormGroupInput = IFonction | PartialWithRequiredKeyOf<NewFonction>;

type FonctionFormDefaults = Pick<NewFonction, 'id'>;

type FonctionFormGroupContent = {
  id: FormControl<IFonction['id'] | NewFonction['id']>;
  title: FormControl<IFonction['title']>;
  description: FormControl<IFonction['description']>;
};

export type FonctionFormGroup = FormGroup<FonctionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FonctionFormService {
  createFonctionFormGroup(fonction: FonctionFormGroupInput = { id: null }): FonctionFormGroup {
    const fonctionRawValue = {
      ...this.getFormDefaults(),
      ...fonction,
    };
    return new FormGroup<FonctionFormGroupContent>({
      id: new FormControl(
        { value: fonctionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(fonctionRawValue.title),
      description: new FormControl(fonctionRawValue.description),
    });
  }

  getFonction(form: FonctionFormGroup): IFonction | NewFonction {
    return form.getRawValue() as IFonction | NewFonction;
  }

  resetForm(form: FonctionFormGroup, fonction: FonctionFormGroupInput): void {
    const fonctionRawValue = { ...this.getFormDefaults(), ...fonction };
    form.reset(
      {
        ...fonctionRawValue,
        id: { value: fonctionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FonctionFormDefaults {
    return {
      id: null,
    };
  }
}
