import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFormuleAdhesion, NewFormuleAdhesion } from '../formule-adhesion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFormuleAdhesion for edit and NewFormuleAdhesionFormGroupInput for create.
 */
type FormuleAdhesionFormGroupInput = IFormuleAdhesion | PartialWithRequiredKeyOf<NewFormuleAdhesion>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFormuleAdhesion | NewFormuleAdhesion> = Omit<T, 'dateDebut'> & {
  dateDebut?: string | null;
};

type FormuleAdhesionFormRawValue = FormValueOf<IFormuleAdhesion>;

type NewFormuleAdhesionFormRawValue = FormValueOf<NewFormuleAdhesion>;

type FormuleAdhesionFormDefaults = Pick<NewFormuleAdhesion, 'id' | 'adhesionPeriodique' | 'dateDebut' | 'montantLibre'>;

type FormuleAdhesionFormGroupContent = {
  id: FormControl<FormuleAdhesionFormRawValue['id'] | NewFormuleAdhesion['id']>;
  adhesionPeriodique: FormControl<FormuleAdhesionFormRawValue['adhesionPeriodique']>;
  dateDebut: FormControl<FormuleAdhesionFormRawValue['dateDebut']>;
  dureeAdhesionMois: FormControl<FormuleAdhesionFormRawValue['dureeAdhesionMois']>;
  montantLibre: FormControl<FormuleAdhesionFormRawValue['montantLibre']>;
  description: FormControl<FormuleAdhesionFormRawValue['description']>;
  tarif: FormControl<FormuleAdhesionFormRawValue['tarif']>;
  adhesion: FormControl<FormuleAdhesionFormRawValue['adhesion']>;
};

export type FormuleAdhesionFormGroup = FormGroup<FormuleAdhesionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FormuleAdhesionFormService {
  createFormuleAdhesionFormGroup(formuleAdhesion: FormuleAdhesionFormGroupInput = { id: null }): FormuleAdhesionFormGroup {
    const formuleAdhesionRawValue = this.convertFormuleAdhesionToFormuleAdhesionRawValue({
      ...this.getFormDefaults(),
      ...formuleAdhesion,
    });
    return new FormGroup<FormuleAdhesionFormGroupContent>({
      id: new FormControl(
        { value: formuleAdhesionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      adhesionPeriodique: new FormControl(formuleAdhesionRawValue.adhesionPeriodique),
      dateDebut: new FormControl(formuleAdhesionRawValue.dateDebut),
      dureeAdhesionMois: new FormControl(formuleAdhesionRawValue.dureeAdhesionMois),
      montantLibre: new FormControl(formuleAdhesionRawValue.montantLibre),
      description: new FormControl(formuleAdhesionRawValue.description),
      tarif: new FormControl(formuleAdhesionRawValue.tarif),
      adhesion: new FormControl(formuleAdhesionRawValue.adhesion),
    });
  }

  getFormuleAdhesion(form: FormuleAdhesionFormGroup): IFormuleAdhesion | NewFormuleAdhesion {
    return this.convertFormuleAdhesionRawValueToFormuleAdhesion(
      form.getRawValue() as FormuleAdhesionFormRawValue | NewFormuleAdhesionFormRawValue
    );
  }

  resetForm(form: FormuleAdhesionFormGroup, formuleAdhesion: FormuleAdhesionFormGroupInput): void {
    const formuleAdhesionRawValue = this.convertFormuleAdhesionToFormuleAdhesionRawValue({ ...this.getFormDefaults(), ...formuleAdhesion });
    form.reset(
      {
        ...formuleAdhesionRawValue,
        id: { value: formuleAdhesionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FormuleAdhesionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      adhesionPeriodique: false,
      dateDebut: currentTime,
      montantLibre: false,
    };
  }

  private convertFormuleAdhesionRawValueToFormuleAdhesion(
    rawFormuleAdhesion: FormuleAdhesionFormRawValue | NewFormuleAdhesionFormRawValue
  ): IFormuleAdhesion | NewFormuleAdhesion {
    return {
      ...rawFormuleAdhesion,
      dateDebut: dayjs(rawFormuleAdhesion.dateDebut, DATE_TIME_FORMAT),
    };
  }

  private convertFormuleAdhesionToFormuleAdhesionRawValue(
    formuleAdhesion: IFormuleAdhesion | (Partial<NewFormuleAdhesion> & FormuleAdhesionFormDefaults)
  ): FormuleAdhesionFormRawValue | PartialWithRequiredKeyOf<NewFormuleAdhesionFormRawValue> {
    return {
      ...formuleAdhesion,
      dateDebut: formuleAdhesion.dateDebut ? formuleAdhesion.dateDebut.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
