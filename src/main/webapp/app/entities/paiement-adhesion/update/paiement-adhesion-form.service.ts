import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPaiementAdhesion, NewPaiementAdhesion } from '../paiement-adhesion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaiementAdhesion for edit and NewPaiementAdhesionFormGroupInput for create.
 */
type PaiementAdhesionFormGroupInput = IPaiementAdhesion | PartialWithRequiredKeyOf<NewPaiementAdhesion>;

type PaiementAdhesionFormDefaults = Pick<NewPaiementAdhesion, 'id'>;

type PaiementAdhesionFormGroupContent = {
  id: FormControl<IPaiementAdhesion['id'] | NewPaiementAdhesion['id']>;
  referencePaiement: FormControl<IPaiementAdhesion['referencePaiement']>;
  adhesion: FormControl<IPaiementAdhesion['adhesion']>;
};

export type PaiementAdhesionFormGroup = FormGroup<PaiementAdhesionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaiementAdhesionFormService {
  createPaiementAdhesionFormGroup(paiementAdhesion: PaiementAdhesionFormGroupInput = { id: null }): PaiementAdhesionFormGroup {
    const paiementAdhesionRawValue = {
      ...this.getFormDefaults(),
      ...paiementAdhesion,
    };
    return new FormGroup<PaiementAdhesionFormGroupContent>({
      id: new FormControl(
        { value: paiementAdhesionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      referencePaiement: new FormControl(paiementAdhesionRawValue.referencePaiement, {
        validators: [Validators.required],
      }),
      adhesion: new FormControl(paiementAdhesionRawValue.adhesion),
    });
  }

  getPaiementAdhesion(form: PaiementAdhesionFormGroup): IPaiementAdhesion | NewPaiementAdhesion {
    return form.getRawValue() as IPaiementAdhesion | NewPaiementAdhesion;
  }

  resetForm(form: PaiementAdhesionFormGroup, paiementAdhesion: PaiementAdhesionFormGroupInput): void {
    const paiementAdhesionRawValue = { ...this.getFormDefaults(), ...paiementAdhesion };
    form.reset(
      {
        ...paiementAdhesionRawValue,
        id: { value: paiementAdhesionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PaiementAdhesionFormDefaults {
    return {
      id: null,
    };
  }
}
