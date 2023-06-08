import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISanctionConfiguration, NewSanctionConfiguration } from '../sanction-configuration.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISanctionConfiguration for edit and NewSanctionConfigurationFormGroupInput for create.
 */
type SanctionConfigurationFormGroupInput = ISanctionConfiguration | PartialWithRequiredKeyOf<NewSanctionConfiguration>;

type SanctionConfigurationFormDefaults = Pick<NewSanctionConfiguration, 'id'>;

type SanctionConfigurationFormGroupContent = {
  id: FormControl<ISanctionConfiguration['id'] | NewSanctionConfiguration['id']>;
  codeAssociation: FormControl<ISanctionConfiguration['codeAssociation']>;
  codeTontine: FormControl<ISanctionConfiguration['codeTontine']>;
  type: FormControl<ISanctionConfiguration['type']>;
};

export type SanctionConfigurationFormGroup = FormGroup<SanctionConfigurationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SanctionConfigurationFormService {
  createSanctionConfigurationFormGroup(
    sanctionConfiguration: SanctionConfigurationFormGroupInput = { id: null }
  ): SanctionConfigurationFormGroup {
    const sanctionConfigurationRawValue = {
      ...this.getFormDefaults(),
      ...sanctionConfiguration,
    };
    return new FormGroup<SanctionConfigurationFormGroupContent>({
      id: new FormControl(
        { value: sanctionConfigurationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      codeAssociation: new FormControl(sanctionConfigurationRawValue.codeAssociation, {
        validators: [Validators.required],
      }),
      codeTontine: new FormControl(sanctionConfigurationRawValue.codeTontine, {
        validators: [Validators.required],
      }),
      type: new FormControl(sanctionConfigurationRawValue.type),
    });
  }

  getSanctionConfiguration(form: SanctionConfigurationFormGroup): ISanctionConfiguration | NewSanctionConfiguration {
    return form.getRawValue() as ISanctionConfiguration | NewSanctionConfiguration;
  }

  resetForm(form: SanctionConfigurationFormGroup, sanctionConfiguration: SanctionConfigurationFormGroupInput): void {
    const sanctionConfigurationRawValue = { ...this.getFormDefaults(), ...sanctionConfiguration };
    form.reset(
      {
        ...sanctionConfigurationRawValue,
        id: { value: sanctionConfigurationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SanctionConfigurationFormDefaults {
    return {
      id: null,
    };
  }
}
