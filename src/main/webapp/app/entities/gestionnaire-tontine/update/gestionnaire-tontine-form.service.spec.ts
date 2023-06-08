import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../gestionnaire-tontine.test-samples';

import { GestionnaireTontineFormService } from './gestionnaire-tontine-form.service';

describe('GestionnaireTontine Form Service', () => {
  let service: GestionnaireTontineFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GestionnaireTontineFormService);
  });

  describe('Service methods', () => {
    describe('createGestionnaireTontineFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGestionnaireTontineFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            matriculeAdherent: expect.any(Object),
            codeTontine: expect.any(Object),
            datePriseFonction: expect.any(Object),
            dateFinFonction: expect.any(Object),
            tontine: expect.any(Object),
          })
        );
      });

      it('passing IGestionnaireTontine should create a new form with FormGroup', () => {
        const formGroup = service.createGestionnaireTontineFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            matriculeAdherent: expect.any(Object),
            codeTontine: expect.any(Object),
            datePriseFonction: expect.any(Object),
            dateFinFonction: expect.any(Object),
            tontine: expect.any(Object),
          })
        );
      });
    });

    describe('getGestionnaireTontine', () => {
      it('should return NewGestionnaireTontine for default GestionnaireTontine initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createGestionnaireTontineFormGroup(sampleWithNewData);

        const gestionnaireTontine = service.getGestionnaireTontine(formGroup) as any;

        expect(gestionnaireTontine).toMatchObject(sampleWithNewData);
      });

      it('should return NewGestionnaireTontine for empty GestionnaireTontine initial value', () => {
        const formGroup = service.createGestionnaireTontineFormGroup();

        const gestionnaireTontine = service.getGestionnaireTontine(formGroup) as any;

        expect(gestionnaireTontine).toMatchObject({});
      });

      it('should return IGestionnaireTontine', () => {
        const formGroup = service.createGestionnaireTontineFormGroup(sampleWithRequiredData);

        const gestionnaireTontine = service.getGestionnaireTontine(formGroup) as any;

        expect(gestionnaireTontine).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGestionnaireTontine should not enable id FormControl', () => {
        const formGroup = service.createGestionnaireTontineFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGestionnaireTontine should disable id FormControl', () => {
        const formGroup = service.createGestionnaireTontineFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
