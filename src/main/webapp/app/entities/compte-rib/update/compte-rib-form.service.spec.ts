import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../compte-rib.test-samples';

import { CompteRIBFormService } from './compte-rib-form.service';

describe('CompteRIB Form Service', () => {
  let service: CompteRIBFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CompteRIBFormService);
  });

  describe('Service methods', () => {
    describe('createCompteRIBFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCompteRIBFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            iban: expect.any(Object),
            titulaireCompte: expect.any(Object),
            verifier: expect.any(Object),
            adherent: expect.any(Object),
          })
        );
      });

      it('passing ICompteRIB should create a new form with FormGroup', () => {
        const formGroup = service.createCompteRIBFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            iban: expect.any(Object),
            titulaireCompte: expect.any(Object),
            verifier: expect.any(Object),
            adherent: expect.any(Object),
          })
        );
      });
    });

    describe('getCompteRIB', () => {
      it('should return NewCompteRIB for default CompteRIB initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCompteRIBFormGroup(sampleWithNewData);

        const compteRIB = service.getCompteRIB(formGroup) as any;

        expect(compteRIB).toMatchObject(sampleWithNewData);
      });

      it('should return NewCompteRIB for empty CompteRIB initial value', () => {
        const formGroup = service.createCompteRIBFormGroup();

        const compteRIB = service.getCompteRIB(formGroup) as any;

        expect(compteRIB).toMatchObject({});
      });

      it('should return ICompteRIB', () => {
        const formGroup = service.createCompteRIBFormGroup(sampleWithRequiredData);

        const compteRIB = service.getCompteRIB(formGroup) as any;

        expect(compteRIB).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICompteRIB should not enable id FormControl', () => {
        const formGroup = service.createCompteRIBFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCompteRIB should disable id FormControl', () => {
        const formGroup = service.createCompteRIBFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
