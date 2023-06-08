import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFonctionAdherent, NewFonctionAdherent } from '../fonction-adherent.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFonctionAdherent for edit and NewFonctionAdherentFormGroupInput for create.
 */
type FonctionAdherentFormGroupInput = IFonctionAdherent | PartialWithRequiredKeyOf<NewFonctionAdherent>;

type FonctionAdherentFormDefaults = Pick<NewFonctionAdherent, 'id' | 'actif'>;

type FonctionAdherentFormGroupContent = {
  id: FormControl<IFonctionAdherent['id'] | NewFonctionAdherent['id']>;
  matriculeAdherent: FormControl<IFonctionAdherent['matriculeAdherent']>;
  codeFonction: FormControl<IFonctionAdherent['codeFonction']>;
  datePriseFonction: FormControl<IFonctionAdherent['datePriseFonction']>;
  dateFinFonction: FormControl<IFonctionAdherent['dateFinFonction']>;
  actif: FormControl<IFonctionAdherent['actif']>;
  adherent: FormControl<IFonctionAdherent['adherent']>;
  fonction: FormControl<IFonctionAdherent['fonction']>;
};

export type FonctionAdherentFormGroup = FormGroup<FonctionAdherentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FonctionAdherentFormService {
  createFonctionAdherentFormGroup(fonctionAdherent: FonctionAdherentFormGroupInput = { id: null }): FonctionAdherentFormGroup {
    const fonctionAdherentRawValue = {
      ...this.getFormDefaults(),
      ...fonctionAdherent,
    };
    return new FormGroup<FonctionAdherentFormGroupContent>({
      id: new FormControl(
        { value: fonctionAdherentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      matriculeAdherent: new FormControl(fonctionAdherentRawValue.matriculeAdherent, {
        validators: [Validators.required],
      }),
      codeFonction: new FormControl(fonctionAdherentRawValue.codeFonction, {
        validators: [Validators.required],
      }),
      datePriseFonction: new FormControl(fonctionAdherentRawValue.datePriseFonction, {
        validators: [Validators.required],
      }),
      dateFinFonction: new FormControl(fonctionAdherentRawValue.dateFinFonction),
      actif: new FormControl(fonctionAdherentRawValue.actif, {
        validators: [Validators.required],
      }),
      adherent: new FormControl(fonctionAdherentRawValue.adherent),
      fonction: new FormControl(fonctionAdherentRawValue.fonction),
    });
  }

  getFonctionAdherent(form: FonctionAdherentFormGroup): IFonctionAdherent | NewFonctionAdherent {
    return form.getRawValue() as IFonctionAdherent | NewFonctionAdherent;
  }

  resetForm(form: FonctionAdherentFormGroup, fonctionAdherent: FonctionAdherentFormGroupInput): void {
    const fonctionAdherentRawValue = { ...this.getFormDefaults(), ...fonctionAdherent };
    form.reset(
      {
        ...fonctionAdherentRawValue,
        id: { value: fonctionAdherentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FonctionAdherentFormDefaults {
    return {
      id: null,
      actif: false,
    };
  }
}
