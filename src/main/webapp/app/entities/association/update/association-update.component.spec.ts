import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AssociationFormService } from './association-form.service';
import { AssociationService } from '../service/association.service';
import { IAssociation } from '../association.model';
import { IMonnaie } from 'app/entities/monnaie/monnaie.model';
import { MonnaieService } from 'app/entities/monnaie/service/monnaie.service';

import { AssociationUpdateComponent } from './association-update.component';

describe('Association Management Update Component', () => {
  let comp: AssociationUpdateComponent;
  let fixture: ComponentFixture<AssociationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let associationFormService: AssociationFormService;
  let associationService: AssociationService;
  let monnaieService: MonnaieService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AssociationUpdateComponent],
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
      .overrideTemplate(AssociationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssociationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    associationFormService = TestBed.inject(AssociationFormService);
    associationService = TestBed.inject(AssociationService);
    monnaieService = TestBed.inject(MonnaieService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Monnaie query and add missing value', () => {
      const association: IAssociation = { id: 456 };
      const monnaie: IMonnaie = { id: 36197 };
      association.monnaie = monnaie;

      const monnaieCollection: IMonnaie[] = [{ id: 97969 }];
      jest.spyOn(monnaieService, 'query').mockReturnValue(of(new HttpResponse({ body: monnaieCollection })));
      const additionalMonnaies = [monnaie];
      const expectedCollection: IMonnaie[] = [...additionalMonnaies, ...monnaieCollection];
      jest.spyOn(monnaieService, 'addMonnaieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ association });
      comp.ngOnInit();

      expect(monnaieService.query).toHaveBeenCalled();
      expect(monnaieService.addMonnaieToCollectionIfMissing).toHaveBeenCalledWith(
        monnaieCollection,
        ...additionalMonnaies.map(expect.objectContaining)
      );
      expect(comp.monnaiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const association: IAssociation = { id: 456 };
      const monnaie: IMonnaie = { id: 33478 };
      association.monnaie = monnaie;

      activatedRoute.data = of({ association });
      comp.ngOnInit();

      expect(comp.monnaiesSharedCollection).toContain(monnaie);
      expect(comp.association).toEqual(association);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssociation>>();
      const association = { id: 123 };
      jest.spyOn(associationFormService, 'getAssociation').mockReturnValue(association);
      jest.spyOn(associationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ association });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: association }));
      saveSubject.complete();

      // THEN
      expect(associationFormService.getAssociation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(associationService.update).toHaveBeenCalledWith(expect.objectContaining(association));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssociation>>();
      const association = { id: 123 };
      jest.spyOn(associationFormService, 'getAssociation').mockReturnValue({ id: null });
      jest.spyOn(associationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ association: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: association }));
      saveSubject.complete();

      // THEN
      expect(associationFormService.getAssociation).toHaveBeenCalled();
      expect(associationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssociation>>();
      const association = { id: 123 };
      jest.spyOn(associationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ association });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(associationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMonnaie', () => {
      it('Should forward to monnaieService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(monnaieService, 'compareMonnaie');
        comp.compareMonnaie(entity, entity2);
        expect(monnaieService.compareMonnaie).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
