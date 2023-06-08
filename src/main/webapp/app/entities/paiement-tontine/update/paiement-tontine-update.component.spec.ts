import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PaiementTontineFormService } from './paiement-tontine-form.service';
import { PaiementTontineService } from '../service/paiement-tontine.service';
import { IPaiementTontine } from '../paiement-tontine.model';
import { ICotisationTontine } from 'app/entities/cotisation-tontine/cotisation-tontine.model';
import { CotisationTontineService } from 'app/entities/cotisation-tontine/service/cotisation-tontine.service';
import { IDecaissementTontine } from 'app/entities/decaissement-tontine/decaissement-tontine.model';
import { DecaissementTontineService } from 'app/entities/decaissement-tontine/service/decaissement-tontine.service';

import { PaiementTontineUpdateComponent } from './paiement-tontine-update.component';

describe('PaiementTontine Management Update Component', () => {
  let comp: PaiementTontineUpdateComponent;
  let fixture: ComponentFixture<PaiementTontineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paiementTontineFormService: PaiementTontineFormService;
  let paiementTontineService: PaiementTontineService;
  let cotisationTontineService: CotisationTontineService;
  let decaissementTontineService: DecaissementTontineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PaiementTontineUpdateComponent],
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
      .overrideTemplate(PaiementTontineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaiementTontineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paiementTontineFormService = TestBed.inject(PaiementTontineFormService);
    paiementTontineService = TestBed.inject(PaiementTontineService);
    cotisationTontineService = TestBed.inject(CotisationTontineService);
    decaissementTontineService = TestBed.inject(DecaissementTontineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CotisationTontine query and add missing value', () => {
      const paiementTontine: IPaiementTontine = { id: 456 };
      const cotisationTontine: ICotisationTontine = { id: 47373 };
      paiementTontine.cotisationTontine = cotisationTontine;

      const cotisationTontineCollection: ICotisationTontine[] = [{ id: 71895 }];
      jest.spyOn(cotisationTontineService, 'query').mockReturnValue(of(new HttpResponse({ body: cotisationTontineCollection })));
      const additionalCotisationTontines = [cotisationTontine];
      const expectedCollection: ICotisationTontine[] = [...additionalCotisationTontines, ...cotisationTontineCollection];
      jest.spyOn(cotisationTontineService, 'addCotisationTontineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paiementTontine });
      comp.ngOnInit();

      expect(cotisationTontineService.query).toHaveBeenCalled();
      expect(cotisationTontineService.addCotisationTontineToCollectionIfMissing).toHaveBeenCalledWith(
        cotisationTontineCollection,
        ...additionalCotisationTontines.map(expect.objectContaining)
      );
      expect(comp.cotisationTontinesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DecaissementTontine query and add missing value', () => {
      const paiementTontine: IPaiementTontine = { id: 456 };
      const decaissementTontine: IDecaissementTontine = { id: 80539 };
      paiementTontine.decaissementTontine = decaissementTontine;

      const decaissementTontineCollection: IDecaissementTontine[] = [{ id: 7915 }];
      jest.spyOn(decaissementTontineService, 'query').mockReturnValue(of(new HttpResponse({ body: decaissementTontineCollection })));
      const additionalDecaissementTontines = [decaissementTontine];
      const expectedCollection: IDecaissementTontine[] = [...additionalDecaissementTontines, ...decaissementTontineCollection];
      jest.spyOn(decaissementTontineService, 'addDecaissementTontineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paiementTontine });
      comp.ngOnInit();

      expect(decaissementTontineService.query).toHaveBeenCalled();
      expect(decaissementTontineService.addDecaissementTontineToCollectionIfMissing).toHaveBeenCalledWith(
        decaissementTontineCollection,
        ...additionalDecaissementTontines.map(expect.objectContaining)
      );
      expect(comp.decaissementTontinesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const paiementTontine: IPaiementTontine = { id: 456 };
      const cotisationTontine: ICotisationTontine = { id: 92334 };
      paiementTontine.cotisationTontine = cotisationTontine;
      const decaissementTontine: IDecaissementTontine = { id: 6519 };
      paiementTontine.decaissementTontine = decaissementTontine;

      activatedRoute.data = of({ paiementTontine });
      comp.ngOnInit();

      expect(comp.cotisationTontinesSharedCollection).toContain(cotisationTontine);
      expect(comp.decaissementTontinesSharedCollection).toContain(decaissementTontine);
      expect(comp.paiementTontine).toEqual(paiementTontine);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementTontine>>();
      const paiementTontine = { id: 123 };
      jest.spyOn(paiementTontineFormService, 'getPaiementTontine').mockReturnValue(paiementTontine);
      jest.spyOn(paiementTontineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementTontine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiementTontine }));
      saveSubject.complete();

      // THEN
      expect(paiementTontineFormService.getPaiementTontine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paiementTontineService.update).toHaveBeenCalledWith(expect.objectContaining(paiementTontine));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementTontine>>();
      const paiementTontine = { id: 123 };
      jest.spyOn(paiementTontineFormService, 'getPaiementTontine').mockReturnValue({ id: null });
      jest.spyOn(paiementTontineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementTontine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiementTontine }));
      saveSubject.complete();

      // THEN
      expect(paiementTontineFormService.getPaiementTontine).toHaveBeenCalled();
      expect(paiementTontineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementTontine>>();
      const paiementTontine = { id: 123 };
      jest.spyOn(paiementTontineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementTontine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paiementTontineService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCotisationTontine', () => {
      it('Should forward to cotisationTontineService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cotisationTontineService, 'compareCotisationTontine');
        comp.compareCotisationTontine(entity, entity2);
        expect(cotisationTontineService.compareCotisationTontine).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDecaissementTontine', () => {
      it('Should forward to decaissementTontineService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(decaissementTontineService, 'compareDecaissementTontine');
        comp.compareDecaissementTontine(entity, entity2);
        expect(decaissementTontineService.compareDecaissementTontine).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
