import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDecaisementBanque, NewDecaisementBanque } from '../decaisement-banque.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDecaisementBanque for edit and NewDecaisementBanqueFormGroupInput for create.
 */
type DecaisementBanqueFormGroupInput = IDecaisementBanque | PartialWithRequiredKeyOf<NewDecaisementBanque>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDecaisementBanque | NewDecaisementBanque> = Omit<T, 'dateDecaissement'> & {
  dateDecaissement?: string | null;
};

type DecaisementBanqueFormRawValue = FormValueOf<IDecaisementBanque>;

type NewDecaisementBanqueFormRawValue = FormValueOf<NewDecaisementBanque>;

type DecaisementBanqueFormDefaults = Pick<NewDecaisementBanque, 'id' | 'dateDecaissement'>;

type DecaisementBanqueFormGroupContent = {
  id: FormControl<DecaisementBanqueFormRawValue['id'] | NewDecaisementBanque['id']>;
  libelle: FormControl<DecaisementBanqueFormRawValue['libelle']>;
  montant: FormControl<DecaisementBanqueFormRawValue['montant']>;
  dateDecaissement: FormControl<DecaisementBanqueFormRawValue['dateDecaissement']>;
  montantDecaisse: FormControl<DecaisementBanqueFormRawValue['montantDecaisse']>;
  commentaire: FormControl<DecaisementBanqueFormRawValue['commentaire']>;
  compteBanque: FormControl<DecaisementBanqueFormRawValue['compteBanque']>;
};

export type DecaisementBanqueFormGroup = FormGroup<DecaisementBanqueFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DecaisementBanqueFormService {
  createDecaisementBanqueFormGroup(decaisementBanque: DecaisementBanqueFormGroupInput = { id: null }): DecaisementBanqueFormGroup {
    const decaisementBanqueRawValue = this.convertDecaisementBanqueToDecaisementBanqueRawValue({
      ...this.getFormDefaults(),
      ...decaisementBanque,
    });
    return new FormGroup<DecaisementBanqueFormGroupContent>({
      id: new FormControl(
        { value: decaisementBanqueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      libelle: new FormControl(decaisementBanqueRawValue.libelle),
      montant: new FormControl(decaisementBanqueRawValue.montant),
      dateDecaissement: new FormControl(decaisementBanqueRawValue.dateDecaissement),
      montantDecaisse: new FormControl(decaisementBanqueRawValue.montantDecaisse),
      commentaire: new FormControl(decaisementBanqueRawValue.commentaire),
      compteBanque: new FormControl(decaisementBanqueRawValue.compteBanque),
    });
  }

  getDecaisementBanque(form: DecaisementBanqueFormGroup): IDecaisementBanque | NewDecaisementBanque {
    return this.convertDecaisementBanqueRawValueToDecaisementBanque(
      form.getRawValue() as DecaisementBanqueFormRawValue | NewDecaisementBanqueFormRawValue
    );
  }

  resetForm(form: DecaisementBanqueFormGroup, decaisementBanque: DecaisementBanqueFormGroupInput): void {
    const decaisementBanqueRawValue = this.convertDecaisementBanqueToDecaisementBanqueRawValue({
      ...this.getFormDefaults(),
      ...decaisementBanque,
    });
    form.reset(
      {
        ...decaisementBanqueRawValue,
        id: { value: decaisementBanqueRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DecaisementBanqueFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDecaissement: currentTime,
    };
  }

  private convertDecaisementBanqueRawValueToDecaisementBanque(
    rawDecaisementBanque: DecaisementBanqueFormRawValue | NewDecaisementBanqueFormRawValue
  ): IDecaisementBanque | NewDecaisementBanque {
    return {
      ...rawDecaisementBanque,
      dateDecaissement: dayjs(rawDecaisementBanque.dateDecaissement, DATE_TIME_FORMAT),
    };
  }

  private convertDecaisementBanqueToDecaisementBanqueRawValue(
    decaisementBanque: IDecaisementBanque | (Partial<NewDecaisementBanque> & DecaisementBanqueFormDefaults)
  ): DecaisementBanqueFormRawValue | PartialWithRequiredKeyOf<NewDecaisementBanqueFormRawValue> {
    return {
      ...decaisementBanque,
      dateDecaissement: decaisementBanque.dateDecaissement ? decaisementBanque.dateDecaissement.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
