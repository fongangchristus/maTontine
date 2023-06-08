import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cotisation-tontine.test-samples';

import { CotisationTontineFormService } from './cotisation-tontine-form.service';

describe('CotisationTontine Form Service', () => {
  let service: CotisationTontineFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CotisationTontineFormService);
  });

  describe('Service methods', () => {
    describe('createCotisationTontineFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCotisationTontineFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            montantCotise: expect.any(Object),
            pieceJustifPath: expect.any(Object),
            dateCotisation: expect.any(Object),
            dateValidation: expect.any(Object),
            commentaire: expect.any(Object),
            etat: expect.any(Object),
            sessionTontine: expect.any(Object),
            compteTontine: expect.any(Object),
          })
        );
      });

      it('passing ICotisationTontine should create a new form with FormGroup', () => {
        const formGroup = service.createCotisationTontineFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            montantCotise: expect.any(Object),
            pieceJustifPath: expect.any(Object),
            dateCotisation: expect.any(Object),
            dateValidation: expect.any(Object),
            commentaire: expect.any(Object),
            etat: expect.any(Object),
            sessionTontine: expect.any(Object),
            compteTontine: expect.any(Object),
          })
        );
      });
    });

    describe('getCotisationTontine', () => {
      it('should return NewCotisationTontine for default CotisationTontine initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCotisationTontineFormGroup(sampleWithNewData);

        const cotisationTontine = service.getCotisationTontine(formGroup) as any;

        expect(cotisationTontine).toMatchObject(sampleWithNewData);
      });

      it('should return NewCotisationTontine for empty CotisationTontine initial value', () => {
        const formGroup = service.createCotisationTontineFormGroup();

        const cotisationTontine = service.getCotisationTontine(formGroup) as any;

        expect(cotisationTontine).toMatchObject({});
      });

      it('should return ICotisationTontine', () => {
        const formGroup = service.createCotisationTontineFormGroup(sampleWithRequiredData);

        const cotisationTontine = service.getCotisationTontine(formGroup) as any;

        expect(cotisationTontine).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICotisationTontine should not enable id FormControl', () => {
        const formGroup = service.createCotisationTontineFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCotisationTontine should disable id FormControl', () => {
        const formGroup = service.createCotisationTontineFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
