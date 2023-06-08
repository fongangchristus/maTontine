import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../commentaire-pot.test-samples';

import { CommentairePotFormService } from './commentaire-pot-form.service';

describe('CommentairePot Form Service', () => {
  let service: CommentairePotFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommentairePotFormService);
  });

  describe('Service methods', () => {
    describe('createCommentairePotFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCommentairePotFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            matriculeContributeur: expect.any(Object),
            identifiantPot: expect.any(Object),
            contenu: expect.any(Object),
            dateComentaire: expect.any(Object),
            pot: expect.any(Object),
          })
        );
      });

      it('passing ICommentairePot should create a new form with FormGroup', () => {
        const formGroup = service.createCommentairePotFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            matriculeContributeur: expect.any(Object),
            identifiantPot: expect.any(Object),
            contenu: expect.any(Object),
            dateComentaire: expect.any(Object),
            pot: expect.any(Object),
          })
        );
      });
    });

    describe('getCommentairePot', () => {
      it('should return NewCommentairePot for default CommentairePot initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCommentairePotFormGroup(sampleWithNewData);

        const commentairePot = service.getCommentairePot(formGroup) as any;

        expect(commentairePot).toMatchObject(sampleWithNewData);
      });

      it('should return NewCommentairePot for empty CommentairePot initial value', () => {
        const formGroup = service.createCommentairePotFormGroup();

        const commentairePot = service.getCommentairePot(formGroup) as any;

        expect(commentairePot).toMatchObject({});
      });

      it('should return ICommentairePot', () => {
        const formGroup = service.createCommentairePotFormGroup(sampleWithRequiredData);

        const commentairePot = service.getCommentairePot(formGroup) as any;

        expect(commentairePot).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICommentairePot should not enable id FormControl', () => {
        const formGroup = service.createCommentairePotFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCommentairePot should disable id FormControl', () => {
        const formGroup = service.createCommentairePotFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
