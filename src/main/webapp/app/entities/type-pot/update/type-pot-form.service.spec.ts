import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-pot.test-samples';

import { TypePotFormService } from './type-pot-form.service';

describe('TypePot Form Service', () => {
  let service: TypePotFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypePotFormService);
  });

  describe('Service methods', () => {
    describe('createTypePotFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypePotFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libele: expect.any(Object),
            descrption: expect.any(Object),
          })
        );
      });

      it('passing ITypePot should create a new form with FormGroup', () => {
        const formGroup = service.createTypePotFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libele: expect.any(Object),
            descrption: expect.any(Object),
          })
        );
      });
    });

    describe('getTypePot', () => {
      it('should return NewTypePot for default TypePot initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTypePotFormGroup(sampleWithNewData);

        const typePot = service.getTypePot(formGroup) as any;

        expect(typePot).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypePot for empty TypePot initial value', () => {
        const formGroup = service.createTypePotFormGroup();

        const typePot = service.getTypePot(formGroup) as any;

        expect(typePot).toMatchObject({});
      });

      it('should return ITypePot', () => {
        const formGroup = service.createTypePotFormGroup(sampleWithRequiredData);

        const typePot = service.getTypePot(formGroup) as any;

        expect(typePot).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypePot should not enable id FormControl', () => {
        const formGroup = service.createTypePotFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypePot should disable id FormControl', () => {
        const formGroup = service.createTypePotFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
