import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAdresse, NewAdresse } from '../adresse.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAdresse for edit and NewAdresseFormGroupInput for create.
 */
type AdresseFormGroupInput = IAdresse | PartialWithRequiredKeyOf<NewAdresse>;

type AdresseFormDefaults = Pick<NewAdresse, 'id'>;

type AdresseFormGroupContent = {
  id: FormControl<IAdresse['id'] | NewAdresse['id']>;
  departmentName: FormControl<IAdresse['departmentName']>;
  streetAddress: FormControl<IAdresse['streetAddress']>;
  postalCode: FormControl<IAdresse['postalCode']>;
  city: FormControl<IAdresse['city']>;
  stateProvince: FormControl<IAdresse['stateProvince']>;
  pays: FormControl<IAdresse['pays']>;
};

export type AdresseFormGroup = FormGroup<AdresseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AdresseFormService {
  createAdresseFormGroup(adresse: AdresseFormGroupInput = { id: null }): AdresseFormGroup {
    const adresseRawValue = {
      ...this.getFormDefaults(),
      ...adresse,
    };
    return new FormGroup<AdresseFormGroupContent>({
      id: new FormControl(
        { value: adresseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      departmentName: new FormControl(adresseRawValue.departmentName, {
        validators: [Validators.required],
      }),
      streetAddress: new FormControl(adresseRawValue.streetAddress),
      postalCode: new FormControl(adresseRawValue.postalCode),
      city: new FormControl(adresseRawValue.city),
      stateProvince: new FormControl(adresseRawValue.stateProvince),
      pays: new FormControl(adresseRawValue.pays),
    });
  }

  getAdresse(form: AdresseFormGroup): IAdresse | NewAdresse {
    return form.getRawValue() as IAdresse | NewAdresse;
  }

  resetForm(form: AdresseFormGroup, adresse: AdresseFormGroupInput): void {
    const adresseRawValue = { ...this.getFormDefaults(), ...adresse };
    form.reset(
      {
        ...adresseRawValue,
        id: { value: adresseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AdresseFormDefaults {
    return {
      id: null,
    };
  }
}
