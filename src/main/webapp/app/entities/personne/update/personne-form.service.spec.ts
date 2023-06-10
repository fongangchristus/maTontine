import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../personne.test-samples';

import { PersonneFormService } from './personne-form.service';

describe('Personne Form Service', () => {
  let service: PersonneFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PersonneFormService);
  });

  describe('Service methods', () => {
    describe('createPersonneFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPersonneFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idUser: expect.any(Object),
            codeAssociation: expect.any(Object),
            matricule: expect.any(Object),
            nom: expect.any(Object),
            prenom: expect.any(Object),
            telephone: expect.any(Object),
            email: expect.any(Object),
            dateNaissance: expect.any(Object),
            lieuNaissance: expect.any(Object),
            dateInscription: expect.any(Object),
            profession: expect.any(Object),
            sexe: expect.any(Object),
            photoPath: expect.any(Object),
            dateIntegration: expect.any(Object),
            isAdmin: expect.any(Object),
            isDonateur: expect.any(Object),
            isBenevole: expect.any(Object),
            typePersonne: expect.any(Object),
            adresse: expect.any(Object),
          })
        );
      });

      it('passing IPersonne should create a new form with FormGroup', () => {
        const formGroup = service.createPersonneFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idUser: expect.any(Object),
            codeAssociation: expect.any(Object),
            matricule: expect.any(Object),
            nom: expect.any(Object),
            prenom: expect.any(Object),
            telephone: expect.any(Object),
            email: expect.any(Object),
            dateNaissance: expect.any(Object),
            lieuNaissance: expect.any(Object),
            dateInscription: expect.any(Object),
            profession: expect.any(Object),
            sexe: expect.any(Object),
            photoPath: expect.any(Object),
            dateIntegration: expect.any(Object),
            isAdmin: expect.any(Object),
            isDonateur: expect.any(Object),
            isBenevole: expect.any(Object),
            typePersonne: expect.any(Object),
            adresse: expect.any(Object),
          })
        );
      });
    });

    describe('getPersonne', () => {
      it('should return NewPersonne for default Personne initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPersonneFormGroup(sampleWithNewData);

        const personne = service.getPersonne(formGroup) as any;

        expect(personne).toMatchObject(sampleWithNewData);
      });

      it('should return NewPersonne for empty Personne initial value', () => {
        const formGroup = service.createPersonneFormGroup();

        const personne = service.getPersonne(formGroup) as any;

        expect(personne).toMatchObject({});
      });

      it('should return IPersonne', () => {
        const formGroup = service.createPersonneFormGroup(sampleWithRequiredData);

        const personne = service.getPersonne(formGroup) as any;

        expect(personne).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPersonne should not enable id FormControl', () => {
        const formGroup = service.createPersonneFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPersonne should disable id FormControl', () => {
        const formGroup = service.createPersonneFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
