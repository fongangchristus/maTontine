import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../historique-personne.test-samples';

import { HistoriquePersonneFormService } from './historique-personne-form.service';

describe('HistoriquePersonne Form Service', () => {
  let service: HistoriquePersonneFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HistoriquePersonneFormService);
  });

  describe('Service methods', () => {
    describe('createHistoriquePersonneFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHistoriquePersonneFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateAction: expect.any(Object),
            matriculePersonne: expect.any(Object),
            action: expect.any(Object),
            result: expect.any(Object),
            description: expect.any(Object),
            personne: expect.any(Object),
          })
        );
      });

      it('passing IHistoriquePersonne should create a new form with FormGroup', () => {
        const formGroup = service.createHistoriquePersonneFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateAction: expect.any(Object),
            matriculePersonne: expect.any(Object),
            action: expect.any(Object),
            result: expect.any(Object),
            description: expect.any(Object),
            personne: expect.any(Object),
          })
        );
      });
    });

    describe('getHistoriquePersonne', () => {
      it('should return NewHistoriquePersonne for default HistoriquePersonne initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createHistoriquePersonneFormGroup(sampleWithNewData);

        const historiquePersonne = service.getHistoriquePersonne(formGroup) as any;

        expect(historiquePersonne).toMatchObject(sampleWithNewData);
      });

      it('should return NewHistoriquePersonne for empty HistoriquePersonne initial value', () => {
        const formGroup = service.createHistoriquePersonneFormGroup();

        const historiquePersonne = service.getHistoriquePersonne(formGroup) as any;

        expect(historiquePersonne).toMatchObject({});
      });

      it('should return IHistoriquePersonne', () => {
        const formGroup = service.createHistoriquePersonneFormGroup(sampleWithRequiredData);

        const historiquePersonne = service.getHistoriquePersonne(formGroup) as any;

        expect(historiquePersonne).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHistoriquePersonne should not enable id FormControl', () => {
        const formGroup = service.createHistoriquePersonneFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHistoriquePersonne should disable id FormControl', () => {
        const formGroup = service.createHistoriquePersonneFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
