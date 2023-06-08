import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DecaissementTontineFormService } from './decaissement-tontine-form.service';
import { DecaissementTontineService } from '../service/decaissement-tontine.service';
import { IDecaissementTontine } from '../decaissement-tontine.model';
import { ISessionTontine } from 'app/entities/session-tontine/session-tontine.model';
import { SessionTontineService } from 'app/entities/session-tontine/service/session-tontine.service';
import { ICompteTontine } from 'app/entities/compte-tontine/compte-tontine.model';
import { CompteTontineService } from 'app/entities/compte-tontine/service/compte-tontine.service';

import { DecaissementTontineUpdateComponent } from './decaissement-tontine-update.component';

describe('DecaissementTontine Management Update Component', () => {
  let comp: DecaissementTontineUpdateComponent;
  let fixture: ComponentFixture<DecaissementTontineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let decaissementTontineFormService: DecaissementTontineFormService;
  let decaissementTontineService: DecaissementTontineService;
  let sessionTontineService: SessionTontineService;
  let compteTontineService: CompteTontineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DecaissementTontineUpdateComponent],
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
      .overrideTemplate(DecaissementTontineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DecaissementTontineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    decaissementTontineFormService = TestBed.inject(DecaissementTontineFormService);
    decaissementTontineService = TestBed.inject(DecaissementTontineService);
    sessionTontineService = TestBed.inject(SessionTontineService);
    compteTontineService = TestBed.inject(CompteTontineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SessionTontine query and add missing value', () => {
      const decaissementTontine: IDecaissementTontine = { id: 456 };
      const tontine: ISessionTontine = { id: 44722 };
      decaissementTontine.tontine = tontine;

      const sessionTontineCollection: ISessionTontine[] = [{ id: 30504 }];
      jest.spyOn(sessionTontineService, 'query').mockReturnValue(of(new HttpResponse({ body: sessionTontineCollection })));
      const additionalSessionTontines = [tontine];
      const expectedCollection: ISessionTontine[] = [...additionalSessionTontines, ...sessionTontineCollection];
      jest.spyOn(sessionTontineService, 'addSessionTontineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ decaissementTontine });
      comp.ngOnInit();

      expect(sessionTontineService.query).toHaveBeenCalled();
      expect(sessionTontineService.addSessionTontineToCollectionIfMissing).toHaveBeenCalledWith(
        sessionTontineCollection,
        ...additionalSessionTontines.map(expect.objectContaining)
      );
      expect(comp.sessionTontinesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CompteTontine query and add missing value', () => {
      const decaissementTontine: IDecaissementTontine = { id: 456 };
      const compteTontine: ICompteTontine = { id: 88889 };
      decaissementTontine.compteTontine = compteTontine;

      const compteTontineCollection: ICompteTontine[] = [{ id: 95745 }];
      jest.spyOn(compteTontineService, 'query').mockReturnValue(of(new HttpResponse({ body: compteTontineCollection })));
      const additionalCompteTontines = [compteTontine];
      const expectedCollection: ICompteTontine[] = [...additionalCompteTontines, ...compteTontineCollection];
      jest.spyOn(compteTontineService, 'addCompteTontineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ decaissementTontine });
      comp.ngOnInit();

      expect(compteTontineService.query).toHaveBeenCalled();
      expect(compteTontineService.addCompteTontineToCollectionIfMissing).toHaveBeenCalledWith(
        compteTontineCollection,
        ...additionalCompteTontines.map(expect.objectContaining)
      );
      expect(comp.compteTontinesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const decaissementTontine: IDecaissementTontine = { id: 456 };
      const tontine: ISessionTontine = { id: 97142 };
      decaissementTontine.tontine = tontine;
      const compteTontine: ICompteTontine = { id: 28276 };
      decaissementTontine.compteTontine = compteTontine;

      activatedRoute.data = of({ decaissementTontine });
      comp.ngOnInit();

      expect(comp.sessionTontinesSharedCollection).toContain(tontine);
      expect(comp.compteTontinesSharedCollection).toContain(compteTontine);
      expect(comp.decaissementTontine).toEqual(decaissementTontine);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDecaissementTontine>>();
      const decaissementTontine = { id: 123 };
      jest.spyOn(decaissementTontineFormService, 'getDecaissementTontine').mockReturnValue(decaissementTontine);
      jest.spyOn(decaissementTontineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ decaissementTontine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: decaissementTontine }));
      saveSubject.complete();

      // THEN
      expect(decaissementTontineFormService.getDecaissementTontine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(decaissementTontineService.update).toHaveBeenCalledWith(expect.objectContaining(decaissementTontine));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDecaissementTontine>>();
      const decaissementTontine = { id: 123 };
      jest.spyOn(decaissementTontineFormService, 'getDecaissementTontine').mockReturnValue({ id: null });
      jest.spyOn(decaissementTontineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ decaissementTontine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: decaissementTontine }));
      saveSubject.complete();

      // THEN
      expect(decaissementTontineFormService.getDecaissementTontine).toHaveBeenCalled();
      expect(decaissementTontineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDecaissementTontine>>();
      const decaissementTontine = { id: 123 };
      jest.spyOn(decaissementTontineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ decaissementTontine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(decaissementTontineService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSessionTontine', () => {
      it('Should forward to sessionTontineService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sessionTontineService, 'compareSessionTontine');
        comp.compareSessionTontine(entity, entity2);
        expect(sessionTontineService.compareSessionTontine).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCompteTontine', () => {
      it('Should forward to compteTontineService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(compteTontineService, 'compareCompteTontine');
        comp.compareCompteTontine(entity, entity2);
        expect(compteTontineService.compareCompteTontine).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
