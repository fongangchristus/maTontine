import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../compte-tontine.test-samples';

import { CompteTontineFormService } from './compte-tontine-form.service';

describe('CompteTontine Form Service', () => {
  let service: CompteTontineFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CompteTontineFormService);
  });

  describe('Service methods', () => {
    describe('createCompteTontineFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCompteTontineFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            etatDeCompte: expect.any(Object),
            libele: expect.any(Object),
            odreBeneficiere: expect.any(Object),
            matriculeCompte: expect.any(Object),
            matriculeMenbre: expect.any(Object),
            tontine: expect.any(Object),
          })
        );
      });

      it('passing ICompteTontine should create a new form with FormGroup', () => {
        const formGroup = service.createCompteTontineFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            etatDeCompte: expect.any(Object),
            libele: expect.any(Object),
            odreBeneficiere: expect.any(Object),
            matriculeCompte: expect.any(Object),
            matriculeMenbre: expect.any(Object),
            tontine: expect.any(Object),
          })
        );
      });
    });

    describe('getCompteTontine', () => {
      it('should return NewCompteTontine for default CompteTontine initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCompteTontineFormGroup(sampleWithNewData);

        const compteTontine = service.getCompteTontine(formGroup) as any;

        expect(compteTontine).toMatchObject(sampleWithNewData);
      });

      it('should return NewCompteTontine for empty CompteTontine initial value', () => {
        const formGroup = service.createCompteTontineFormGroup();

        const compteTontine = service.getCompteTontine(formGroup) as any;

        expect(compteTontine).toMatchObject({});
      });

      it('should return ICompteTontine', () => {
        const formGroup = service.createCompteTontineFormGroup(sampleWithRequiredData);

        const compteTontine = service.getCompteTontine(formGroup) as any;

        expect(compteTontine).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICompteTontine should not enable id FormControl', () => {
        const formGroup = service.createCompteTontineFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCompteTontine should disable id FormControl', () => {
        const formGroup = service.createCompteTontineFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
