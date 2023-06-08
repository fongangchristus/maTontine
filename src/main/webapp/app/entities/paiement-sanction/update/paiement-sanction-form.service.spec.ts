import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../paiement-sanction.test-samples';

import { PaiementSanctionFormService } from './paiement-sanction-form.service';

describe('PaiementSanction Form Service', () => {
  let service: PaiementSanctionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaiementSanctionFormService);
  });

  describe('Service methods', () => {
    describe('createPaiementSanctionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaiementSanctionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            referencePaiement: expect.any(Object),
            sanction: expect.any(Object),
          })
        );
      });

      it('passing IPaiementSanction should create a new form with FormGroup', () => {
        const formGroup = service.createPaiementSanctionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            referencePaiement: expect.any(Object),
            sanction: expect.any(Object),
          })
        );
      });
    });

    describe('getPaiementSanction', () => {
      it('should return NewPaiementSanction for default PaiementSanction initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPaiementSanctionFormGroup(sampleWithNewData);

        const paiementSanction = service.getPaiementSanction(formGroup) as any;

        expect(paiementSanction).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaiementSanction for empty PaiementSanction initial value', () => {
        const formGroup = service.createPaiementSanctionFormGroup();

        const paiementSanction = service.getPaiementSanction(formGroup) as any;

        expect(paiementSanction).toMatchObject({});
      });

      it('should return IPaiementSanction', () => {
        const formGroup = service.createPaiementSanctionFormGroup(sampleWithRequiredData);

        const paiementSanction = service.getPaiementSanction(formGroup) as any;

        expect(paiementSanction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaiementSanction should not enable id FormControl', () => {
        const formGroup = service.createPaiementSanctionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaiementSanction should disable id FormControl', () => {
        const formGroup = service.createPaiementSanctionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
