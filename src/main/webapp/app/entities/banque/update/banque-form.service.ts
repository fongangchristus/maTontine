import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBanque, NewBanque } from '../banque.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBanque for edit and NewBanqueFormGroupInput for create.
 */
type BanqueFormGroupInput = IBanque | PartialWithRequiredKeyOf<NewBanque>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBanque | NewBanque> = Omit<T, 'dateOuverture' | 'dateCloture'> & {
  dateOuverture?: string | null;
  dateCloture?: string | null;
};

type BanqueFormRawValue = FormValueOf<IBanque>;

type NewBanqueFormRawValue = FormValueOf<NewBanque>;

type BanqueFormDefaults = Pick<NewBanque, 'id' | 'dateOuverture' | 'dateCloture'>;

type BanqueFormGroupContent = {
  id: FormControl<BanqueFormRawValue['id'] | NewBanque['id']>;
  codeAssociation: FormControl<BanqueFormRawValue['codeAssociation']>;
  libelle: FormControl<BanqueFormRawValue['libelle']>;
  description: FormControl<BanqueFormRawValue['description']>;
  dateOuverture: FormControl<BanqueFormRawValue['dateOuverture']>;
  dateCloture: FormControl<BanqueFormRawValue['dateCloture']>;
  penaliteRetardRnbrsmnt: FormControl<BanqueFormRawValue['penaliteRetardRnbrsmnt']>;
  tauxInteretPret: FormControl<BanqueFormRawValue['tauxInteretPret']>;
};

export type BanqueFormGroup = FormGroup<BanqueFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BanqueFormService {
  createBanqueFormGroup(banque: BanqueFormGroupInput = { id: null }): BanqueFormGroup {
    const banqueRawValue = this.convertBanqueToBanqueRawValue({
      ...this.getFormDefaults(),
      ...banque,
    });
    return new FormGroup<BanqueFormGroupContent>({
      id: new FormControl(
        { value: banqueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      codeAssociation: new FormControl(banqueRawValue.codeAssociation, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(banqueRawValue.libelle),
      description: new FormControl(banqueRawValue.description),
      dateOuverture: new FormControl(banqueRawValue.dateOuverture),
      dateCloture: new FormControl(banqueRawValue.dateCloture),
      penaliteRetardRnbrsmnt: new FormControl(banqueRawValue.penaliteRetardRnbrsmnt),
      tauxInteretPret: new FormControl(banqueRawValue.tauxInteretPret),
    });
  }

  getBanque(form: BanqueFormGroup): IBanque | NewBanque {
    return this.convertBanqueRawValueToBanque(form.getRawValue() as BanqueFormRawValue | NewBanqueFormRawValue);
  }

  resetForm(form: BanqueFormGroup, banque: BanqueFormGroupInput): void {
    const banqueRawValue = this.convertBanqueToBanqueRawValue({ ...this.getFormDefaults(), ...banque });
    form.reset(
      {
        ...banqueRawValue,
        id: { value: banqueRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BanqueFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateOuverture: currentTime,
      dateCloture: currentTime,
    };
  }

  private convertBanqueRawValueToBanque(rawBanque: BanqueFormRawValue | NewBanqueFormRawValue): IBanque | NewBanque {
    return {
      ...rawBanque,
      dateOuverture: dayjs(rawBanque.dateOuverture, DATE_TIME_FORMAT),
      dateCloture: dayjs(rawBanque.dateCloture, DATE_TIME_FORMAT),
    };
  }

  private convertBanqueToBanqueRawValue(
    banque: IBanque | (Partial<NewBanque> & BanqueFormDefaults)
  ): BanqueFormRawValue | PartialWithRequiredKeyOf<NewBanqueFormRawValue> {
    return {
      ...banque,
      dateOuverture: banque.dateOuverture ? banque.dateOuverture.format(DATE_TIME_FORMAT) : undefined,
      dateCloture: banque.dateCloture ? banque.dateCloture.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
