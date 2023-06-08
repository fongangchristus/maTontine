import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../fonction-adherent.test-samples';

import { FonctionAdherentFormService } from './fonction-adherent-form.service';

describe('FonctionAdherent Form Service', () => {
  let service: FonctionAdherentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FonctionAdherentFormService);
  });

  describe('Service methods', () => {
    describe('createFonctionAdherentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFonctionAdherentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            matriculeAdherent: expect.any(Object),
            codeFonction: expect.any(Object),
            datePriseFonction: expect.any(Object),
            dateFinFonction: expect.any(Object),
            actif: expect.any(Object),
            adherent: expect.any(Object),
            fonction: expect.any(Object),
          })
        );
      });

      it('passing IFonctionAdherent should create a new form with FormGroup', () => {
        const formGroup = service.createFonctionAdherentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            matriculeAdherent: expect.any(Object),
            codeFonction: expect.any(Object),
            datePriseFonction: expect.any(Object),
            dateFinFonction: expect.any(Object),
            actif: expect.any(Object),
            adherent: expect.any(Object),
            fonction: expect.any(Object),
          })
        );
      });
    });

    describe('getFonctionAdherent', () => {
      it('should return NewFonctionAdherent for default FonctionAdherent initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFonctionAdherentFormGroup(sampleWithNewData);

        const fonctionAdherent = service.getFonctionAdherent(formGroup) as any;

        expect(fonctionAdherent).toMatchObject(sampleWithNewData);
      });

      it('should return NewFonctionAdherent for empty FonctionAdherent initial value', () => {
        const formGroup = service.createFonctionAdherentFormGroup();

        const fonctionAdherent = service.getFonctionAdherent(formGroup) as any;

        expect(fonctionAdherent).toMatchObject({});
      });

      it('should return IFonctionAdherent', () => {
        const formGroup = service.createFonctionAdherentFormGroup(sampleWithRequiredData);

        const fonctionAdherent = service.getFonctionAdherent(formGroup) as any;

        expect(fonctionAdherent).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFonctionAdherent should not enable id FormControl', () => {
        const formGroup = service.createFonctionAdherentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFonctionAdherent should disable id FormControl', () => {
        const formGroup = service.createFonctionAdherentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
