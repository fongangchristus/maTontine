import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPaiementSanction, NewPaiementSanction } from '../paiement-sanction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaiementSanction for edit and NewPaiementSanctionFormGroupInput for create.
 */
type PaiementSanctionFormGroupInput = IPaiementSanction | PartialWithRequiredKeyOf<NewPaiementSanction>;

type PaiementSanctionFormDefaults = Pick<NewPaiementSanction, 'id'>;

type PaiementSanctionFormGroupContent = {
  id: FormControl<IPaiementSanction['id'] | NewPaiementSanction['id']>;
  referencePaiement: FormControl<IPaiementSanction['referencePaiement']>;
  sanction: FormControl<IPaiementSanction['sanction']>;
};

export type PaiementSanctionFormGroup = FormGroup<PaiementSanctionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaiementSanctionFormService {
  createPaiementSanctionFormGroup(paiementSanction: PaiementSanctionFormGroupInput = { id: null }): PaiementSanctionFormGroup {
    const paiementSanctionRawValue = {
      ...this.getFormDefaults(),
      ...paiementSanction,
    };
    return new FormGroup<PaiementSanctionFormGroupContent>({
      id: new FormControl(
        { value: paiementSanctionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      referencePaiement: new FormControl(paiementSanctionRawValue.referencePaiement, {
        validators: [Validators.required],
      }),
      sanction: new FormControl(paiementSanctionRawValue.sanction),
    });
  }

  getPaiementSanction(form: PaiementSanctionFormGroup): IPaiementSanction | NewPaiementSanction {
    return form.getRawValue() as IPaiementSanction | NewPaiementSanction;
  }

  resetForm(form: PaiementSanctionFormGroup, paiementSanction: PaiementSanctionFormGroupInput): void {
    const paiementSanctionRawValue = { ...this.getFormDefaults(), ...paiementSanction };
    form.reset(
      {
        ...paiementSanctionRawValue,
        id: { value: paiementSanctionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PaiementSanctionFormDefaults {
    return {
      id: null,
    };
  }
}
