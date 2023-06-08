import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICompteTontine, NewCompteTontine } from '../compte-tontine.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompteTontine for edit and NewCompteTontineFormGroupInput for create.
 */
type CompteTontineFormGroupInput = ICompteTontine | PartialWithRequiredKeyOf<NewCompteTontine>;

type CompteTontineFormDefaults = Pick<NewCompteTontine, 'id' | 'etatDeCompte'>;

type CompteTontineFormGroupContent = {
  id: FormControl<ICompteTontine['id'] | NewCompteTontine['id']>;
  etatDeCompte: FormControl<ICompteTontine['etatDeCompte']>;
  libele: FormControl<ICompteTontine['libele']>;
  odreBeneficiere: FormControl<ICompteTontine['odreBeneficiere']>;
  matriculeCompte: FormControl<ICompteTontine['matriculeCompte']>;
  matriculeMenbre: FormControl<ICompteTontine['matriculeMenbre']>;
  tontine: FormControl<ICompteTontine['tontine']>;
};

export type CompteTontineFormGroup = FormGroup<CompteTontineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompteTontineFormService {
  createCompteTontineFormGroup(compteTontine: CompteTontineFormGroupInput = { id: null }): CompteTontineFormGroup {
    const compteTontineRawValue = {
      ...this.getFormDefaults(),
      ...compteTontine,
    };
    return new FormGroup<CompteTontineFormGroupContent>({
      id: new FormControl(
        { value: compteTontineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      etatDeCompte: new FormControl(compteTontineRawValue.etatDeCompte),
      libele: new FormControl(compteTontineRawValue.libele),
      odreBeneficiere: new FormControl(compteTontineRawValue.odreBeneficiere),
      matriculeCompte: new FormControl(compteTontineRawValue.matriculeCompte, {
        validators: [Validators.required],
      }),
      matriculeMenbre: new FormControl(compteTontineRawValue.matriculeMenbre, {
        validators: [Validators.required],
      }),
      tontine: new FormControl(compteTontineRawValue.tontine),
    });
  }

  getCompteTontine(form: CompteTontineFormGroup): ICompteTontine | NewCompteTontine {
    return form.getRawValue() as ICompteTontine | NewCompteTontine;
  }

  resetForm(form: CompteTontineFormGroup, compteTontine: CompteTontineFormGroupInput): void {
    const compteTontineRawValue = { ...this.getFormDefaults(), ...compteTontine };
    form.reset(
      {
        ...compteTontineRawValue,
        id: { value: compteTontineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CompteTontineFormDefaults {
    return {
      id: null,
      etatDeCompte: false,
    };
  }
}
