import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPot, NewPot } from '../pot.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPot for edit and NewPotFormGroupInput for create.
 */
type PotFormGroupInput = IPot | PartialWithRequiredKeyOf<NewPot>;

type PotFormDefaults = Pick<NewPot, 'id'>;

type PotFormGroupContent = {
  id: FormControl<IPot['id'] | NewPot['id']>;
  libele: FormControl<IPot['libele']>;
  codepot: FormControl<IPot['codepot']>;
  montantCible: FormControl<IPot['montantCible']>;
  description: FormControl<IPot['description']>;
  dateDebutCollecte: FormControl<IPot['dateDebutCollecte']>;
  dateFinCollecte: FormControl<IPot['dateFinCollecte']>;
  statut: FormControl<IPot['statut']>;
  typePot: FormControl<IPot['typePot']>;
};

export type PotFormGroup = FormGroup<PotFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PotFormService {
  createPotFormGroup(pot: PotFormGroupInput = { id: null }): PotFormGroup {
    const potRawValue = {
      ...this.getFormDefaults(),
      ...pot,
    };
    return new FormGroup<PotFormGroupContent>({
      id: new FormControl(
        { value: potRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      libele: new FormControl(potRawValue.libele),
      codepot: new FormControl(potRawValue.codepot, {
        validators: [Validators.required],
      }),
      montantCible: new FormControl(potRawValue.montantCible),
      description: new FormControl(potRawValue.description),
      dateDebutCollecte: new FormControl(potRawValue.dateDebutCollecte),
      dateFinCollecte: new FormControl(potRawValue.dateFinCollecte),
      statut: new FormControl(potRawValue.statut),
      typePot: new FormControl(potRawValue.typePot),
    });
  }

  getPot(form: PotFormGroup): IPot | NewPot {
    return form.getRawValue() as IPot | NewPot;
  }

  resetForm(form: PotFormGroup, pot: PotFormGroupInput): void {
    const potRawValue = { ...this.getFormDefaults(), ...pot };
    form.reset(
      {
        ...potRawValue,
        id: { value: potRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PotFormDefaults {
    return {
      id: null,
    };
  }
}
