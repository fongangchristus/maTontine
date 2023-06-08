import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEvenement, NewEvenement } from '../evenement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvenement for edit and NewEvenementFormGroupInput for create.
 */
type EvenementFormGroupInput = IEvenement | PartialWithRequiredKeyOf<NewEvenement>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEvenement | NewEvenement> = Omit<T, 'dateEvenement'> & {
  dateEvenement?: string | null;
};

type EvenementFormRawValue = FormValueOf<IEvenement>;

type NewEvenementFormRawValue = FormValueOf<NewEvenement>;

type EvenementFormDefaults = Pick<NewEvenement, 'id' | 'dateEvenement'>;

type EvenementFormGroupContent = {
  id: FormControl<EvenementFormRawValue['id'] | NewEvenement['id']>;
  libele: FormControl<EvenementFormRawValue['libele']>;
  codepot: FormControl<EvenementFormRawValue['codepot']>;
  montantPayer: FormControl<EvenementFormRawValue['montantPayer']>;
  description: FormControl<EvenementFormRawValue['description']>;
  budget: FormControl<EvenementFormRawValue['budget']>;
  dateEvenement: FormControl<EvenementFormRawValue['dateEvenement']>;
  typeEvenement: FormControl<EvenementFormRawValue['typeEvenement']>;
};

export type EvenementFormGroup = FormGroup<EvenementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EvenementFormService {
  createEvenementFormGroup(evenement: EvenementFormGroupInput = { id: null }): EvenementFormGroup {
    const evenementRawValue = this.convertEvenementToEvenementRawValue({
      ...this.getFormDefaults(),
      ...evenement,
    });
    return new FormGroup<EvenementFormGroupContent>({
      id: new FormControl(
        { value: evenementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      libele: new FormControl(evenementRawValue.libele),
      codepot: new FormControl(evenementRawValue.codepot),
      montantPayer: new FormControl(evenementRawValue.montantPayer),
      description: new FormControl(evenementRawValue.description),
      budget: new FormControl(evenementRawValue.budget),
      dateEvenement: new FormControl(evenementRawValue.dateEvenement),
      typeEvenement: new FormControl(evenementRawValue.typeEvenement),
    });
  }

  getEvenement(form: EvenementFormGroup): IEvenement | NewEvenement {
    return this.convertEvenementRawValueToEvenement(form.getRawValue() as EvenementFormRawValue | NewEvenementFormRawValue);
  }

  resetForm(form: EvenementFormGroup, evenement: EvenementFormGroupInput): void {
    const evenementRawValue = this.convertEvenementToEvenementRawValue({ ...this.getFormDefaults(), ...evenement });
    form.reset(
      {
        ...evenementRawValue,
        id: { value: evenementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EvenementFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateEvenement: currentTime,
    };
  }

  private convertEvenementRawValueToEvenement(rawEvenement: EvenementFormRawValue | NewEvenementFormRawValue): IEvenement | NewEvenement {
    return {
      ...rawEvenement,
      dateEvenement: dayjs(rawEvenement.dateEvenement, DATE_TIME_FORMAT),
    };
  }

  private convertEvenementToEvenementRawValue(
    evenement: IEvenement | (Partial<NewEvenement> & EvenementFormDefaults)
  ): EvenementFormRawValue | PartialWithRequiredKeyOf<NewEvenementFormRawValue> {
    return {
      ...evenement,
      dateEvenement: evenement.dateEvenement ? evenement.dateEvenement.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
