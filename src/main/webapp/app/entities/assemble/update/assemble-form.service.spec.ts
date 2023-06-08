import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../assemble.test-samples';

import { AssembleFormService } from './assemble-form.service';

describe('Assemble Form Service', () => {
  let service: AssembleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AssembleFormService);
  });

  describe('Service methods', () => {
    describe('createAssembleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAssembleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeAssociation: expect.any(Object),
            libele: expect.any(Object),
            enLigne: expect.any(Object),
            dateSeance: expect.any(Object),
            lieuSeance: expect.any(Object),
            matriculeMembreRecoit: expect.any(Object),
            nature: expect.any(Object),
            compteRendu: expect.any(Object),
            resumeAssemble: expect.any(Object),
            documentCRPath: expect.any(Object),
          })
        );
      });

      it('passing IAssemble should create a new form with FormGroup', () => {
        const formGroup = service.createAssembleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeAssociation: expect.any(Object),
            libele: expect.any(Object),
            enLigne: expect.any(Object),
            dateSeance: expect.any(Object),
            lieuSeance: expect.any(Object),
            matriculeMembreRecoit: expect.any(Object),
            nature: expect.any(Object),
            compteRendu: expect.any(Object),
            resumeAssemble: expect.any(Object),
            documentCRPath: expect.any(Object),
          })
        );
      });
    });

    describe('getAssemble', () => {
      it('should return NewAssemble for default Assemble initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAssembleFormGroup(sampleWithNewData);

        const assemble = service.getAssemble(formGroup) as any;

        expect(assemble).toMatchObject(sampleWithNewData);
      });

      it('should return NewAssemble for empty Assemble initial value', () => {
        const formGroup = service.createAssembleFormGroup();

        const assemble = service.getAssemble(formGroup) as any;

        expect(assemble).toMatchObject({});
      });

      it('should return IAssemble', () => {
        const formGroup = service.createAssembleFormGroup(sampleWithRequiredData);

        const assemble = service.getAssemble(formGroup) as any;

        expect(assemble).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAssemble should not enable id FormControl', () => {
        const formGroup = service.createAssembleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAssemble should disable id FormControl', () => {
        const formGroup = service.createAssembleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
