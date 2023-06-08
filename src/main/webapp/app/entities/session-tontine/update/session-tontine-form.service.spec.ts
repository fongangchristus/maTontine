import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../session-tontine.test-samples';

import { SessionTontineFormService } from './session-tontine-form.service';

describe('SessionTontine Form Service', () => {
  let service: SessionTontineFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionTontineFormService);
  });

  describe('Service methods', () => {
    describe('createSessionTontineFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSessionTontineFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            tontine: expect.any(Object),
          })
        );
      });

      it('passing ISessionTontine should create a new form with FormGroup', () => {
        const formGroup = service.createSessionTontineFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            tontine: expect.any(Object),
          })
        );
      });
    });

    describe('getSessionTontine', () => {
      it('should return NewSessionTontine for default SessionTontine initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSessionTontineFormGroup(sampleWithNewData);

        const sessionTontine = service.getSessionTontine(formGroup) as any;

        expect(sessionTontine).toMatchObject(sampleWithNewData);
      });

      it('should return NewSessionTontine for empty SessionTontine initial value', () => {
        const formGroup = service.createSessionTontineFormGroup();

        const sessionTontine = service.getSessionTontine(formGroup) as any;

        expect(sessionTontine).toMatchObject({});
      });

      it('should return ISessionTontine', () => {
        const formGroup = service.createSessionTontineFormGroup(sampleWithRequiredData);

        const sessionTontine = service.getSessionTontine(formGroup) as any;

        expect(sessionTontine).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISessionTontine should not enable id FormControl', () => {
        const formGroup = service.createSessionTontineFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSessionTontine should disable id FormControl', () => {
        const formGroup = service.createSessionTontineFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
