import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tontine.test-samples';

import { TontineFormService } from './tontine-form.service';

describe('Tontine Form Service', () => {
  let service: TontineFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TontineFormService);
  });

  describe('Service methods', () => {
    describe('createTontineFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTontineFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeAssociation: expect.any(Object),
            libele: expect.any(Object),
            nombreTour: expect.any(Object),
            nombreMaxPersonne: expect.any(Object),
            margeBeneficiaire: expect.any(Object),
            montantPart: expect.any(Object),
            amandeEchec: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            statutTontine: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });

      it('passing ITontine should create a new form with FormGroup', () => {
        const formGroup = service.createTontineFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeAssociation: expect.any(Object),
            libele: expect.any(Object),
            nombreTour: expect.any(Object),
            nombreMaxPersonne: expect.any(Object),
            margeBeneficiaire: expect.any(Object),
            montantPart: expect.any(Object),
            amandeEchec: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            statutTontine: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });
    });

    describe('getTontine', () => {
      it('should return NewTontine for default Tontine initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTontineFormGroup(sampleWithNewData);

        const tontine = service.getTontine(formGroup) as any;

        expect(tontine).toMatchObject(sampleWithNewData);
      });

      it('should return NewTontine for empty Tontine initial value', () => {
        const formGroup = service.createTontineFormGroup();

        const tontine = service.getTontine(formGroup) as any;

        expect(tontine).toMatchObject({});
      });

      it('should return ITontine', () => {
        const formGroup = service.createTontineFormGroup(sampleWithRequiredData);

        const tontine = service.getTontine(formGroup) as any;

        expect(tontine).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITontine should not enable id FormControl', () => {
        const formGroup = service.createTontineFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTontine should disable id FormControl', () => {
        const formGroup = service.createTontineFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
