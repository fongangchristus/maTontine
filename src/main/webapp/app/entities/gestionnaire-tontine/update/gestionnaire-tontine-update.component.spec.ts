import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GestionnaireTontineFormService } from './gestionnaire-tontine-form.service';
import { GestionnaireTontineService } from '../service/gestionnaire-tontine.service';
import { IGestionnaireTontine } from '../gestionnaire-tontine.model';
import { ITontine } from 'app/entities/tontine/tontine.model';
import { TontineService } from 'app/entities/tontine/service/tontine.service';

import { GestionnaireTontineUpdateComponent } from './gestionnaire-tontine-update.component';

describe('GestionnaireTontine Management Update Component', () => {
  let comp: GestionnaireTontineUpdateComponent;
  let fixture: ComponentFixture<GestionnaireTontineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gestionnaireTontineFormService: GestionnaireTontineFormService;
  let gestionnaireTontineService: GestionnaireTontineService;
  let tontineService: TontineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GestionnaireTontineUpdateComponent],
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
      .overrideTemplate(GestionnaireTontineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GestionnaireTontineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gestionnaireTontineFormService = TestBed.inject(GestionnaireTontineFormService);
    gestionnaireTontineService = TestBed.inject(GestionnaireTontineService);
    tontineService = TestBed.inject(TontineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tontine query and add missing value', () => {
      const gestionnaireTontine: IGestionnaireTontine = { id: 456 };
      const tontine: ITontine = { id: 62985 };
      gestionnaireTontine.tontine = tontine;

      const tontineCollection: ITontine[] = [{ id: 50661 }];
      jest.spyOn(tontineService, 'query').mockReturnValue(of(new HttpResponse({ body: tontineCollection })));
      const additionalTontines = [tontine];
      const expectedCollection: ITontine[] = [...additionalTontines, ...tontineCollection];
      jest.spyOn(tontineService, 'addTontineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gestionnaireTontine });
      comp.ngOnInit();

      expect(tontineService.query).toHaveBeenCalled();
      expect(tontineService.addTontineToCollectionIfMissing).toHaveBeenCalledWith(
        tontineCollection,
        ...additionalTontines.map(expect.objectContaining)
      );
      expect(comp.tontinesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const gestionnaireTontine: IGestionnaireTontine = { id: 456 };
      const tontine: ITontine = { id: 65109 };
      gestionnaireTontine.tontine = tontine;

      activatedRoute.data = of({ gestionnaireTontine });
      comp.ngOnInit();

      expect(comp.tontinesSharedCollection).toContain(tontine);
      expect(comp.gestionnaireTontine).toEqual(gestionnaireTontine);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGestionnaireTontine>>();
      const gestionnaireTontine = { id: 123 };
      jest.spyOn(gestionnaireTontineFormService, 'getGestionnaireTontine').mockReturnValue(gestionnaireTontine);
      jest.spyOn(gestionnaireTontineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gestionnaireTontine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gestionnaireTontine }));
      saveSubject.complete();

      // THEN
      expect(gestionnaireTontineFormService.getGestionnaireTontine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gestionnaireTontineService.update).toHaveBeenCalledWith(expect.objectContaining(gestionnaireTontine));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGestionnaireTontine>>();
      const gestionnaireTontine = { id: 123 };
      jest.spyOn(gestionnaireTontineFormService, 'getGestionnaireTontine').mockReturnValue({ id: null });
      jest.spyOn(gestionnaireTontineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gestionnaireTontine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gestionnaireTontine }));
      saveSubject.complete();

      // THEN
      expect(gestionnaireTontineFormService.getGestionnaireTontine).toHaveBeenCalled();
      expect(gestionnaireTontineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGestionnaireTontine>>();
      const gestionnaireTontine = { id: 123 };
      jest.spyOn(gestionnaireTontineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gestionnaireTontine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gestionnaireTontineService.update).toHaveBeenCalled();
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
