import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICotisationBanque, NewCotisationBanque } from '../cotisation-banque.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICotisationBanque for edit and NewCotisationBanqueFormGroupInput for create.
 */
type CotisationBanqueFormGroupInput = ICotisationBanque | PartialWithRequiredKeyOf<NewCotisationBanque>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICotisationBanque | NewCotisationBanque> = Omit<T, 'dateCotisation'> & {
  dateCotisation?: string | null;
};

type CotisationBanqueFormRawValue = FormValueOf<ICotisationBanque>;

type NewCotisationBanqueFormRawValue = FormValueOf<NewCotisationBanque>;

type CotisationBanqueFormDefaults = Pick<NewCotisationBanque, 'id' | 'dateCotisation'>;

type CotisationBanqueFormGroupContent = {
  id: FormControl<CotisationBanqueFormRawValue['id'] | NewCotisationBanque['id']>;
  libelle: FormControl<CotisationBanqueFormRawValue['libelle']>;
  montant: FormControl<CotisationBanqueFormRawValue['montant']>;
  dateCotisation: FormControl<CotisationBanqueFormRawValue['dateCotisation']>;
  montantCotise: FormControl<CotisationBanqueFormRawValue['montantCotise']>;
  commentaire: FormControl<CotisationBanqueFormRawValue['commentaire']>;
  compteBanque: FormControl<CotisationBanqueFormRawValue['compteBanque']>;
};

export type CotisationBanqueFormGroup = FormGroup<CotisationBanqueFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CotisationBanqueFormService {
  createCotisationBanqueFormGroup(cotisationBanque: CotisationBanqueFormGroupInput = { id: null }): CotisationBanqueFormGroup {
    const cotisationBanqueRawValue = this.convertCotisationBanqueToCotisationBanqueRawValue({
      ...this.getFormDefaults(),
      ...cotisationBanque,
    });
    return new FormGroup<CotisationBanqueFormGroupContent>({
      id: new FormControl(
        { value: cotisationBanqueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      libelle: new FormControl(cotisationBanqueRawValue.libelle),
      montant: new FormControl(cotisationBanqueRawValue.montant),
      dateCotisation: new FormControl(cotisationBanqueRawValue.dateCotisation),
      montantCotise: new FormControl(cotisationBanqueRawValue.montantCotise),
      commentaire: new FormControl(cotisationBanqueRawValue.commentaire),
      compteBanque: new FormControl(cotisationBanqueRawValue.compteBanque),
    });
  }

  getCotisationBanque(form: CotisationBanqueFormGroup): ICotisationBanque | NewCotisationBanque {
    return this.convertCotisationBanqueRawValueToCotisationBanque(
      form.getRawValue() as CotisationBanqueFormRawValue | NewCotisationBanqueFormRawValue
    );
  }

  resetForm(form: CotisationBanqueFormGroup, cotisationBanque: CotisationBanqueFormGroupInput): void {
    const cotisationBanqueRawValue = this.convertCotisationBanqueToCotisationBanqueRawValue({
      ...this.getFormDefaults(),
      ...cotisationBanque,
    });
    form.reset(
      {
        ...cotisationBanqueRawValue,
        id: { value: cotisationBanqueRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CotisationBanqueFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCotisation: currentTime,
    };
  }

  private convertCotisationBanqueRawValueToCotisationBanque(
    rawCotisationBanque: CotisationBanqueFormRawValue | NewCotisationBanqueFormRawValue
  ): ICotisationBanque | NewCotisationBanque {
    return {
      ...rawCotisationBanque,
      dateCotisation: dayjs(rawCotisationBanque.dateCotisation, DATE_TIME_FORMAT),
    };
  }

  private convertCotisationBanqueToCotisationBanqueRawValue(
    cotisationBanque: ICotisationBanque | (Partial<NewCotisationBanque> & CotisationBanqueFormDefaults)
  ): CotisationBanqueFormRawValue | PartialWithRequiredKeyOf<NewCotisationBanqueFormRawValue> {
    return {
      ...cotisationBanque,
      dateCotisation: cotisationBanque.dateCotisation ? cotisationBanque.dateCotisation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
