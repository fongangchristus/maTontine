import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAssemble, NewAssemble } from '../assemble.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssemble for edit and NewAssembleFormGroupInput for create.
 */
type AssembleFormGroupInput = IAssemble | PartialWithRequiredKeyOf<NewAssemble>;

type AssembleFormDefaults = Pick<NewAssemble, 'id' | 'enLigne'>;

type AssembleFormGroupContent = {
  id: FormControl<IAssemble['id'] | NewAssemble['id']>;
  codeAssociation: FormControl<IAssemble['codeAssociation']>;
  libele: FormControl<IAssemble['libele']>;
  enLigne: FormControl<IAssemble['enLigne']>;
  dateSeance: FormControl<IAssemble['dateSeance']>;
  lieuSeance: FormControl<IAssemble['lieuSeance']>;
  matriculeMembreRecoit: FormControl<IAssemble['matriculeMembreRecoit']>;
  nature: FormControl<IAssemble['nature']>;
  compteRendu: FormControl<IAssemble['compteRendu']>;
  resumeAssemble: FormControl<IAssemble['resumeAssemble']>;
  documentCRPath: FormControl<IAssemble['documentCRPath']>;
};

export type AssembleFormGroup = FormGroup<AssembleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssembleFormService {
  createAssembleFormGroup(assemble: AssembleFormGroupInput = { id: null }): AssembleFormGroup {
    const assembleRawValue = {
      ...this.getFormDefaults(),
      ...assemble,
    };
    return new FormGroup<AssembleFormGroupContent>({
      id: new FormControl(
        { value: assembleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      codeAssociation: new FormControl(assembleRawValue.codeAssociation, {
        validators: [Validators.required],
      }),
      libele: new FormControl(assembleRawValue.libele),
      enLigne: new FormControl(assembleRawValue.enLigne),
      dateSeance: new FormControl(assembleRawValue.dateSeance),
      lieuSeance: new FormControl(assembleRawValue.lieuSeance),
      matriculeMembreRecoit: new FormControl(assembleRawValue.matriculeMembreRecoit),
      nature: new FormControl(assembleRawValue.nature),
      compteRendu: new FormControl(assembleRawValue.compteRendu),
      resumeAssemble: new FormControl(assembleRawValue.resumeAssemble),
      documentCRPath: new FormControl(assembleRawValue.documentCRPath),
    });
  }

  getAssemble(form: AssembleFormGroup): IAssemble | NewAssemble {
    return form.getRawValue() as IAssemble | NewAssemble;
  }

  resetForm(form: AssembleFormGroup, assemble: AssembleFormGroupInput): void {
    const assembleRawValue = { ...this.getFormDefaults(), ...assemble };
    form.reset(
      {
        ...assembleRawValue,
        id: { value: assembleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AssembleFormDefaults {
    return {
      id: null,
      enLigne: false,
    };
  }
}
