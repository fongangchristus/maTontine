import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../paiement-banque.test-samples';

import { PaiementBanqueFormService } from './paiement-banque-form.service';

describe('PaiementBanque Form Service', () => {
  let service: PaiementBanqueFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaiementBanqueFormService);
  });

  describe('Service methods', () => {
    describe('createPaiementBanqueFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaiementBanqueFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            referencePaiement: expect.any(Object),
          })
        );
      });

      it('passing IPaiementBanque should create a new form with FormGroup', () => {
        const formGroup = service.createPaiementBanqueFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            referencePaiement: expect.any(Object),
          })
        );
      });
    });

    describe('getPaiementBanque', () => {
      it('should return NewPaiementBanque for default PaiementBanque initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPaiementBanqueFormGroup(sampleWithNewData);

        const paiementBanque = service.getPaiementBanque(formGroup) as any;

        expect(paiementBanque).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaiementBanque for empty PaiementBanque initial value', () => {
        const formGroup = service.createPaiementBanqueFormGroup();

        const paiementBanque = service.getPaiementBanque(formGroup) as any;

        expect(paiementBanque).toMatchObject({});
      });

      it('should return IPaiementBanque', () => {
        const formGroup = service.createPaiementBanqueFormGroup(sampleWithRequiredData);

        const paiementBanque = service.getPaiementBanque(formGroup) as any;

        expect(paiementBanque).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaiementBanque should not enable id FormControl', () => {
        const formGroup = service.createPaiementBanqueFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaiementBanque should disable id FormControl', () => {
        const formGroup = service.createPaiementBanqueFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
