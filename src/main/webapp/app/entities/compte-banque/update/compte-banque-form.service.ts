import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICompteBanque, NewCompteBanque } from '../compte-banque.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompteBanque for edit and NewCompteBanqueFormGroupInput for create.
 */
type CompteBanqueFormGroupInput = ICompteBanque | PartialWithRequiredKeyOf<NewCompteBanque>;

type CompteBanqueFormDefaults = Pick<NewCompteBanque, 'id'>;

type CompteBanqueFormGroupContent = {
  id: FormControl<ICompteBanque['id'] | NewCompteBanque['id']>;
  libelle: FormControl<ICompteBanque['libelle']>;
  description: FormControl<ICompteBanque['description']>;
  matriculeAdherant: FormControl<ICompteBanque['matriculeAdherant']>;
  montantDisponnible: FormControl<ICompteBanque['montantDisponnible']>;
  banque: FormControl<ICompteBanque['banque']>;
};

export type CompteBanqueFormGroup = FormGroup<CompteBanqueFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompteBanqueFormService {
  createCompteBanqueFormGroup(compteBanque: CompteBanqueFormGroupInput = { id: null }): CompteBanqueFormGroup {
    const compteBanqueRawValue = {
      ...this.getFormDefaults(),
      ...compteBanque,
    };
    return new FormGroup<CompteBanqueFormGroupContent>({
      id: new FormControl(
        { value: compteBanqueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      libelle: new FormControl(compteBanqueRawValue.libelle),
      description: new FormControl(compteBanqueRawValue.description),
      matriculeAdherant: new FormControl(compteBanqueRawValue.matriculeAdherant),
      montantDisponnible: new FormControl(compteBanqueRawValue.montantDisponnible),
      banque: new FormControl(compteBanqueRawValue.banque),
    });
  }

  getCompteBanque(form: CompteBanqueFormGroup): ICompteBanque | NewCompteBanque {
    return form.getRawValue() as ICompteBanque | NewCompteBanque;
  }

  resetForm(form: CompteBanqueFormGroup, compteBanque: CompteBanqueFormGroupInput): void {
    const compteBanqueRawValue = { ...this.getFormDefaults(), ...compteBanque };
    form.reset(
      {
        ...compteBanqueRawValue,
        id: { value: compteBanqueRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CompteBanqueFormDefaults {
    return {
      id: null,
    };
  }
}
