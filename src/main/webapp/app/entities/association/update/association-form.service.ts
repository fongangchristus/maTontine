import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAssociation, NewAssociation } from '../association.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssociation for edit and NewAssociationFormGroupInput for create.
 */
type AssociationFormGroupInput = IAssociation | PartialWithRequiredKeyOf<NewAssociation>;

type AssociationFormDefaults = Pick<NewAssociation, 'id'>;

type AssociationFormGroupContent = {
  id: FormControl<IAssociation['id'] | NewAssociation['id']>;
  codeAssociation: FormControl<IAssociation['codeAssociation']>;
  denomination: FormControl<IAssociation['denomination']>;
  slogan: FormControl<IAssociation['slogan']>;
  logoPath: FormControl<IAssociation['logoPath']>;
  reglementPath: FormControl<IAssociation['reglementPath']>;
  statutPath: FormControl<IAssociation['statutPath']>;
  description: FormControl<IAssociation['description']>;
  dateCreation: FormControl<IAssociation['dateCreation']>;
  fuseauHoraire: FormControl<IAssociation['fuseauHoraire']>;
  langue: FormControl<IAssociation['langue']>;
  presentation: FormControl<IAssociation['presentation']>;
  monnaie: FormControl<IAssociation['monnaie']>;
};

export type AssociationFormGroup = FormGroup<AssociationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssociationFormService {
  createAssociationFormGroup(association: AssociationFormGroupInput = { id: null }): AssociationFormGroup {
    const associationRawValue = {
      ...this.getFormDefaults(),
      ...association,
    };
    return new FormGroup<AssociationFormGroupContent>({
      id: new FormControl(
        { value: associationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      codeAssociation: new FormControl(associationRawValue.codeAssociation, {
        validators: [Validators.required],
      }),
      denomination: new FormControl(associationRawValue.denomination),
      slogan: new FormControl(associationRawValue.slogan),
      logoPath: new FormControl(associationRawValue.logoPath),
      reglementPath: new FormControl(associationRawValue.reglementPath),
      statutPath: new FormControl(associationRawValue.statutPath),
      description: new FormControl(associationRawValue.description),
      dateCreation: new FormControl(associationRawValue.dateCreation),
      fuseauHoraire: new FormControl(associationRawValue.fuseauHoraire),
      langue: new FormControl(associationRawValue.langue),
      presentation: new FormControl(associationRawValue.presentation),
      monnaie: new FormControl(associationRawValue.monnaie),
    });
  }

  getAssociation(form: AssociationFormGroup): IAssociation | NewAssociation {
    return form.getRawValue() as IAssociation | NewAssociation;
  }

  resetForm(form: AssociationFormGroup, association: AssociationFormGroupInput): void {
    const associationRawValue = { ...this.getFormDefaults(), ...association };
    form.reset(
      {
        ...associationRawValue,
        id: { value: associationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AssociationFormDefaults {
    return {
      id: null,
    };
  }
}
