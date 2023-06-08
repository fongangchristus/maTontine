import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../fonction.test-samples';

import { FonctionFormService } from './fonction-form.service';

describe('Fonction Form Service', () => {
  let service: FonctionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FonctionFormService);
  });

  describe('Service methods', () => {
    describe('createFonctionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFonctionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });

      it('passing IFonction should create a new form with FormGroup', () => {
        const formGroup = service.createFonctionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });
    });

    describe('getFonction', () => {
      it('should return NewFonction for default Fonction initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFonctionFormGroup(sampleWithNewData);

        const fonction = service.getFonction(formGroup) as any;

        expect(fonction).toMatchObject(sampleWithNewData);
      });

      it('should return NewFonction for empty Fonction initial value', () => {
        const formGroup = service.createFonctionFormGroup();

        const fonction = service.getFonction(formGroup) as any;

        expect(fonction).toMatchObject({});
      });

      it('should return IFonction', () => {
        const formGroup = service.createFonctionFormGroup(sampleWithRequiredData);

        const fonction = service.getFonction(formGroup) as any;

        expect(fonction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFonction should not enable id FormControl', () => {
        const formGroup = service.createFonctionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFonction should disable id FormControl', () => {
        const formGroup = service.createFonctionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
