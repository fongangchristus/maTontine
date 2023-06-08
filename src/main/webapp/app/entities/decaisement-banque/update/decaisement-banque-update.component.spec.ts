import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DecaisementBanqueFormService } from './decaisement-banque-form.service';
import { DecaisementBanqueService } from '../service/decaisement-banque.service';
import { IDecaisementBanque } from '../decaisement-banque.model';
import { ICompteBanque } from 'app/entities/compte-banque/compte-banque.model';
import { CompteBanqueService } from 'app/entities/compte-banque/service/compte-banque.service';

import { DecaisementBanqueUpdateComponent } from './decaisement-banque-update.component';

describe('DecaisementBanque Management Update Component', () => {
  let comp: DecaisementBanqueUpdateComponent;
  let fixture: ComponentFixture<DecaisementBanqueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let decaisementBanqueFormService: DecaisementBanqueFormService;
  let decaisementBanqueService: DecaisementBanqueService;
  let compteBanqueService: CompteBanqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DecaisementBanqueUpdateComponent],
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
      .overrideTemplate(DecaisementBanqueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DecaisementBanqueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    decaisementBanqueFormService = TestBed.inject(DecaisementBanqueFormService);
    decaisementBanqueService = TestBed.inject(DecaisementBanqueService);
    compteBanqueService = TestBed.inject(CompteBanqueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CompteBanque query and add missing value', () => {
      const decaisementBanque: IDecaisementBanque = { id: 456 };
      const compteBanque: ICompteBanque = { id: 87320 };
      decaisementBanque.compteBanque = compteBanque;

      const compteBanqueCollection: ICompteBanque[] = [{ id: 83313 }];
      jest.spyOn(compteBanqueService, 'query').mockReturnValue(of(new HttpResponse({ body: compteBanqueCollection })));
      const additionalCompteBanques = [compteBanque];
      const expectedCollection: ICompteBanque[] = [...additionalCompteBanques, ...compteBanqueCollection];
      jest.spyOn(compteBanqueService, 'addCompteBanqueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ decaisementBanque });
      comp.ngOnInit();

      expect(compteBanqueService.query).toHaveBeenCalled();
      expect(compteBanqueService.addCompteBanqueToCollectionIfMissing).toHaveBeenCalledWith(
        compteBanqueCollection,
        ...additionalCompteBanques.map(expect.objectContaining)
      );
      expect(comp.compteBanquesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const decaisementBanque: IDecaisementBanque = { id: 456 };
      const compteBanque: ICompteBanque = { id: 61664 };
      decaisementBanque.compteBanque = compteBanque;

      activatedRoute.data = of({ decaisementBanque });
      comp.ngOnInit();

      expect(comp.compteBanquesSharedCollection).toContain(compteBanque);
      expect(comp.decaisementBanque).toEqual(decaisementBanque);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDecaisementBanque>>();
      const decaisementBanque = { id: 123 };
      jest.spyOn(decaisementBanqueFormService, 'getDecaisementBanque').mockReturnValue(decaisementBanque);
      jest.spyOn(decaisementBanqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ decaisementBanque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: decaisementBanque }));
      saveSubject.complete();

      // THEN
      expect(decaisementBanqueFormService.getDecaisementBanque).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(decaisementBanqueService.update).toHaveBeenCalledWith(expect.objectContaining(decaisementBanque));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDecaisementBanque>>();
      const decaisementBanque = { id: 123 };
      jest.spyOn(decaisementBanqueFormService, 'getDecaisementBanque').mockReturnValue({ id: null });
      jest.spyOn(decaisementBanqueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ decaisementBanque: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: decaisementBanque }));
      saveSubject.complete();

      // THEN
      expect(decaisementBanqueFormService.getDecaisementBanque).toHaveBeenCalled();
      expect(decaisementBanqueService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDecaisementBanque>>();
      const decaisementBanque = { id: 123 };
      jest.spyOn(decaisementBanqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ decaisementBanque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(decaisementBanqueService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCompteBanque', () => {
      it('Should forward to compteBanqueService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(compteBanqueService, 'compareCompteBanque');
        comp.compareCompteBanque(entity, entity2);
        expect(compteBanqueService.compareCompteBanque).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
