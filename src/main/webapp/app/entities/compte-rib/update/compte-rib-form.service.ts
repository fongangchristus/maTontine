import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICompteRIB, NewCompteRIB } from '../compte-rib.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompteRIB for edit and NewCompteRIBFormGroupInput for create.
 */
type CompteRIBFormGroupInput = ICompteRIB | PartialWithRequiredKeyOf<NewCompteRIB>;

type CompteRIBFormDefaults = Pick<NewCompteRIB, 'id' | 'verifier'>;

type CompteRIBFormGroupContent = {
  id: FormControl<ICompteRIB['id'] | NewCompteRIB['id']>;
  iban: FormControl<ICompteRIB['iban']>;
  titulaireCompte: FormControl<ICompteRIB['titulaireCompte']>;
  verifier: FormControl<ICompteRIB['verifier']>;
  adherent: FormControl<ICompteRIB['adherent']>;
};

export type CompteRIBFormGroup = FormGroup<CompteRIBFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompteRIBFormService {
  createCompteRIBFormGroup(compteRIB: CompteRIBFormGroupInput = { id: null }): CompteRIBFormGroup {
    const compteRIBRawValue = {
      ...this.getFormDefaults(),
      ...compteRIB,
    };
    return new FormGroup<CompteRIBFormGroupContent>({
      id: new FormControl(
        { value: compteRIBRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      iban: new FormControl(compteRIBRawValue.iban),
      titulaireCompte: new FormControl(compteRIBRawValue.titulaireCompte),
      verifier: new FormControl(compteRIBRawValue.verifier),
      adherent: new FormControl(compteRIBRawValue.adherent),
    });
  }

  getCompteRIB(form: CompteRIBFormGroup): ICompteRIB | NewCompteRIB {
    return form.getRawValue() as ICompteRIB | NewCompteRIB;
  }

  resetForm(form: CompteRIBFormGroup, compteRIB: CompteRIBFormGroupInput): void {
    const compteRIBRawValue = { ...this.getFormDefaults(), ...compteRIB };
    form.reset(
      {
        ...compteRIBRawValue,
        id: { value: compteRIBRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CompteRIBFormDefaults {
    return {
      id: null,
      verifier: false,
    };
  }
}
