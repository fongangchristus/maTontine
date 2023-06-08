import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CotisationTontineFormService } from './cotisation-tontine-form.service';
import { CotisationTontineService } from '../service/cotisation-tontine.service';
import { ICotisationTontine } from '../cotisation-tontine.model';
import { ISessionTontine } from 'app/entities/session-tontine/session-tontine.model';
import { SessionTontineService } from 'app/entities/session-tontine/service/session-tontine.service';
import { ICompteTontine } from 'app/entities/compte-tontine/compte-tontine.model';
import { CompteTontineService } from 'app/entities/compte-tontine/service/compte-tontine.service';

import { CotisationTontineUpdateComponent } from './cotisation-tontine-update.component';

describe('CotisationTontine Management Update Component', () => {
  let comp: CotisationTontineUpdateComponent;
  let fixture: ComponentFixture<CotisationTontineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cotisationTontineFormService: CotisationTontineFormService;
  let cotisationTontineService: CotisationTontineService;
  let sessionTontineService: SessionTontineService;
  let compteTontineService: CompteTontineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CotisationTontineUpdateComponent],
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
      .overrideTemplate(CotisationTontineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CotisationTontineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cotisationTontineFormService = TestBed.inject(CotisationTontineFormService);
    cotisationTontineService = TestBed.inject(CotisationTontineService);
    sessionTontineService = TestBed.inject(SessionTontineService);
    compteTontineService = TestBed.inject(CompteTontineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SessionTontine query and add missing value', () => {
      const cotisationTontine: ICotisationTontine = { id: 456 };
      const sessionTontine: ISessionTontine = { id: 27759 };
      cotisationTontine.sessionTontine = sessionTontine;

      const sessionTontineCollection: ISessionTontine[] = [{ id: 94361 }];
      jest.spyOn(sessionTontineService, 'query').mockReturnValue(of(new HttpResponse({ body: sessionTontineCollection })));
      const additionalSessionTontines = [sessionTontine];
      const expectedCollection: ISessionTontine[] = [...additionalSessionTontines, ...sessionTontineCollection];
      jest.spyOn(sessionTontineService, 'addSessionTontineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cotisationTontine });
      comp.ngOnInit();

      expect(sessionTontineService.query).toHaveBeenCalled();
      expect(sessionTontineService.addSessionTontineToCollectionIfMissing).toHaveBeenCalledWith(
        sessionTontineCollection,
        ...additionalSessionTontines.map(expect.objectContaining)
      );
      expect(comp.sessionTontinesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CompteTontine query and add missing value', () => {
      const cotisationTontine: ICotisationTontine = { id: 456 };
      const compteTontine: ICompteTontine = { id: 54626 };
      cotisationTontine.compteTontine = compteTontine;

      const compteTontineCollection: ICompteTontine[] = [{ id: 60539 }];
      jest.spyOn(compteTontineService, 'query').mockReturnValue(of(new HttpResponse({ body: compteTontineCollection })));
      const additionalCompteTontines = [compteTontine];
      const expectedCollection: ICompteTontine[] = [...additionalCompteTontines, ...compteTontineCollection];
      jest.spyOn(compteTontineService, 'addCompteTontineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cotisationTontine });
      comp.ngOnInit();

      expect(compteTontineService.query).toHaveBeenCalled();
      expect(compteTontineService.addCompteTontineToCollectionIfMissing).toHaveBeenCalledWith(
        compteTontineCollection,
        ...additionalCompteTontines.map(expect.objectContaining)
      );
      expect(comp.compteTontinesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cotisationTontine: ICotisationTontine = { id: 456 };
      const sessionTontine: ISessionTontine = { id: 69826 };
      cotisationTontine.sessionTontine = sessionTontine;
      const compteTontine: ICompteTontine = { id: 84496 };
      cotisationTontine.compteTontine = compteTontine;

      activatedRoute.data = of({ cotisationTontine });
      comp.ngOnInit();

      expect(comp.sessionTontinesSharedCollection).toContain(sessionTontine);
      expect(comp.compteTontinesSharedCollection).toContain(compteTontine);
      expect(comp.cotisationTontine).toEqual(cotisationTontine);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICotisationTontine>>();
      const cotisationTontine = { id: 123 };
      jest.spyOn(cotisationTontineFormService, 'getCotisationTontine').mockReturnValue(cotisationTontine);
      jest.spyOn(cotisationTontineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cotisationTontine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cotisationTontine }));
      saveSubject.complete();

      // THEN
      expect(cotisationTontineFormService.getCotisationTontine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cotisationTontineService.update).toHaveBeenCalledWith(expect.objectContaining(cotisationTontine));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICotisationTontine>>();
      const cotisationTontine = { id: 123 };
      jest.spyOn(cotisationTontineFormService, 'getCotisationTontine').mockReturnValue({ id: null });
      jest.spyOn(cotisationTontineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cotisationTontine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cotisationTontine }));
      saveSubject.complete();

      // THEN
      expect(cotisationTontineFormService.getCotisationTontine).toHaveBeenCalled();
      expect(cotisationTontineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICotisationTontine>>();
      const cotisationTontine = { id: 123 };
      jest.spyOn(cotisationTontineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cotisationTontine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cotisationTontineService.update).toHaveBeenCalled();
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
