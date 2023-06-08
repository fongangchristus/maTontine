import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPaiementTontine, NewPaiementTontine } from '../paiement-tontine.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaiementTontine for edit and NewPaiementTontineFormGroupInput for create.
 */
type PaiementTontineFormGroupInput = IPaiementTontine | PartialWithRequiredKeyOf<NewPaiementTontine>;

type PaiementTontineFormDefaults = Pick<NewPaiementTontine, 'id'>;

type PaiementTontineFormGroupContent = {
  id: FormControl<IPaiementTontine['id'] | NewPaiementTontine['id']>;
  referencePaiement: FormControl<IPaiementTontine['referencePaiement']>;
  cotisationTontine: FormControl<IPaiementTontine['cotisationTontine']>;
  decaissementTontine: FormControl<IPaiementTontine['decaissementTontine']>;
};

export type PaiementTontineFormGroup = FormGroup<PaiementTontineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaiementTontineFormService {
  createPaiementTontineFormGroup(paiementTontine: PaiementTontineFormGroupInput = { id: null }): PaiementTontineFormGroup {
    const paiementTontineRawValue = {
      ...this.getFormDefaults(),
      ...paiementTontine,
    };
    return new FormGroup<PaiementTontineFormGroupContent>({
      id: new FormControl(
        { value: paiementTontineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      referencePaiement: new FormControl(paiementTontineRawValue.referencePaiement),
      cotisationTontine: new FormControl(paiementTontineRawValue.cotisationTontine),
      decaissementTontine: new FormControl(paiementTontineRawValue.decaissementTontine),
    });
  }

  getPaiementTontine(form: PaiementTontineFormGroup): IPaiementTontine | NewPaiementTontine {
    return form.getRawValue() as IPaiementTontine | NewPaiementTontine;
  }

  resetForm(form: PaiementTontineFormGroup, paiementTontine: PaiementTontineFormGroupInput): void {
    const paiementTontineRawValue = { ...this.getFormDefaults(), ...paiementTontine };
    form.reset(
      {
        ...paiementTontineRawValue,
        id: { value: paiementTontineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PaiementTontineFormDefaults {
    return {
      id: null,
    };
  }
}
