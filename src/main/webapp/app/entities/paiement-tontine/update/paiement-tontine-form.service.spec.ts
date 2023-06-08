import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../paiement-tontine.test-samples';

import { PaiementTontineFormService } from './paiement-tontine-form.service';

describe('PaiementTontine Form Service', () => {
  let service: PaiementTontineFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaiementTontineFormService);
  });

  describe('Service methods', () => {
    describe('createPaiementTontineFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaiementTontineFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            referencePaiement: expect.any(Object),
            cotisationTontine: expect.any(Object),
            decaissementTontine: expect.any(Object),
          })
        );
      });

      it('passing IPaiementTontine should create a new form with FormGroup', () => {
        const formGroup = service.createPaiementTontineFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            referencePaiement: expect.any(Object),
            cotisationTontine: expect.any(Object),
            decaissementTontine: expect.any(Object),
          })
        );
      });
    });

    describe('getPaiementTontine', () => {
      it('should return NewPaiementTontine for default PaiementTontine initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPaiementTontineFormGroup(sampleWithNewData);

        const paiementTontine = service.getPaiementTontine(formGroup) as any;

        expect(paiementTontine).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaiementTontine for empty PaiementTontine initial value', () => {
        const formGroup = service.createPaiementTontineFormGroup();

        const paiementTontine = service.getPaiementTontine(formGroup) as any;

        expect(paiementTontine).toMatchObject({});
      });

      it('should return IPaiementTontine', () => {
        const formGroup = service.createPaiementTontineFormGroup(sampleWithRequiredData);

        const paiementTontine = service.getPaiementTontine(formGroup) as any;

        expect(paiementTontine).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaiementTontine should not enable id FormControl', () => {
        const formGroup = service.createPaiementTontineFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaiementTontine should disable id FormControl', () => {
        const formGroup = service.createPaiementTontineFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
