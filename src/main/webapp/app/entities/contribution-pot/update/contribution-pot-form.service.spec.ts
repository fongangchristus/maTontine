import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../contribution-pot.test-samples';

import { ContributionPotFormService } from './contribution-pot-form.service';

describe('ContributionPot Form Service', () => {
  let service: ContributionPotFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContributionPotFormService);
  });

  describe('Service methods', () => {
    describe('createContributionPotFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContributionPotFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            identifiant: expect.any(Object),
            matriculeContributeur: expect.any(Object),
            montantContribution: expect.any(Object),
            dateContribution: expect.any(Object),
            pot: expect.any(Object),
          })
        );
      });

      it('passing IContributionPot should create a new form with FormGroup', () => {
        const formGroup = service.createContributionPotFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            identifiant: expect.any(Object),
            matriculeContributeur: expect.any(Object),
            montantContribution: expect.any(Object),
            dateContribution: expect.any(Object),
            pot: expect.any(Object),
          })
        );
      });
    });

    describe('getContributionPot', () => {
      it('should return NewContributionPot for default ContributionPot initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createContributionPotFormGroup(sampleWithNewData);

        const contributionPot = service.getContributionPot(formGroup) as any;

        expect(contributionPot).toMatchObject(sampleWithNewData);
      });

      it('should return NewContributionPot for empty ContributionPot initial value', () => {
        const formGroup = service.createContributionPotFormGroup();

        const contributionPot = service.getContributionPot(formGroup) as any;

        expect(contributionPot).toMatchObject({});
      });

      it('should return IContributionPot', () => {
        const formGroup = service.createContributionPotFormGroup(sampleWithRequiredData);

        const contributionPot = service.getContributionPot(formGroup) as any;

        expect(contributionPot).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContributionPot should not enable id FormControl', () => {
        const formGroup = service.createContributionPotFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContributionPot should disable id FormControl', () => {
        const formGroup = service.createContributionPotFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
