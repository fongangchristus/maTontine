import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../paiement-adhesion.test-samples';

import { PaiementAdhesionFormService } from './paiement-adhesion-form.service';

describe('PaiementAdhesion Form Service', () => {
  let service: PaiementAdhesionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaiementAdhesionFormService);
  });

  describe('Service methods', () => {
    describe('createPaiementAdhesionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaiementAdhesionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            referencePaiement: expect.any(Object),
            adhesion: expect.any(Object),
          })
        );
      });

      it('passing IPaiementAdhesion should create a new form with FormGroup', () => {
        const formGroup = service.createPaiementAdhesionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            referencePaiement: expect.any(Object),
            adhesion: expect.any(Object),
          })
        );
      });
    });

    describe('getPaiementAdhesion', () => {
      it('should return NewPaiementAdhesion for default PaiementAdhesion initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPaiementAdhesionFormGroup(sampleWithNewData);

        const paiementAdhesion = service.getPaiementAdhesion(formGroup) as any;

        expect(paiementAdhesion).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaiementAdhesion for empty PaiementAdhesion initial value', () => {
        const formGroup = service.createPaiementAdhesionFormGroup();

        const paiementAdhesion = service.getPaiementAdhesion(formGroup) as any;

        expect(paiementAdhesion).toMatchObject({});
      });

      it('should return IPaiementAdhesion', () => {
        const formGroup = service.createPaiementAdhesionFormGroup(sampleWithRequiredData);

        const paiementAdhesion = service.getPaiementAdhesion(formGroup) as any;

        expect(paiementAdhesion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaiementAdhesion should not enable id FormControl', () => {
        const formGroup = service.createPaiementAdhesionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaiementAdhesion should disable id FormControl', () => {
        const formGroup = service.createPaiementAdhesionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
