import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SessionTontineFormService } from './session-tontine-form.service';
import { SessionTontineService } from '../service/session-tontine.service';
import { ISessionTontine } from '../session-tontine.model';
import { ITontine } from 'app/entities/tontine/tontine.model';
import { TontineService } from 'app/entities/tontine/service/tontine.service';

import { SessionTontineUpdateComponent } from './session-tontine-update.component';

describe('SessionTontine Management Update Component', () => {
  let comp: SessionTontineUpdateComponent;
  let fixture: ComponentFixture<SessionTontineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sessionTontineFormService: SessionTontineFormService;
  let sessionTontineService: SessionTontineService;
  let tontineService: TontineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SessionTontineUpdateComponent],
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
      .overrideTemplate(SessionTontineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SessionTontineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sessionTontineFormService = TestBed.inject(SessionTontineFormService);
    sessionTontineService = TestBed.inject(SessionTontineService);
    tontineService = TestBed.inject(TontineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tontine query and add missing value', () => {
      const sessionTontine: ISessionTontine = { id: 456 };
      const tontine: ITontine = { id: 25021 };
      sessionTontine.tontine = tontine;

      const tontineCollection: ITontine[] = [{ id: 80860 }];
      jest.spyOn(tontineService, 'query').mockReturnValue(of(new HttpResponse({ body: tontineCollection })));
      const additionalTontines = [tontine];
      const expectedCollection: ITontine[] = [...additionalTontines, ...tontineCollection];
      jest.spyOn(tontineService, 'addTontineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sessionTontine });
      comp.ngOnInit();

      expect(tontineService.query).toHaveBeenCalled();
      expect(tontineService.addTontineToCollectionIfMissing).toHaveBeenCalledWith(
        tontineCollection,
        ...additionalTontines.map(expect.objectContaining)
      );
      expect(comp.tontinesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sessionTontine: ISessionTontine = { id: 456 };
      const tontine: ITontine = { id: 84019 };
      sessionTontine.tontine = tontine;

      activatedRoute.data = of({ sessionTontine });
      comp.ngOnInit();

      expect(comp.tontinesSharedCollection).toContain(tontine);
      expect(comp.sessionTontine).toEqual(sessionTontine);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISessionTontine>>();
      const sessionTontine = { id: 123 };
      jest.spyOn(sessionTontineFormService, 'getSessionTontine').mockReturnValue(sessionTontine);
      jest.spyOn(sessionTontineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sessionTontine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sessionTontine }));
      saveSubject.complete();

      // THEN
      expect(sessionTontineFormService.getSessionTontine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sessionTontineService.update).toHaveBeenCalledWith(expect.objectContaining(sessionTontine));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISessionTontine>>();
      const sessionTontine = { id: 123 };
      jest.spyOn(sessionTontineFormService, 'getSessionTontine').mockReturnValue({ id: null });
      jest.spyOn(sessionTontineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sessionTontine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sessionTontine }));
      saveSubject.complete();

      // THEN
      expect(sessionTontineFormService.getSessionTontine).toHaveBeenCalled();
      expect(sessionTontineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISessionTontine>>();
      const sessionTontine = { id: 123 };
      jest.spyOn(sessionTontineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sessionTontine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sessionTontineService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTontine', () => {
      it('Should forward to tontineService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tontineService, 'compareTontine');
        comp.compareTontine(entity, entity2);
        expect(tontineService.compareTontine).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
