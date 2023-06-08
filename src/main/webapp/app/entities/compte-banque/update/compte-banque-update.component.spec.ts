import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompteBanqueFormService } from './compte-banque-form.service';
import { CompteBanqueService } from '../service/compte-banque.service';
import { ICompteBanque } from '../compte-banque.model';
import { IBanque } from 'app/entities/banque/banque.model';
import { BanqueService } from 'app/entities/banque/service/banque.service';

import { CompteBanqueUpdateComponent } from './compte-banque-update.component';

describe('CompteBanque Management Update Component', () => {
  let comp: CompteBanqueUpdateComponent;
  let fixture: ComponentFixture<CompteBanqueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let compteBanqueFormService: CompteBanqueFormService;
  let compteBanqueService: CompteBanqueService;
  let banqueService: BanqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompteBanqueUpdateComponent],
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
      .overrideTemplate(CompteBanqueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompteBanqueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    compteBanqueFormService = TestBed.inject(CompteBanqueFormService);
    compteBanqueService = TestBed.inject(CompteBanqueService);
    banqueService = TestBed.inject(BanqueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Banque query and add missing value', () => {
      const compteBanque: ICompteBanque = { id: 456 };
      const banque: IBanque = { id: 44047 };
      compteBanque.banque = banque;

      const banqueCollection: IBanque[] = [{ id: 77816 }];
      jest.spyOn(banqueService, 'query').mockReturnValue(of(new HttpResponse({ body: banqueCollection })));
      const additionalBanques = [banque];
      const expectedCollection: IBanque[] = [...additionalBanques, ...banqueCollection];
      jest.spyOn(banqueService, 'addBanqueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ compteBanque });
      comp.ngOnInit();

      expect(banqueService.query).toHaveBeenCalled();
      expect(banqueService.addBanqueToCollectionIfMissing).toHaveBeenCalledWith(
        banqueCollection,
        ...additionalBanques.map(expect.objectContaining)
      );
      expect(comp.banquesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const compteBanque: ICompteBanque = { id: 456 };
      const banque: IBanque = { id: 63272 };
      compteBanque.banque = banque;

      activatedRoute.data = of({ compteBanque });
      comp.ngOnInit();

      expect(comp.banquesSharedCollection).toContain(banque);
      expect(comp.compteBanque).toEqual(compteBanque);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompteBanque>>();
      const compteBanque = { id: 123 };
      jest.spyOn(compteBanqueFormService, 'getCompteBanque').mockReturnValue(compteBanque);
      jest.spyOn(compteBanqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compteBanque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compteBanque }));
      saveSubject.complete();

      // THEN
      expect(compteBanqueFormService.getCompteBanque).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(compteBanqueService.update).toHaveBeenCalledWith(expect.objectContaining(compteBanque));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompteBanque>>();
      const compteBanque = { id: 123 };
      jest.spyOn(compteBanqueFormService, 'getCompteBanque').mockReturnValue({ id: null });
      jest.spyOn(compteBanqueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compteBanque: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compteBanque }));
      saveSubject.complete();

      // THEN
      expect(compteBanqueFormService.getCompteBanque).toHaveBeenCalled();
      expect(compteBanqueService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompteBanque>>();
      const compteBanque = { id: 123 };
      jest.spyOn(compteBanqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compteBanque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(compteBanqueService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBanque', () => {
      it('Should forward to banqueService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(banqueService, 'compareBanque');
        comp.compareBanque(entity, entity2);
        expect(banqueService.compareBanque).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
