import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cotisation-banque.test-samples';

import { CotisationBanqueFormService } from './cotisation-banque-form.service';

describe('CotisationBanque Form Service', () => {
  let service: CotisationBanqueFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CotisationBanqueFormService);
  });

  describe('Service methods', () => {
    describe('createCotisationBanqueFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCotisationBanqueFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            montant: expect.any(Object),
            dateCotisation: expect.any(Object),
            montantCotise: expect.any(Object),
            commentaire: expect.any(Object),
            compteBanque: expect.any(Object),
          })
        );
      });

      it('passing ICotisationBanque should create a new form with FormGroup', () => {
        const formGroup = service.createCotisationBanqueFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            montant: expect.any(Object),
            dateCotisation: expect.any(Object),
            montantCotise: expect.any(Object),
            commentaire: expect.any(Object),
            compteBanque: expect.any(Object),
          })
        );
      });
    });

    describe('getCotisationBanque', () => {
      it('should return NewCotisationBanque for default CotisationBanque initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCotisationBanqueFormGroup(sampleWithNewData);

        const cotisationBanque = service.getCotisationBanque(formGroup) as any;

        expect(cotisationBanque).toMatchObject(sampleWithNewData);
      });

      it('should return NewCotisationBanque for empty CotisationBanque initial value', () => {
        const formGroup = service.createCotisationBanqueFormGroup();

        const cotisationBanque = service.getCotisationBanque(formGroup) as any;

        expect(cotisationBanque).toMatchObject({});
      });

      it('should return ICotisationBanque', () => {
        const formGroup = service.createCotisationBanqueFormGroup(sampleWithRequiredData);

        const cotisationBanque = service.getCotisationBanque(formGroup) as any;

        expect(cotisationBanque).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICotisationBanque should not enable id FormControl', () => {
        const formGroup = service.createCotisationBanqueFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCotisationBanque should disable id FormControl', () => {
        const formGroup = service.createCotisationBanqueFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
