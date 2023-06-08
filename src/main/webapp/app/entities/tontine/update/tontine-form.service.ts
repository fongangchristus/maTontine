import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITontine, NewTontine } from '../tontine.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITontine for edit and NewTontineFormGroupInput for create.
 */
type TontineFormGroupInput = ITontine | PartialWithRequiredKeyOf<NewTontine>;

type TontineFormDefaults = Pick<NewTontine, 'id'>;

type TontineFormGroupContent = {
  id: FormControl<ITontine['id'] | NewTontine['id']>;
  codeAssociation: FormControl<ITontine['codeAssociation']>;
  libele: FormControl<ITontine['libele']>;
  nombreTour: FormControl<ITontine['nombreTour']>;
  nombreMaxPersonne: FormControl<ITontine['nombreMaxPersonne']>;
  margeBeneficiaire: FormControl<ITontine['margeBeneficiaire']>;
  montantPart: FormControl<ITontine['montantPart']>;
  amandeEchec: FormControl<ITontine['amandeEchec']>;
  dateDebut: FormControl<ITontine['dateDebut']>;
  dateFin: FormControl<ITontine['dateFin']>;
  statutTontine: FormControl<ITontine['statutTontine']>;
  description: FormControl<ITontine['description']>;
};

export type TontineFormGroup = FormGroup<TontineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TontineFormService {
  createTontineFormGroup(tontine: TontineFormGroupInput = { id: null }): TontineFormGroup {
    const tontineRawValue = {
      ...this.getFormDefaults(),
      ...tontine,
    };
    return new FormGroup<TontineFormGroupContent>({
      id: new FormControl(
        { value: tontineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      codeAssociation: new FormControl(tontineRawValue.codeAssociation, {
        validators: [Validators.required],
      }),
      libele: new FormControl(tontineRawValue.libele),
      nombreTour: new FormControl(tontineRawValue.nombreTour),
      nombreMaxPersonne: new FormControl(tontineRawValue.nombreMaxPersonne),
      margeBeneficiaire: new FormControl(tontineRawValue.margeBeneficiaire),
      montantPart: new FormControl(tontineRawValue.montantPart),
      amandeEchec: new FormControl(tontineRawValue.amandeEchec),
      dateDebut: new FormControl(tontineRawValue.dateDebut),
      dateFin: new FormControl(tontineRawValue.dateFin),
      statutTontine: new FormControl(tontineRawValue.statutTontine),
      description: new FormControl(tontineRawValue.description),
    });
  }

  getTontine(form: TontineFormGroup): ITontine | NewTontine {
    return form.getRawValue() as ITontine | NewTontine;
  }

  resetForm(form: TontineFormGroup, tontine: TontineFormGroupInput): void {
    const tontineRawValue = { ...this.getFormDefaults(), ...tontine };
    form.reset(
      {
        ...tontineRawValue,
        id: { value: tontineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TontineFormDefaults {
    return {
      id: null,
    };
  }
}
