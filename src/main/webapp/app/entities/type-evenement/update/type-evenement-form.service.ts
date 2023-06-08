import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeEvenement, NewTypeEvenement } from '../type-evenement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeEvenement for edit and NewTypeEvenementFormGroupInput for create.
 */
type TypeEvenementFormGroupInput = ITypeEvenement | PartialWithRequiredKeyOf<NewTypeEvenement>;

type TypeEvenementFormDefaults = Pick<NewTypeEvenement, 'id'>;

type TypeEvenementFormGroupContent = {
  id: FormControl<ITypeEvenement['id'] | NewTypeEvenement['id']>;
  libele: FormControl<ITypeEvenement['libele']>;
  observation: FormControl<ITypeEvenement['observation']>;
};

export type TypeEvenementFormGroup = FormGroup<TypeEvenementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeEvenementFormService {
  createTypeEvenementFormGroup(typeEvenement: TypeEvenementFormGroupInput = { id: null }): TypeEvenementFormGroup {
    const typeEvenementRawValue = {
      ...this.getFormDefaults(),
      ...typeEvenement,
    };
    return new FormGroup<TypeEvenementFormGroupContent>({
      id: new FormControl(
        { value: typeEvenementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      libele: new FormControl(typeEvenementRawValue.libele),
      observation: new FormControl(typeEvenementRawValue.observation),
    });
  }

  getTypeEvenement(form: TypeEvenementFormGroup): ITypeEvenement | NewTypeEvenement {
    return form.getRawValue() as ITypeEvenement | NewTypeEvenement;
  }

  resetForm(form: TypeEvenementFormGroup, typeEvenement: TypeEvenementFormGroupInput): void {
    const typeEvenementRawValue = { ...this.getFormDefaults(), ...typeEvenement };
    form.reset(
      {
        ...typeEvenementRawValue,
        id: { value: typeEvenementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TypeEvenementFormDefaults {
    return {
      id: null,
    };
  }
}
