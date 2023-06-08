import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompteTontineFormService } from './compte-tontine-form.service';
import { CompteTontineService } from '../service/compte-tontine.service';
import { ICompteTontine } from '../compte-tontine.model';
import { ITontine } from 'app/entities/tontine/tontine.model';
import { TontineService } from 'app/entities/tontine/service/tontine.service';

import { CompteTontineUpdateComponent } from './compte-tontine-update.component';

describe('CompteTontine Management Update Component', () => {
  let comp: CompteTontineUpdateComponent;
  let fixture: ComponentFixture<CompteTontineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let compteTontineFormService: CompteTontineFormService;
  let compteTontineService: CompteTontineService;
  let tontineService: TontineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompteTontineUpdateComponent],
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
      .overrideTemplate(CompteTontineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompteTontineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    compteTontineFormService = TestBed.inject(CompteTontineFormService);
    compteTontineService = TestBed.inject(CompteTontineService);
    tontineService = TestBed.inject(TontineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tontine query and add missing value', () => {
      const compteTontine: ICompteTontine = { id: 456 };
      const tontine: ITontine = { id: 29230 };
      compteTontine.tontine = tontine;

      const tontineCollection: ITontine[] = [{ id: 41087 }];
      jest.spyOn(tontineService, 'query').mockReturnValue(of(new HttpResponse({ body: tontineCollection })));
      const additionalTontines = [tontine];
      const expectedCollection: ITontine[] = [...additionalTontines, ...tontineCollection];
      jest.spyOn(tontineService, 'addTontineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ compteTontine });
      comp.ngOnInit();

      expect(tontineService.query).toHaveBeenCalled();
      expect(tontineService.addTontineToCollectionIfMissing).toHaveBeenCalledWith(
        tontineCollection,
        ...additionalTontines.map(expect.objectContaining)
      );
      expect(comp.tontinesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const compteTontine: ICompteTontine = { id: 456 };
      const tontine: ITontine = { id: 88394 };
      compteTontine.tontine = tontine;

      activatedRoute.data = of({ compteTontine });
      comp.ngOnInit();

      expect(comp.tontinesSharedCollection).toContain(tontine);
      expect(comp.compteTontine).toEqual(compteTontine);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompteTontine>>();
      const compteTontine = { id: 123 };
      jest.spyOn(compteTontineFormService, 'getCompteTontine').mockReturnValue(compteTontine);
      jest.spyOn(compteTontineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compteTontine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compteTontine }));
      saveSubject.complete();

      // THEN
      expect(compteTontineFormService.getCompteTontine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(compteTontineService.update).toHaveBeenCalledWith(expect.objectContaining(compteTontine));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompteTontine>>();
      const compteTontine = { id: 123 };
      jest.spyOn(compteTontineFormService, 'getCompteTontine').mockReturnValue({ id: null });
      jest.spyOn(compteTontineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compteTontine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compteTontine }));
      saveSubject.complete();

      // THEN
      expect(compteTontineFormService.getCompteTontine).toHaveBeenCalled();
      expect(compteTontineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompteTontine>>();
      const compteTontine = { id: 123 };
      jest.spyOn(compteTontineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compteTontine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(compteTontineService.update).toHaveBeenCalled();
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
