import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../compte-banque.test-samples';

import { CompteBanqueFormService } from './compte-banque-form.service';

describe('CompteBanque Form Service', () => {
  let service: CompteBanqueFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CompteBanqueFormService);
  });

  describe('Service methods', () => {
    describe('createCompteBanqueFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCompteBanqueFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            description: expect.any(Object),
            matriculeAdherant: expect.any(Object),
            montantDisponnible: expect.any(Object),
            banque: expect.any(Object),
          })
        );
      });

      it('passing ICompteBanque should create a new form with FormGroup', () => {
        const formGroup = service.createCompteBanqueFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            description: expect.any(Object),
            matriculeAdherant: expect.any(Object),
            montantDisponnible: expect.any(Object),
            banque: expect.any(Object),
          })
        );
      });
    });

    describe('getCompteBanque', () => {
      it('should return NewCompteBanque for default CompteBanque initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCompteBanqueFormGroup(sampleWithNewData);

        const compteBanque = service.getCompteBanque(formGroup) as any;

        expect(compteBanque).toMatchObject(sampleWithNewData);
      });

      it('should return NewCompteBanque for empty CompteBanque initial value', () => {
        const formGroup = service.createCompteBanqueFormGroup();

        const compteBanque = service.getCompteBanque(formGroup) as any;

        expect(compteBanque).toMatchObject({});
      });

      it('should return ICompteBanque', () => {
        const formGroup = service.createCompteBanqueFormGroup(sampleWithRequiredData);

        const compteBanque = service.getCompteBanque(formGroup) as any;

        expect(compteBanque).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICompteBanque should not enable id FormControl', () => {
        const formGroup = service.createCompteBanqueFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCompteBanque should disable id FormControl', () => {
        const formGroup = service.createCompteBanqueFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
