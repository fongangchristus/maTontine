import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../adhesion.test-samples';

import { AdhesionFormService } from './adhesion-form.service';

describe('Adhesion Form Service', () => {
  let service: AdhesionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdhesionFormService);
  });

  describe('Service methods', () => {
    describe('createAdhesionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAdhesionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            statutAdhesion: expect.any(Object),
            matriculePersonne: expect.any(Object),
            dateDebutAdhesion: expect.any(Object),
            dateFinAdhesion: expect.any(Object),
          })
        );
      });

      it('passing IAdhesion should create a new form with FormGroup', () => {
        const formGroup = service.createAdhesionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            statutAdhesion: expect.any(Object),
            matriculePersonne: expect.any(Object),
            dateDebutAdhesion: expect.any(Object),
            dateFinAdhesion: expect.any(Object),
          })
        );
      });
    });

    describe('getAdhesion', () => {
      it('should return NewAdhesion for default Adhesion initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAdhesionFormGroup(sampleWithNewData);

        const adhesion = service.getAdhesion(formGroup) as any;

        expect(adhesion).toMatchObject(sampleWithNewData);
      });

      it('should return NewAdhesion for empty Adhesion initial value', () => {
        const formGroup = service.createAdhesionFormGroup();

        const adhesion = service.getAdhesion(formGroup) as any;

        expect(adhesion).toMatchObject({});
      });

      it('should return IAdhesion', () => {
        const formGroup = service.createAdhesionFormGroup(sampleWithRequiredData);

        const adhesion = service.getAdhesion(formGroup) as any;

        expect(adhesion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAdhesion should not enable id FormControl', () => {
        const formGroup = service.createAdhesionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAdhesion should disable id FormControl', () => {
        const formGroup = service.createAdhesionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
