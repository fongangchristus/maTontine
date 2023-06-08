import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CotisationBanqueFormService } from './cotisation-banque-form.service';
import { CotisationBanqueService } from '../service/cotisation-banque.service';
import { ICotisationBanque } from '../cotisation-banque.model';
import { ICompteBanque } from 'app/entities/compte-banque/compte-banque.model';
import { CompteBanqueService } from 'app/entities/compte-banque/service/compte-banque.service';

import { CotisationBanqueUpdateComponent } from './cotisation-banque-update.component';

describe('CotisationBanque Management Update Component', () => {
  let comp: CotisationBanqueUpdateComponent;
  let fixture: ComponentFixture<CotisationBanqueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cotisationBanqueFormService: CotisationBanqueFormService;
  let cotisationBanqueService: CotisationBanqueService;
  let compteBanqueService: CompteBanqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CotisationBanqueUpdateComponent],
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
      .overrideTemplate(CotisationBanqueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CotisationBanqueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cotisationBanqueFormService = TestBed.inject(CotisationBanqueFormService);
    cotisationBanqueService = TestBed.inject(CotisationBanqueService);
    compteBanqueService = TestBed.inject(CompteBanqueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CompteBanque query and add missing value', () => {
      const cotisationBanque: ICotisationBanque = { id: 456 };
      const compteBanque: ICompteBanque = { id: 5277 };
      cotisationBanque.compteBanque = compteBanque;

      const compteBanqueCollection: ICompteBanque[] = [{ id: 26267 }];
      jest.spyOn(compteBanqueService, 'query').mockReturnValue(of(new HttpResponse({ body: compteBanqueCollection })));
      const additionalCompteBanques = [compteBanque];
      const expectedCollection: ICompteBanque[] = [...additionalCompteBanques, ...compteBanqueCollection];
      jest.spyOn(compteBanqueService, 'addCompteBanqueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cotisationBanque });
      comp.ngOnInit();

      expect(compteBanqueService.query).toHaveBeenCalled();
      expect(compteBanqueService.addCompteBanqueToCollectionIfMissing).toHaveBeenCalledWith(
        compteBanqueCollection,
        ...additionalCompteBanques.map(expect.objectContaining)
      );
      expect(comp.compteBanquesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cotisationBanque: ICotisationBanque = { id: 456 };
      const compteBanque: ICompteBanque = { id: 95132 };
      cotisationBanque.compteBanque = compteBanque;

      activatedRoute.data = of({ cotisationBanque });
      comp.ngOnInit();

      expect(comp.compteBanquesSharedCollection).toContain(compteBanque);
      expect(comp.cotisationBanque).toEqual(cotisationBanque);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICotisationBanque>>();
      const cotisationBanque = { id: 123 };
      jest.spyOn(cotisationBanqueFormService, 'getCotisationBanque').mockReturnValue(cotisationBanque);
      jest.spyOn(cotisationBanqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cotisationBanque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cotisationBanque }));
      saveSubject.complete();

      // THEN
      expect(cotisationBanqueFormService.getCotisationBanque).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cotisationBanqueService.update).toHaveBeenCalledWith(expect.objectContaining(cotisationBanque));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICotisationBanque>>();
      const cotisationBanque = { id: 123 };
      jest.spyOn(cotisationBanqueFormService, 'getCotisationBanque').mockReturnValue({ id: null });
      jest.spyOn(cotisationBanqueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cotisationBanque: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cotisationBanque }));
      saveSubject.complete();

      // THEN
      expect(cotisationBanqueFormService.getCotisationBanque).toHaveBeenCalled();
      expect(cotisationBanqueService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICotisationBanque>>();
      const cotisationBanque = { id: 123 };
      jest.spyOn(cotisationBanqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cotisationBanque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cotisationBanqueService.update).toHaveBeenCalled();
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
