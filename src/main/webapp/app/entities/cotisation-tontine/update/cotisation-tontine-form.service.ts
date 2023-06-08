import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICotisationTontine, NewCotisationTontine } from '../cotisation-tontine.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICotisationTontine for edit and NewCotisationTontineFormGroupInput for create.
 */
type CotisationTontineFormGroupInput = ICotisationTontine | PartialWithRequiredKeyOf<NewCotisationTontine>;

type CotisationTontineFormDefaults = Pick<NewCotisationTontine, 'id'>;

type CotisationTontineFormGroupContent = {
  id: FormControl<ICotisationTontine['id'] | NewCotisationTontine['id']>;
  montantCotise: FormControl<ICotisationTontine['montantCotise']>;
  pieceJustifPath: FormControl<ICotisationTontine['pieceJustifPath']>;
  dateCotisation: FormControl<ICotisationTontine['dateCotisation']>;
  dateValidation: FormControl<ICotisationTontine['dateValidation']>;
  commentaire: FormControl<ICotisationTontine['commentaire']>;
  etat: FormControl<ICotisationTontine['etat']>;
  sessionTontine: FormControl<ICotisationTontine['sessionTontine']>;
  compteTontine: FormControl<ICotisationTontine['compteTontine']>;
};

export type CotisationTontineFormGroup = FormGroup<CotisationTontineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CotisationTontineFormService {
  createCotisationTontineFormGroup(cotisationTontine: CotisationTontineFormGroupInput = { id: null }): CotisationTontineFormGroup {
    const cotisationTontineRawValue = {
      ...this.getFormDefaults(),
      ...cotisationTontine,
    };
    return new FormGroup<CotisationTontineFormGroupContent>({
      id: new FormControl(
        { value: cotisationTontineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      montantCotise: new FormControl(cotisationTontineRawValue.montantCotise),
      pieceJustifPath: new FormControl(cotisationTontineRawValue.pieceJustifPath),
      dateCotisation: new FormControl(cotisationTontineRawValue.dateCotisation),
      dateValidation: new FormControl(cotisationTontineRawValue.dateValidation),
      commentaire: new FormControl(cotisationTontineRawValue.commentaire),
      etat: new FormControl(cotisationTontineRawValue.etat),
      sessionTontine: new FormControl(cotisationTontineRawValue.sessionTontine),
      compteTontine: new FormControl(cotisationTontineRawValue.compteTontine),
    });
  }

  getCotisationTontine(form: CotisationTontineFormGroup): ICotisationTontine | NewCotisationTontine {
    return form.getRawValue() as ICotisationTontine | NewCotisationTontine;
  }

  resetForm(form: CotisationTontineFormGroup, cotisationTontine: CotisationTontineFormGroupInput): void {
    const cotisationTontineRawValue = { ...this.getFormDefaults(), ...cotisationTontine };
    form.reset(
      {
        ...cotisationTontineRawValue,
        id: { value: cotisationTontineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CotisationTontineFormDefaults {
    return {
      id: null,
    };
  }
}
