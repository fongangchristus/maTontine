import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../formule-adhesion.test-samples';

import { FormuleAdhesionFormService } from './formule-adhesion-form.service';

describe('FormuleAdhesion Form Service', () => {
  let service: FormuleAdhesionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormuleAdhesionFormService);
  });

  describe('Service methods', () => {
    describe('createFormuleAdhesionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFormuleAdhesionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            adhesionPeriodique: expect.any(Object),
            dateDebut: expect.any(Object),
            dureeAdhesionMois: expect.any(Object),
            montantLibre: expect.any(Object),
            description: expect.any(Object),
            tarif: expect.any(Object),
            adhesion: expect.any(Object),
          })
        );
      });

      it('passing IFormuleAdhesion should create a new form with FormGroup', () => {
        const formGroup = service.createFormuleAdhesionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            adhesionPeriodique: expect.any(Object),
            dateDebut: expect.any(Object),
            dureeAdhesionMois: expect.any(Object),
            montantLibre: expect.any(Object),
            description: expect.any(Object),
            tarif: expect.any(Object),
            adhesion: expect.any(Object),
          })
        );
      });
    });

    describe('getFormuleAdhesion', () => {
      it('should return NewFormuleAdhesion for default FormuleAdhesion initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFormuleAdhesionFormGroup(sampleWithNewData);

        const formuleAdhesion = service.getFormuleAdhesion(formGroup) as any;

        expect(formuleAdhesion).toMatchObject(sampleWithNewData);
      });

      it('should return NewFormuleAdhesion for empty FormuleAdhesion initial value', () => {
        const formGroup = service.createFormuleAdhesionFormGroup();

        const formuleAdhesion = service.getFormuleAdhesion(formGroup) as any;

        expect(formuleAdhesion).toMatchObject({});
      });

      it('should return IFormuleAdhesion', () => {
        const formGroup = service.createFormuleAdhesionFormGroup(sampleWithRequiredData);

        const formuleAdhesion = service.getFormuleAdhesion(formGroup) as any;

        expect(formuleAdhesion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFormuleAdhesion should not enable id FormControl', () => {
        const formGroup = service.createFormuleAdhesionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFormuleAdhesion should disable id FormControl', () => {
        const formGroup = service.createFormuleAdhesionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
