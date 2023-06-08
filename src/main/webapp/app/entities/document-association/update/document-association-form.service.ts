import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDocumentAssociation, NewDocumentAssociation } from '../document-association.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentAssociation for edit and NewDocumentAssociationFormGroupInput for create.
 */
type DocumentAssociationFormGroupInput = IDocumentAssociation | PartialWithRequiredKeyOf<NewDocumentAssociation>;

type DocumentAssociationFormDefaults = Pick<NewDocumentAssociation, 'id'>;

type DocumentAssociationFormGroupContent = {
  id: FormControl<IDocumentAssociation['id'] | NewDocumentAssociation['id']>;
  codeDocument: FormControl<IDocumentAssociation['codeDocument']>;
  libele: FormControl<IDocumentAssociation['libele']>;
  description: FormControl<IDocumentAssociation['description']>;
  dateEnregistrement: FormControl<IDocumentAssociation['dateEnregistrement']>;
  dateArchivage: FormControl<IDocumentAssociation['dateArchivage']>;
  version: FormControl<IDocumentAssociation['version']>;
  association: FormControl<IDocumentAssociation['association']>;
};

export type DocumentAssociationFormGroup = FormGroup<DocumentAssociationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentAssociationFormService {
  createDocumentAssociationFormGroup(documentAssociation: DocumentAssociationFormGroupInput = { id: null }): DocumentAssociationFormGroup {
    const documentAssociationRawValue = {
      ...this.getFormDefaults(),
      ...documentAssociation,
    };
    return new FormGroup<DocumentAssociationFormGroupContent>({
      id: new FormControl(
        { value: documentAssociationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      codeDocument: new FormControl(documentAssociationRawValue.codeDocument),
      libele: new FormControl(documentAssociationRawValue.libele),
      description: new FormControl(documentAssociationRawValue.description),
      dateEnregistrement: new FormControl(documentAssociationRawValue.dateEnregistrement),
      dateArchivage: new FormControl(documentAssociationRawValue.dateArchivage),
      version: new FormControl(documentAssociationRawValue.version),
      association: new FormControl(documentAssociationRawValue.association),
    });
  }

  getDocumentAssociation(form: DocumentAssociationFormGroup): IDocumentAssociation | NewDocumentAssociation {
    return form.getRawValue() as IDocumentAssociation | NewDocumentAssociation;
  }

  resetForm(form: DocumentAssociationFormGroup, documentAssociation: DocumentAssociationFormGroupInput): void {
    const documentAssociationRawValue = { ...this.getFormDefaults(), ...documentAssociation };
    form.reset(
      {
        ...documentAssociationRawValue,
        id: { value: documentAssociationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DocumentAssociationFormDefaults {
    return {
      id: null,
    };
  }
}
