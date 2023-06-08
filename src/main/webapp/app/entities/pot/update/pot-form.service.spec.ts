import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pot.test-samples';

import { PotFormService } from './pot-form.service';

describe('Pot Form Service', () => {
  let service: PotFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PotFormService);
  });

  describe('Service methods', () => {
    describe('createPotFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPotFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libele: expect.any(Object),
            codepot: expect.any(Object),
            montantCible: expect.any(Object),
            description: expect.any(Object),
            dateDebutCollecte: expect.any(Object),
            dateFinCollecte: expect.any(Object),
            statut: expect.any(Object),
            typePot: expect.any(Object),
          })
        );
      });

      it('passing IPot should create a new form with FormGroup', () => {
        const formGroup = service.createPotFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libele: expect.any(Object),
            codepot: expect.any(Object),
            montantCible: expect.any(Object),
            description: expect.any(Object),
            dateDebutCollecte: expect.any(Object),
            dateFinCollecte: expect.any(Object),
            statut: expect.any(Object),
            typePot: expect.any(Object),
          })
        );
      });
    });

    describe('getPot', () => {
      it('should return NewPot for default Pot initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPotFormGroup(sampleWithNewData);

        const pot = service.getPot(formGroup) as any;

        expect(pot).toMatchObject(sampleWithNewData);
      });

      it('should return NewPot for empty Pot initial value', () => {
        const formGroup = service.createPotFormGroup();

        const pot = service.getPot(formGroup) as any;

        expect(pot).toMatchObject({});
      });

      it('should return IPot', () => {
        const formGroup = service.createPotFormGroup(sampleWithRequiredData);

        const pot = service.getPot(formGroup) as any;

        expect(pot).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPot should not enable id FormControl', () => {
        const formGroup = service.createPotFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPot should disable id FormControl', () => {
        const formGroup = service.createPotFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
