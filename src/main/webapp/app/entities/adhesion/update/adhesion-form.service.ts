import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAdhesion, NewAdhesion } from '../adhesion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAdhesion for edit and NewAdhesionFormGroupInput for create.
 */
type AdhesionFormGroupInput = IAdhesion | PartialWithRequiredKeyOf<NewAdhesion>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAdhesion | NewAdhesion> = Omit<T, 'dateDebutAdhesion' | 'dateFinAdhesion'> & {
  dateDebutAdhesion?: string | null;
  dateFinAdhesion?: string | null;
};

type AdhesionFormRawValue = FormValueOf<IAdhesion>;

type NewAdhesionFormRawValue = FormValueOf<NewAdhesion>;

type AdhesionFormDefaults = Pick<NewAdhesion, 'id' | 'dateDebutAdhesion' | 'dateFinAdhesion'>;

type AdhesionFormGroupContent = {
  id: FormControl<AdhesionFormRawValue['id'] | NewAdhesion['id']>;
  statutAdhesion: FormControl<AdhesionFormRawValue['statutAdhesion']>;
  matriculePersonne: FormControl<AdhesionFormRawValue['matriculePersonne']>;
  dateDebutAdhesion: FormControl<AdhesionFormRawValue['dateDebutAdhesion']>;
  dateFinAdhesion: FormControl<AdhesionFormRawValue['dateFinAdhesion']>;
};

export type AdhesionFormGroup = FormGroup<AdhesionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AdhesionFormService {
  createAdhesionFormGroup(adhesion: AdhesionFormGroupInput = { id: null }): AdhesionFormGroup {
    const adhesionRawValue = this.convertAdhesionToAdhesionRawValue({
      ...this.getFormDefaults(),
      ...adhesion,
    });
    return new FormGroup<AdhesionFormGroupContent>({
      id: new FormControl(
        { value: adhesionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      statutAdhesion: new FormControl(adhesionRawValue.statutAdhesion),
      matriculePersonne: new FormControl(adhesionRawValue.matriculePersonne),
      dateDebutAdhesion: new FormControl(adhesionRawValue.dateDebutAdhesion),
      dateFinAdhesion: new FormControl(adhesionRawValue.dateFinAdhesion),
    });
  }

  getAdhesion(form: AdhesionFormGroup): IAdhesion | NewAdhesion {
    return this.convertAdhesionRawValueToAdhesion(form.getRawValue() as AdhesionFormRawValue | NewAdhesionFormRawValue);
  }

  resetForm(form: AdhesionFormGroup, adhesion: AdhesionFormGroupInput): void {
    const adhesionRawValue = this.convertAdhesionToAdhesionRawValue({ ...this.getFormDefaults(), ...adhesion });
    form.reset(
      {
        ...adhesionRawValue,
        id: { value: adhesionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AdhesionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDebutAdhesion: currentTime,
      dateFinAdhesion: currentTime,
    };
  }

  private convertAdhesionRawValueToAdhesion(rawAdhesion: AdhesionFormRawValue | NewAdhesionFormRawValue): IAdhesion | NewAdhesion {
    return {
      ...rawAdhesion,
      dateDebutAdhesion: dayjs(rawAdhesion.dateDebutAdhesion, DATE_TIME_FORMAT),
      dateFinAdhesion: dayjs(rawAdhesion.dateFinAdhesion, DATE_TIME_FORMAT),
    };
  }

  private convertAdhesionToAdhesionRawValue(
    adhesion: IAdhesion | (Partial<NewAdhesion> & AdhesionFormDefaults)
  ): AdhesionFormRawValue | PartialWithRequiredKeyOf<NewAdhesionFormRawValue> {
    return {
      ...adhesion,
      dateDebutAdhesion: adhesion.dateDebutAdhesion ? adhesion.dateDebutAdhesion.format(DATE_TIME_FORMAT) : undefined,
      dateFinAdhesion: adhesion.dateFinAdhesion ? adhesion.dateFinAdhesion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
