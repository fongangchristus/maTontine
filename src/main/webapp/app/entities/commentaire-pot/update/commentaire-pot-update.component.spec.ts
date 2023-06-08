import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommentairePotFormService } from './commentaire-pot-form.service';
import { CommentairePotService } from '../service/commentaire-pot.service';
import { ICommentairePot } from '../commentaire-pot.model';
import { IPot } from 'app/entities/pot/pot.model';
import { PotService } from 'app/entities/pot/service/pot.service';

import { CommentairePotUpdateComponent } from './commentaire-pot-update.component';

describe('CommentairePot Management Update Component', () => {
  let comp: CommentairePotUpdateComponent;
  let fixture: ComponentFixture<CommentairePotUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commentairePotFormService: CommentairePotFormService;
  let commentairePotService: CommentairePotService;
  let potService: PotService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CommentairePotUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CommentairePotUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommentairePotUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commentairePotFormService = TestBed.inject(CommentairePotFormService);
    commentairePotService = TestBed.inject(CommentairePotService);
    potService = TestBed.inject(PotService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Pot query and add missing value', () => {
      const commentairePot: ICommentairePot = { id: 456 };
      const pot: IPot = { id: 95790 };
      commentairePot.pot = pot;

      const potCollection: IPot[] = [{ id: 498 }];
      jest.spyOn(potService, 'query').mockReturnValue(of(new HttpResponse({ body: potCollection })));
      const additionalPots = [pot];
      const expectedCollection: IPot[] = [...additionalPots, ...potCollection];
      jest.spyOn(potService, 'addPotToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commentairePot });
      comp.ngOnInit();

      expect(potService.query).toHaveBeenCalled();
      expect(potService.addPotToCollectionIfMissing).toHaveBeenCalledWith(potCollection, ...additionalPots.map(expect.objectContaining));
      expect(comp.potsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commentairePot: ICommentairePot = { id: 456 };
      const pot: IPot = { id: 72382 };
      commentairePot.pot = pot;

      activatedRoute.data = of({ commentairePot });
      comp.ngOnInit();

      expect(comp.potsSharedCollection).toContain(pot);
      expect(comp.commentairePot).toEqual(commentairePot);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommentairePot>>();
      const commentairePot = { id: 123 };
      jest.spyOn(commentairePotFormService, 'getCommentairePot').mockReturnValue(commentairePot);
      jest.spyOn(commentairePotService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commentairePot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commentairePot }));
      saveSubject.complete();

      // THEN
      expect(commentairePotFormService.getCommentairePot).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(commentairePotService.update).toHaveBeenCalledWith(expect.objectContaining(commentairePot));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommentairePot>>();
      const commentairePot = { id: 123 };
      jest.spyOn(commentairePotFormService, 'getCommentairePot').mockReturnValue({ id: null });
      jest.spyOn(commentairePotService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commentairePot: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commentairePot }));
      saveSubject.complete();

      // THEN
      expect(commentairePotFormService.getCommentairePot).toHaveBeenCalled();
      expect(commentairePotService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommentairePot>>();
      const commentairePot = { id: 123 };
      jest.spyOn(commentairePotService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commentairePot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commentairePotService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePot', () => {
      it('Should forward to potService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(potService, 'comparePot');
        comp.comparePot(entity, entity2);
        expect(potService.comparePot).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
