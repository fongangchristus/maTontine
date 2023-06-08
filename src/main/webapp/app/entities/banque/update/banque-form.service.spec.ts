import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../banque.test-samples';

import { BanqueFormService } from './banque-form.service';

describe('Banque Form Service', () => {
  let service: BanqueFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BanqueFormService);
  });

  describe('Service methods', () => {
    describe('createBanqueFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBanqueFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeAssociation: expect.any(Object),
            libelle: expect.any(Object),
            description: expect.any(Object),
            dateOuverture: expect.any(Object),
            dateCloture: expect.any(Object),
          })
        );
      });

      it('passing IBanque should create a new form with FormGroup', () => {
        const formGroup = service.createBanqueFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeAssociation: expect.any(Object),
            libelle: expect.any(Object),
            description: expect.any(Object),
            dateOuverture: expect.any(Object),
            dateCloture: expect.any(Object),
          })
        );
      });
    });

    describe('getBanque', () => {
      it('should return NewBanque for default Banque initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBanqueFormGroup(sampleWithNewData);

        const banque = service.getBanque(formGroup) as any;

        expect(banque).toMatchObject(sampleWithNewData);
      });

      it('should return NewBanque for empty Banque initial value', () => {
        const formGroup = service.createBanqueFormGroup();

        const banque = service.getBanque(formGroup) as any;

        expect(banque).toMatchObject({});
      });

      it('should return IBanque', () => {
        const formGroup = service.createBanqueFormGroup(sampleWithRequiredData);

        const banque = service.getBanque(formGroup) as any;

        expect(banque).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBanque should not enable id FormControl', () => {
        const formGroup = service.createBanqueFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBanque should disable id FormControl', () => {
        const formGroup = service.createBanqueFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
