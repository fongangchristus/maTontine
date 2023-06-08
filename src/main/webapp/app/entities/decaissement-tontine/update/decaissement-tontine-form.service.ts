import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDecaissementTontine, NewDecaissementTontine } from '../decaissement-tontine.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDecaissementTontine for edit and NewDecaissementTontineFormGroupInput for create.
 */
type DecaissementTontineFormGroupInput = IDecaissementTontine | PartialWithRequiredKeyOf<NewDecaissementTontine>;

type DecaissementTontineFormDefaults = Pick<NewDecaissementTontine, 'id'>;

type DecaissementTontineFormGroupContent = {
  id: FormControl<IDecaissementTontine['id'] | NewDecaissementTontine['id']>;
  libele: FormControl<IDecaissementTontine['libele']>;
  dateDecaissement: FormControl<IDecaissementTontine['dateDecaissement']>;
  montantDecaisse: FormControl<IDecaissementTontine['montantDecaisse']>;
  commentaire: FormControl<IDecaissementTontine['commentaire']>;
  tontine: FormControl<IDecaissementTontine['tontine']>;
  compteTontine: FormControl<IDecaissementTontine['compteTontine']>;
};

export type DecaissementTontineFormGroup = FormGroup<DecaissementTontineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DecaissementTontineFormService {
  createDecaissementTontineFormGroup(decaissementTontine: DecaissementTontineFormGroupInput = { id: null }): DecaissementTontineFormGroup {
    const decaissementTontineRawValue = {
      ...this.getFormDefaults(),
      ...decaissementTontine,
    };
    return new FormGroup<DecaissementTontineFormGroupContent>({
      id: new FormControl(
        { value: decaissementTontineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      libele: new FormControl(decaissementTontineRawValue.libele),
      dateDecaissement: new FormControl(decaissementTontineRawValue.dateDecaissement),
      montantDecaisse: new FormControl(decaissementTontineRawValue.montantDecaisse),
      commentaire: new FormControl(decaissementTontineRawValue.commentaire),
      tontine: new FormControl(decaissementTontineRawValue.tontine),
      compteTontine: new FormControl(decaissementTontineRawValue.compteTontine),
    });
  }

  getDecaissementTontine(form: DecaissementTontineFormGroup): IDecaissementTontine | NewDecaissementTontine {
    return form.getRawValue() as IDecaissementTontine | NewDecaissementTontine;
  }

  resetForm(form: DecaissementTontineFormGroup, decaissementTontine: DecaissementTontineFormGroupInput): void {
    const decaissementTontineRawValue = { ...this.getFormDefaults(), ...decaissementTontine };
    form.reset(
      {
        ...decaissementTontineRawValue,
        id: { value: decaissementTontineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DecaissementTontineFormDefaults {
    return {
      id: null,
    };
  }
}
