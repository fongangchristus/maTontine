import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sanction.test-samples';

import { SanctionFormService } from './sanction-form.service';

describe('Sanction Form Service', () => {
  let service: SanctionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SanctionFormService);
  });

  describe('Service methods', () => {
    describe('createSanctionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSanctionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            matriculeAdherent: expect.any(Object),
            dateSanction: expect.any(Object),
            motifSanction: expect.any(Object),
            description: expect.any(Object),
            codeActivite: expect.any(Object),
            sanctionConfig: expect.any(Object),
          })
        );
      });

      it('passing ISanction should create a new form with FormGroup', () => {
        const formGroup = service.createSanctionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            matriculeAdherent: expect.any(Object),
            dateSanction: expect.any(Object),
            motifSanction: expect.any(Object),
            description: expect.any(Object),
            codeActivite: expect.any(Object),
            sanctionConfig: expect.any(Object),
          })
        );
      });
    });

    describe('getSanction', () => {
      it('should return NewSanction for default Sanction initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSanctionFormGroup(sampleWithNewData);

        const sanction = service.getSanction(formGroup) as any;

        expect(sanction).toMatchObject(sampleWithNewData);
      });

      it('should return NewSanction for empty Sanction initial value', () => {
        const formGroup = service.createSanctionFormGroup();

        const sanction = service.getSanction(formGroup) as any;

        expect(sanction).toMatchObject({});
      });

      it('should return ISanction', () => {
        const formGroup = service.createSanctionFormGroup(sampleWithRequiredData);

        const sanction = service.getSanction(formGroup) as any;

        expect(sanction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISanction should not enable id FormControl', () => {
        const formGroup = service.createSanctionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSanction should disable id FormControl', () => {
        const formGroup = service.createSanctionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
