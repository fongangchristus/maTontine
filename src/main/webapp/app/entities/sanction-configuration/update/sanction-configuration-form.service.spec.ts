import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sanction-configuration.test-samples';

import { SanctionConfigurationFormService } from './sanction-configuration-form.service';

describe('SanctionConfiguration Form Service', () => {
  let service: SanctionConfigurationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SanctionConfigurationFormService);
  });

  describe('Service methods', () => {
    describe('createSanctionConfigurationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSanctionConfigurationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeAssociation: expect.any(Object),
            codeTontine: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });

      it('passing ISanctionConfiguration should create a new form with FormGroup', () => {
        const formGroup = service.createSanctionConfigurationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeAssociation: expect.any(Object),
            codeTontine: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });
    });

    describe('getSanctionConfiguration', () => {
      it('should return NewSanctionConfiguration for default SanctionConfiguration initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSanctionConfigurationFormGroup(sampleWithNewData);

        const sanctionConfiguration = service.getSanctionConfiguration(formGroup) as any;

        expect(sanctionConfiguration).toMatchObject(sampleWithNewData);
      });

      it('should return NewSanctionConfiguration for empty SanctionConfiguration initial value', () => {
        const formGroup = service.createSanctionConfigurationFormGroup();

        const sanctionConfiguration = service.getSanctionConfiguration(formGroup) as any;

        expect(sanctionConfiguration).toMatchObject({});
      });

      it('should return ISanctionConfiguration', () => {
        const formGroup = service.createSanctionConfigurationFormGroup(sampleWithRequiredData);

        const sanctionConfiguration = service.getSanctionConfiguration(formGroup) as any;

        expect(sanctionConfiguration).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISanctionConfiguration should not enable id FormControl', () => {
        const formGroup = service.createSanctionConfigurationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSanctionConfiguration should disable id FormControl', () => {
        const formGroup = service.createSanctionConfigurationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
