import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../document-association.test-samples';

import { DocumentAssociationFormService } from './document-association-form.service';

describe('DocumentAssociation Form Service', () => {
  let service: DocumentAssociationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentAssociationFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentAssociationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentAssociationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeDocument: expect.any(Object),
            libele: expect.any(Object),
            description: expect.any(Object),
            dateEnregistrement: expect.any(Object),
            dateArchivage: expect.any(Object),
            version: expect.any(Object),
            association: expect.any(Object),
          })
        );
      });

      it('passing IDocumentAssociation should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentAssociationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeDocument: expect.any(Object),
            libele: expect.any(Object),
            description: expect.any(Object),
            dateEnregistrement: expect.any(Object),
            dateArchivage: expect.any(Object),
            version: expect.any(Object),
            association: expect.any(Object),
          })
        );
      });
    });

    describe('getDocumentAssociation', () => {
      it('should return NewDocumentAssociation for default DocumentAssociation initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDocumentAssociationFormGroup(sampleWithNewData);

        const documentAssociation = service.getDocumentAssociation(formGroup) as any;

        expect(documentAssociation).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentAssociation for empty DocumentAssociation initial value', () => {
        const formGroup = service.createDocumentAssociationFormGroup();

        const documentAssociation = service.getDocumentAssociation(formGroup) as any;

        expect(documentAssociation).toMatchObject({});
      });

      it('should return IDocumentAssociation', () => {
        const formGroup = service.createDocumentAssociationFormGroup(sampleWithRequiredData);

        const documentAssociation = service.getDocumentAssociation(formGroup) as any;

        expect(documentAssociation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentAssociation should not enable id FormControl', () => {
        const formGroup = service.createDocumentAssociationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentAssociation should disable id FormControl', () => {
        const formGroup = service.createDocumentAssociationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
