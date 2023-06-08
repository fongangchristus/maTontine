import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../monnaie.test-samples';

import { MonnaieFormService } from './monnaie-form.service';

describe('Monnaie Form Service', () => {
  let service: MonnaieFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MonnaieFormService);
  });

  describe('Service methods', () => {
    describe('createMonnaieFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMonnaieFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libele: expect.any(Object),
          })
        );
      });

      it('passing IMonnaie should create a new form with FormGroup', () => {
        const formGroup = service.createMonnaieFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libele: expect.any(Object),
          })
        );
      });
    });

    describe('getMonnaie', () => {
      it('should return NewMonnaie for default Monnaie initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMonnaieFormGroup(sampleWithNewData);

        const monnaie = service.getMonnaie(formGroup) as any;

        expect(monnaie).toMatchObject(sampleWithNewData);
      });

      it('should return NewMonnaie for empty Monnaie initial value', () => {
        const formGroup = service.createMonnaieFormGroup();

        const monnaie = service.getMonnaie(formGroup) as any;

        expect(monnaie).toMatchObject({});
      });

      it('should return IMonnaie', () => {
        const formGroup = service.createMonnaieFormGroup(sampleWithRequiredData);

        const monnaie = service.getMonnaie(formGroup) as any;

        expect(monnaie).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMonnaie should not enable id FormControl', () => {
        const formGroup = service.createMonnaieFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMonnaie should disable id FormControl', () => {
        const formGroup = service.createMonnaieFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
