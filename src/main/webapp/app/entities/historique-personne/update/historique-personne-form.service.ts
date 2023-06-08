import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHistoriquePersonne, NewHistoriquePersonne } from '../historique-personne.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHistoriquePersonne for edit and NewHistoriquePersonneFormGroupInput for create.
 */
type HistoriquePersonneFormGroupInput = IHistoriquePersonne | PartialWithRequiredKeyOf<NewHistoriquePersonne>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IHistoriquePersonne | NewHistoriquePersonne> = Omit<T, 'dateAction'> & {
  dateAction?: string | null;
};

type HistoriquePersonneFormRawValue = FormValueOf<IHistoriquePersonne>;

type NewHistoriquePersonneFormRawValue = FormValueOf<NewHistoriquePersonne>;

type HistoriquePersonneFormDefaults = Pick<NewHistoriquePersonne, 'id' | 'dateAction'>;

type HistoriquePersonneFormGroupContent = {
  id: FormControl<HistoriquePersonneFormRawValue['id'] | NewHistoriquePersonne['id']>;
  dateAction: FormControl<HistoriquePersonneFormRawValue['dateAction']>;
  matriculePersonne: FormControl<HistoriquePersonneFormRawValue['matriculePersonne']>;
  action: FormControl<HistoriquePersonneFormRawValue['action']>;
  result: FormControl<HistoriquePersonneFormRawValue['result']>;
  description: FormControl<HistoriquePersonneFormRawValue['description']>;
  personne: FormControl<HistoriquePersonneFormRawValue['personne']>;
};

export type HistoriquePersonneFormGroup = FormGroup<HistoriquePersonneFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HistoriquePersonneFormService {
  createHistoriquePersonneFormGroup(historiquePersonne: HistoriquePersonneFormGroupInput = { id: null }): HistoriquePersonneFormGroup {
    const historiquePersonneRawValue = this.convertHistoriquePersonneToHistoriquePersonneRawValue({
      ...this.getFormDefaults(),
      ...historiquePersonne,
    });
    return new FormGroup<HistoriquePersonneFormGroupContent>({
      id: new FormControl(
        { value: historiquePersonneRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dateAction: new FormControl(historiquePersonneRawValue.dateAction),
      matriculePersonne: new FormControl(historiquePersonneRawValue.matriculePersonne, {
        validators: [Validators.required],
      }),
      action: new FormControl(historiquePersonneRawValue.action),
      result: new FormControl(historiquePersonneRawValue.result),
      description: new FormControl(historiquePersonneRawValue.description),
      personne: new FormControl(historiquePersonneRawValue.personne),
    });
  }

  getHistoriquePersonne(form: HistoriquePersonneFormGroup): IHistoriquePersonne | NewHistoriquePersonne {
    return this.convertHistoriquePersonneRawValueToHistoriquePersonne(
      form.getRawValue() as HistoriquePersonneFormRawValue | NewHistoriquePersonneFormRawValue
    );
  }

  resetForm(form: HistoriquePersonneFormGroup, historiquePersonne: HistoriquePersonneFormGroupInput): void {
    const historiquePersonneRawValue = this.convertHistoriquePersonneToHistoriquePersonneRawValue({
      ...this.getFormDefaults(),
      ...historiquePersonne,
    });
    form.reset(
      {
        ...historiquePersonneRawValue,
        id: { value: historiquePersonneRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): HistoriquePersonneFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateAction: currentTime,
    };
  }

  private convertHistoriquePersonneRawValueToHistoriquePersonne(
    rawHistoriquePersonne: HistoriquePersonneFormRawValue | NewHistoriquePersonneFormRawValue
  ): IHistoriquePersonne | NewHistoriquePersonne {
    return {
      ...rawHistoriquePersonne,
      dateAction: dayjs(rawHistoriquePersonne.dateAction, DATE_TIME_FORMAT),
    };
  }

  private convertHistoriquePersonneToHistoriquePersonneRawValue(
    historiquePersonne: IHistoriquePersonne | (Partial<NewHistoriquePersonne> & HistoriquePersonneFormDefaults)
  ): HistoriquePersonneFormRawValue | PartialWithRequiredKeyOf<NewHistoriquePersonneFormRawValue> {
    return {
      ...historiquePersonne,
      dateAction: historiquePersonne.dateAction ? historiquePersonne.dateAction.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
