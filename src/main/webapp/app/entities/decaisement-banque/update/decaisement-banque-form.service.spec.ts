import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../decaisement-banque.test-samples';

import { DecaisementBanqueFormService } from './decaisement-banque-form.service';

describe('DecaisementBanque Form Service', () => {
  let service: DecaisementBanqueFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DecaisementBanqueFormService);
  });

  describe('Service methods', () => {
    describe('createDecaisementBanqueFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDecaisementBanqueFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            montant: expect.any(Object),
            dateDecaissement: expect.any(Object),
            montantDecaisse: expect.any(Object),
            commentaire: expect.any(Object),
            compteBanque: expect.any(Object),
          })
        );
      });

      it('passing IDecaisementBanque should create a new form with FormGroup', () => {
        const formGroup = service.createDecaisementBanqueFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            montant: expect.any(Object),
            dateDecaissement: expect.any(Object),
            montantDecaisse: expect.any(Object),
            commentaire: expect.any(Object),
            compteBanque: expect.any(Object),
          })
        );
      });
    });

    describe('getDecaisementBanque', () => {
      it('should return NewDecaisementBanque for default DecaisementBanque initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDecaisementBanqueFormGroup(sampleWithNewData);

        const decaisementBanque = service.getDecaisementBanque(formGroup) as any;

        expect(decaisementBanque).toMatchObject(sampleWithNewData);
      });

      it('should return NewDecaisementBanque for empty DecaisementBanque initial value', () => {
        const formGroup = service.createDecaisementBanqueFormGroup();

        const decaisementBanque = service.getDecaisementBanque(formGroup) as any;

        expect(decaisementBanque).toMatchObject({});
      });

      it('should return IDecaisementBanque', () => {
        const formGroup = service.createDecaisementBanqueFormGroup(sampleWithRequiredData);

        const decaisementBanque = service.getDecaisementBanque(formGroup) as any;

        expect(decaisementBanque).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDecaisementBanque should not enable id FormControl', () => {
        const formGroup = service.createDecaisementBanqueFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDecaisementBanque should disable id FormControl', () => {
        const formGroup = service.createDecaisementBanqueFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
