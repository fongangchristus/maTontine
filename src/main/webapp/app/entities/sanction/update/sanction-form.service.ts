import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISanction, NewSanction } from '../sanction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISanction for edit and NewSanctionFormGroupInput for create.
 */
type SanctionFormGroupInput = ISanction | PartialWithRequiredKeyOf<NewSanction>;

type SanctionFormDefaults = Pick<NewSanction, 'id'>;

type SanctionFormGroupContent = {
  id: FormControl<ISanction['id'] | NewSanction['id']>;
  libelle: FormControl<ISanction['libelle']>;
  matriculeAdherent: FormControl<ISanction['matriculeAdherent']>;
  dateSanction: FormControl<ISanction['dateSanction']>;
  motifSanction: FormControl<ISanction['motifSanction']>;
  description: FormControl<ISanction['description']>;
  codeActivite: FormControl<ISanction['codeActivite']>;
  sanctionConfig: FormControl<ISanction['sanctionConfig']>;
};

export type SanctionFormGroup = FormGroup<SanctionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SanctionFormService {
  createSanctionFormGroup(sanction: SanctionFormGroupInput = { id: null }): SanctionFormGroup {
    const sanctionRawValue = {
      ...this.getFormDefaults(),
      ...sanction,
    };
    return new FormGroup<SanctionFormGroupContent>({
      id: new FormControl(
        { value: sanctionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      libelle: new FormControl(sanctionRawValue.libelle),
      matriculeAdherent: new FormControl(sanctionRawValue.matriculeAdherent, {
        validators: [Validators.required],
      }),
      dateSanction: new FormControl(sanctionRawValue.dateSanction),
      motifSanction: new FormControl(sanctionRawValue.motifSanction),
      description: new FormControl(sanctionRawValue.description),
      codeActivite: new FormControl(sanctionRawValue.codeActivite),
      sanctionConfig: new FormControl(sanctionRawValue.sanctionConfig),
    });
  }

  getSanction(form: SanctionFormGroup): ISanction | NewSanction {
    return form.getRawValue() as ISanction | NewSanction;
  }

  resetForm(form: SanctionFormGroup, sanction: SanctionFormGroupInput): void {
    const sanctionRawValue = { ...this.getFormDefaults(), ...sanction };
    form.reset(
      {
        ...sanctionRawValue,
        id: { value: sanctionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SanctionFormDefaults {
    return {
      id: null,
    };
  }
}
