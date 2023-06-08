import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-evenement.test-samples';

import { TypeEvenementFormService } from './type-evenement-form.service';

describe('TypeEvenement Form Service', () => {
  let service: TypeEvenementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeEvenementFormService);
  });

  describe('Service methods', () => {
    describe('createTypeEvenementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeEvenementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libele: expect.any(Object),
            observation: expect.any(Object),
          })
        );
      });

      it('passing ITypeEvenement should create a new form with FormGroup', () => {
        const formGroup = service.createTypeEvenementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libele: expect.any(Object),
            observation: expect.any(Object),
          })
        );
      });
    });

    describe('getTypeEvenement', () => {
      it('should return NewTypeEvenement for default TypeEvenement initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTypeEvenementFormGroup(sampleWithNewData);

        const typeEvenement = service.getTypeEvenement(formGroup) as any;

        expect(typeEvenement).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeEvenement for empty TypeEvenement initial value', () => {
        const formGroup = service.createTypeEvenementFormGroup();

        const typeEvenement = service.getTypeEvenement(formGroup) as any;

        expect(typeEvenement).toMatchObject({});
      });

      it('should return ITypeEvenement', () => {
        const formGroup = service.createTypeEvenementFormGroup(sampleWithRequiredData);

        const typeEvenement = service.getTypeEvenement(formGroup) as any;

        expect(typeEvenement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeEvenement should not enable id FormControl', () => {
        const formGroup = service.createTypeEvenementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeEvenement should disable id FormControl', () => {
        const formGroup = service.createTypeEvenementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
