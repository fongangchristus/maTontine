import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../gestionnaire-banque.test-samples';

import { GestionnaireBanqueFormService } from './gestionnaire-banque-form.service';

describe('GestionnaireBanque Form Service', () => {
  let service: GestionnaireBanqueFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GestionnaireBanqueFormService);
  });

  describe('Service methods', () => {
    describe('createGestionnaireBanqueFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGestionnaireBanqueFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            matriculeMembre: expect.any(Object),
            banque: expect.any(Object),
          })
        );
      });

      it('passing IGestionnaireBanque should create a new form with FormGroup', () => {
        const formGroup = service.createGestionnaireBanqueFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            matriculeMembre: expect.any(Object),
            banque: expect.any(Object),
          })
        );
      });
    });

    describe('getGestionnaireBanque', () => {
      it('should return NewGestionnaireBanque for default GestionnaireBanque initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createGestionnaireBanqueFormGroup(sampleWithNewData);

        const gestionnaireBanque = service.getGestionnaireBanque(formGroup) as any;

        expect(gestionnaireBanque).toMatchObject(sampleWithNewData);
      });

      it('should return NewGestionnaireBanque for empty GestionnaireBanque initial value', () => {
        const formGroup = service.createGestionnaireBanqueFormGroup();

        const gestionnaireBanque = service.getGestionnaireBanque(formGroup) as any;

        expect(gestionnaireBanque).toMatchObject({});
      });

      it('should return IGestionnaireBanque', () => {
        const formGroup = service.createGestionnaireBanqueFormGroup(sampleWithRequiredData);

        const gestionnaireBanque = service.getGestionnaireBanque(formGroup) as any;

        expect(gestionnaireBanque).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGestionnaireBanque should not enable id FormControl', () => {
        const formGroup = service.createGestionnaireBanqueFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGestionnaireBanque should disable id FormControl', () => {
        const formGroup = service.createGestionnaireBanqueFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
