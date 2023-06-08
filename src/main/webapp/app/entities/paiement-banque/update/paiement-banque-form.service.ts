import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPaiementBanque, NewPaiementBanque } from '../paiement-banque.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaiementBanque for edit and NewPaiementBanqueFormGroupInput for create.
 */
type PaiementBanqueFormGroupInput = IPaiementBanque | PartialWithRequiredKeyOf<NewPaiementBanque>;

type PaiementBanqueFormDefaults = Pick<NewPaiementBanque, 'id'>;

type PaiementBanqueFormGroupContent = {
  id: FormControl<IPaiementBanque['id'] | NewPaiementBanque['id']>;
  referencePaiement: FormControl<IPaiementBanque['referencePaiement']>;
};

export type PaiementBanqueFormGroup = FormGroup<PaiementBanqueFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaiementBanqueFormService {
  createPaiementBanqueFormGroup(paiementBanque: PaiementBanqueFormGroupInput = { id: null }): PaiementBanqueFormGroup {
    const paiementBanqueRawValue = {
      ...this.getFormDefaults(),
      ...paiementBanque,
    };
    return new FormGroup<PaiementBanqueFormGroupContent>({
      id: new FormControl(
        { value: paiementBanqueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      referencePaiement: new FormControl(paiementBanqueRawValue.referencePaiement, {
        validators: [Validators.required],
      }),
    });
  }

  getPaiementBanque(form: PaiementBanqueFormGroup): IPaiementBanque | NewPaiementBanque {
    return form.getRawValue() as IPaiementBanque | NewPaiementBanque;
  }

  resetForm(form: PaiementBanqueFormGroup, paiementBanque: PaiementBanqueFormGroupInput): void {
    const paiementBanqueRawValue = { ...this.getFormDefaults(), ...paiementBanque };
    form.reset(
      {
        ...paiementBanqueRawValue,
        id: { value: paiementBanqueRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PaiementBanqueFormDefaults {
    return {
      id: null,
    };
  }
}
