import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISessionTontine, NewSessionTontine } from '../session-tontine.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISessionTontine for edit and NewSessionTontineFormGroupInput for create.
 */
type SessionTontineFormGroupInput = ISessionTontine | PartialWithRequiredKeyOf<NewSessionTontine>;

type SessionTontineFormDefaults = Pick<NewSessionTontine, 'id'>;

type SessionTontineFormGroupContent = {
  id: FormControl<ISessionTontine['id'] | NewSessionTontine['id']>;
  libelle: FormControl<ISessionTontine['libelle']>;
  dateDebut: FormControl<ISessionTontine['dateDebut']>;
  dateFin: FormControl<ISessionTontine['dateFin']>;
  tontine: FormControl<ISessionTontine['tontine']>;
};

export type SessionTontineFormGroup = FormGroup<SessionTontineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SessionTontineFormService {
  createSessionTontineFormGroup(sessionTontine: SessionTontineFormGroupInput = { id: null }): SessionTontineFormGroup {
    const sessionTontineRawValue = {
      ...this.getFormDefaults(),
      ...sessionTontine,
    };
    return new FormGroup<SessionTontineFormGroupContent>({
      id: new FormControl(
        { value: sessionTontineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      libelle: new FormControl(sessionTontineRawValue.libelle),
      dateDebut: new FormControl(sessionTontineRawValue.dateDebut),
      dateFin: new FormControl(sessionTontineRawValue.dateFin),
      tontine: new FormControl(sessionTontineRawValue.tontine),
    });
  }

  getSessionTontine(form: SessionTontineFormGroup): ISessionTontine | NewSessionTontine {
    return form.getRawValue() as ISessionTontine | NewSessionTontine;
  }

  resetForm(form: SessionTontineFormGroup, sessionTontine: SessionTontineFormGroupInput): void {
    const sessionTontineRawValue = { ...this.getFormDefaults(), ...sessionTontine };
    form.reset(
      {
        ...sessionTontineRawValue,
        id: { value: sessionTontineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SessionTontineFormDefaults {
    return {
      id: null,
    };
  }
}
