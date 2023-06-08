import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GestionnaireBanqueFormService } from './gestionnaire-banque-form.service';
import { GestionnaireBanqueService } from '../service/gestionnaire-banque.service';
import { IGestionnaireBanque } from '../gestionnaire-banque.model';
import { IBanque } from 'app/entities/banque/banque.model';
import { BanqueService } from 'app/entities/banque/service/banque.service';

import { GestionnaireBanqueUpdateComponent } from './gestionnaire-banque-update.component';

describe('GestionnaireBanque Management Update Component', () => {
  let comp: GestionnaireBanqueUpdateComponent;
  let fixture: ComponentFixture<GestionnaireBanqueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gestionnaireBanqueFormService: GestionnaireBanqueFormService;
  let gestionnaireBanqueService: GestionnaireBanqueService;
  let banqueService: BanqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GestionnaireBanqueUpdateComponent],
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
      .overrideTemplate(GestionnaireBanqueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GestionnaireBanqueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gestionnaireBanqueFormService = TestBed.inject(GestionnaireBanqueFormService);
    gestionnaireBanqueService = TestBed.inject(GestionnaireBanqueService);
    banqueService = TestBed.inject(BanqueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Banque query and add missing value', () => {
      const gestionnaireBanque: IGestionnaireBanque = { id: 456 };
      const banque: IBanque = { id: 13056 };
      gestionnaireBanque.banque = banque;

      const banqueCollection: IBanque[] = [{ id: 39229 }];
      jest.spyOn(banqueService, 'query').mockReturnValue(of(new HttpResponse({ body: banqueCollection })));
      const additionalBanques = [banque];
      const expectedCollection: IBanque[] = [...additionalBanques, ...banqueCollection];
      jest.spyOn(banqueService, 'addBanqueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gestionnaireBanque });
      comp.ngOnInit();

      expect(banqueService.query).toHaveBeenCalled();
      expect(banqueService.addBanqueToCollectionIfMissing).toHaveBeenCalledWith(
        banqueCollection,
        ...additionalBanques.map(expect.objectContaining)
      );
      expect(comp.banquesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const gestionnaireBanque: IGestionnaireBanque = { id: 456 };
      const banque: IBanque = { id: 10631 };
      gestionnaireBanque.banque = banque;

      activatedRoute.data = of({ gestionnaireBanque });
      comp.ngOnInit();

      expect(comp.banquesSharedCollection).toContain(banque);
      expect(comp.gestionnaireBanque).toEqual(gestionnaireBanque);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGestionnaireBanque>>();
      const gestionnaireBanque = { id: 123 };
      jest.spyOn(gestionnaireBanqueFormService, 'getGestionnaireBanque').mockReturnValue(gestionnaireBanque);
      jest.spyOn(gestionnaireBanqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gestionnaireBanque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gestionnaireBanque }));
      saveSubject.complete();

      // THEN
      expect(gestionnaireBanqueFormService.getGestionnaireBanque).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gestionnaireBanqueService.update).toHaveBeenCalledWith(expect.objectContaining(gestionnaireBanque));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGestionnaireBanque>>();
      const gestionnaireBanque = { id: 123 };
      jest.spyOn(gestionnaireBanqueFormService, 'getGestionnaireBanque').mockReturnValue({ id: null });
      jest.spyOn(gestionnaireBanqueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gestionnaireBanque: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gestionnaireBanque }));
      saveSubject.complete();

      // THEN
      expect(gestionnaireBanqueFormService.getGestionnaireBanque).toHaveBeenCalled();
      expect(gestionnaireBanqueService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGestionnaireBanque>>();
      const gestionnaireBanque = { id: 123 };
      jest.spyOn(gestionnaireBanqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gestionnaireBanque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gestionnaireBanqueService.update).toHaveBeenCalled();
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
