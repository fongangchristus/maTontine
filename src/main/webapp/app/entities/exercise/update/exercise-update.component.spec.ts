import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ExerciseFormService } from './exercise-form.service';
import { ExerciseService } from '../service/exercise.service';
import { IExercise } from '../exercise.model';
import { IAssociation } from 'app/entities/association/association.model';
import { AssociationService } from 'app/entities/association/service/association.service';

import { ExerciseUpdateComponent } from './exercise-update.component';

describe('Exercise Management Update Component', () => {
  let comp: ExerciseUpdateComponent;
  let fixture: ComponentFixture<ExerciseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let exerciseFormService: ExerciseFormService;
  let exerciseService: ExerciseService;
  let associationService: AssociationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ExerciseUpdateComponent],
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
      .overrideTemplate(ExerciseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExerciseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    exerciseFormService = TestBed.inject(ExerciseFormService);
    exerciseService = TestBed.inject(ExerciseService);
    associationService = TestBed.inject(AssociationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Association query and add missing value', () => {
      const exercise: IExercise = { id: 456 };
      const association: IAssociation = { id: 61295 };
      exercise.association = association;

      const associationCollection: IAssociation[] = [{ id: 46190 }];
      jest.spyOn(associationService, 'query').mockReturnValue(of(new HttpResponse({ body: associationCollection })));
      const additionalAssociations = [association];
      const expectedCollection: IAssociation[] = [...additionalAssociations, ...associationCollection];
      jest.spyOn(associationService, 'addAssociationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exercise });
      comp.ngOnInit();

      expect(associationService.query).toHaveBeenCalled();
      expect(associationService.addAssociationToCollectionIfMissing).toHaveBeenCalledWith(
        associationCollection,
        ...additionalAssociations.map(expect.objectContaining)
      );
      expect(comp.associationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const exercise: IExercise = { id: 456 };
      const association: IAssociation = { id: 19255 };
      exercise.association = association;

      activatedRoute.data = of({ exercise });
      comp.ngOnInit();

      expect(comp.associationsSharedCollection).toContain(association);
      expect(comp.exercise).toEqual(exercise);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExercise>>();
      const exercise = { id: 123 };
      jest.spyOn(exerciseFormService, 'getExercise').mockReturnValue(exercise);
      jest.spyOn(exerciseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exercise });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exercise }));
      saveSubject.complete();

      // THEN
      expect(exerciseFormService.getExercise).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(exerciseService.update).toHaveBeenCalledWith(expect.objectContaining(exercise));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExercise>>();
      const exercise = { id: 123 };
      jest.spyOn(exerciseFormService, 'getExercise').mockReturnValue({ id: null });
      jest.spyOn(exerciseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exercise: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exercise }));
      saveSubject.complete();

      // THEN
      expect(exerciseFormService.getExercise).toHaveBeenCalled();
      expect(exerciseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExercise>>();
      const exercise = { id: 123 };
      jest.spyOn(exerciseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exercise });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(exerciseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAssociation', () => {
      it('Should forward to associationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(associationService, 'compareAssociation');
        comp.compareAssociation(entity, entity2);
        expect(associationService.compareAssociation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
