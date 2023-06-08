import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypePot, NewTypePot } from '../type-pot.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypePot for edit and NewTypePotFormGroupInput for create.
 */
type TypePotFormGroupInput = ITypePot | PartialWithRequiredKeyOf<NewTypePot>;

type TypePotFormDefaults = Pick<NewTypePot, 'id'>;

type TypePotFormGroupContent = {
  id: FormControl<ITypePot['id'] | NewTypePot['id']>;
  libele: FormControl<ITypePot['libele']>;
  descrption: FormControl<ITypePot['descrption']>;
};

export type TypePotFormGroup = FormGroup<TypePotFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypePotFormService {
  createTypePotFormGroup(typePot: TypePotFormGroupInput = { id: null }): TypePotFormGroup {
    const typePotRawValue = {
      ...this.getFormDefaults(),
      ...typePot,
    };
    return new FormGroup<TypePotFormGroupContent>({
      id: new FormControl(
        { value: typePotRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      libele: new FormControl(typePotRawValue.libele, {
        validators: [Validators.required],
      }),
      descrption: new FormControl(typePotRawValue.descrption),
    });
  }

  getTypePot(form: TypePotFormGroup): ITypePot | NewTypePot {
    return form.getRawValue() as ITypePot | NewTypePot;
  }

  resetForm(form: TypePotFormGroup, typePot: TypePotFormGroupInput): void {
    const typePotRawValue = { ...this.getFormDefaults(), ...typePot };
    form.reset(
      {
        ...typePotRawValue,
        id: { value: typePotRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TypePotFormDefaults {
    return {
      id: null,
    };
  }
}
