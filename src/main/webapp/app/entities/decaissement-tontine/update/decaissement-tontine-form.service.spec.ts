import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../decaissement-tontine.test-samples';

import { DecaissementTontineFormService } from './decaissement-tontine-form.service';

describe('DecaissementTontine Form Service', () => {
  let service: DecaissementTontineFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DecaissementTontineFormService);
  });

  describe('Service methods', () => {
    describe('createDecaissementTontineFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDecaissementTontineFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libele: expect.any(Object),
            dateDecaissement: expect.any(Object),
            montantDecaisse: expect.any(Object),
            commentaire: expect.any(Object),
            tontine: expect.any(Object),
            compteTontine: expect.any(Object),
          })
        );
      });

      it('passing IDecaissementTontine should create a new form with FormGroup', () => {
        const formGroup = service.createDecaissementTontineFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libele: expect.any(Object),
            dateDecaissement: expect.any(Object),
            montantDecaisse: expect.any(Object),
            commentaire: expect.any(Object),
            tontine: expect.any(Object),
            compteTontine: expect.any(Object),
          })
        );
      });
    });

    describe('getDecaissementTontine', () => {
      it('should return NewDecaissementTontine for default DecaissementTontine initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDecaissementTontineFormGroup(sampleWithNewData);

        const decaissementTontine = service.getDecaissementTontine(formGroup) as any;

        expect(decaissementTontine).toMatchObject(sampleWithNewData);
      });

      it('should return NewDecaissementTontine for empty DecaissementTontine initial value', () => {
        const formGroup = service.createDecaissementTontineFormGroup();

        const decaissementTontine = service.getDecaissementTontine(formGroup) as any;

        expect(decaissementTontine).toMatchObject({});
      });

      it('should return IDecaissementTontine', () => {
        const formGroup = service.createDecaissementTontineFormGroup(sampleWithRequiredData);

        const decaissementTontine = service.getDecaissementTontine(formGroup) as any;

        expect(decaissementTontine).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDecaissementTontine should not enable id FormControl', () => {
        const formGroup = service.createDecaissementTontineFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDecaissementTontine should disable id FormControl', () => {
        const formGroup = service.createDecaissementTontineFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
