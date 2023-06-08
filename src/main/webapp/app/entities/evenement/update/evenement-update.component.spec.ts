import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EvenementFormService } from './evenement-form.service';
import { EvenementService } from '../service/evenement.service';
import { IEvenement } from '../evenement.model';
import { ITypeEvenement } from 'app/entities/type-evenement/type-evenement.model';
import { TypeEvenementService } from 'app/entities/type-evenement/service/type-evenement.service';

import { EvenementUpdateComponent } from './evenement-update.component';

describe('Evenement Management Update Component', () => {
  let comp: EvenementUpdateComponent;
  let fixture: ComponentFixture<EvenementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let evenementFormService: EvenementFormService;
  let evenementService: EvenementService;
  let typeEvenementService: TypeEvenementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EvenementUpdateComponent],
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
      .overrideTemplate(EvenementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EvenementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    evenementFormService = TestBed.inject(EvenementFormService);
    evenementService = TestBed.inject(EvenementService);
    typeEvenementService = TestBed.inject(TypeEvenementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TypeEvenement query and add missing value', () => {
      const evenement: IEvenement = { id: 456 };
      const typeEvenement: ITypeEvenement = { id: 37423 };
      evenement.typeEvenement = typeEvenement;

      const typeEvenementCollection: ITypeEvenement[] = [{ id: 80618 }];
      jest.spyOn(typeEvenementService, 'query').mockReturnValue(of(new HttpResponse({ body: typeEvenementCollection })));
      const additionalTypeEvenements = [typeEvenement];
      const expectedCollection: ITypeEvenement[] = [...additionalTypeEvenements, ...typeEvenementCollection];
      jest.spyOn(typeEvenementService, 'addTypeEvenementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evenement });
      comp.ngOnInit();

      expect(typeEvenementService.query).toHaveBeenCalled();
      expect(typeEvenementService.addTypeEvenementToCollectionIfMissing).toHaveBeenCalledWith(
        typeEvenementCollection,
        ...additionalTypeEvenements.map(expect.objectContaining)
      );
      expect(comp.typeEvenementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const evenement: IEvenement = { id: 456 };
      const typeEvenement: ITypeEvenement = { id: 59504 };
      evenement.typeEvenement = typeEvenement;

      activatedRoute.data = of({ evenement });
      comp.ngOnInit();

      expect(comp.typeEvenementsSharedCollection).toContain(typeEvenement);
      expect(comp.evenement).toEqual(evenement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvenement>>();
      const evenement = { id: 123 };
      jest.spyOn(evenementFormService, 'getEvenement').mockReturnValue(evenement);
      jest.spyOn(evenementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evenement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evenement }));
      saveSubject.complete();

      // THEN
      expect(evenementFormService.getEvenement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(evenementService.update).toHaveBeenCalledWith(expect.objectContaining(evenement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvenement>>();
      const evenement = { id: 123 };
      jest.spyOn(evenementFormService, 'getEvenement').mockReturnValue({ id: null });
      jest.spyOn(evenementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evenement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evenement }));
      saveSubject.complete();

      // THEN
      expect(evenementFormService.getEvenement).toHaveBeenCalled();
      expect(evenementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvenement>>();
      const evenement = { id: 123 };
      jest.spyOn(evenementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evenement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(evenementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypeEvenement', () => {
      it('Should forward to typeEvenementService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeEvenementService, 'compareTypeEvenement');
        comp.compareTypeEvenement(entity, entity2);
        expect(typeEvenementService.compareTypeEvenement).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
