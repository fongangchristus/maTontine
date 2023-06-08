import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PaiementSanctionFormService } from './paiement-sanction-form.service';
import { PaiementSanctionService } from '../service/paiement-sanction.service';
import { IPaiementSanction } from '../paiement-sanction.model';
import { ISanction } from 'app/entities/sanction/sanction.model';
import { SanctionService } from 'app/entities/sanction/service/sanction.service';

import { PaiementSanctionUpdateComponent } from './paiement-sanction-update.component';

describe('PaiementSanction Management Update Component', () => {
  let comp: PaiementSanctionUpdateComponent;
  let fixture: ComponentFixture<PaiementSanctionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paiementSanctionFormService: PaiementSanctionFormService;
  let paiementSanctionService: PaiementSanctionService;
  let sanctionService: SanctionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PaiementSanctionUpdateComponent],
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
      .overrideTemplate(PaiementSanctionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaiementSanctionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paiementSanctionFormService = TestBed.inject(PaiementSanctionFormService);
    paiementSanctionService = TestBed.inject(PaiementSanctionService);
    sanctionService = TestBed.inject(SanctionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sanction query and add missing value', () => {
      const paiementSanction: IPaiementSanction = { id: 456 };
      const sanction: ISanction = { id: 28352 };
      paiementSanction.sanction = sanction;

      const sanctionCollection: ISanction[] = [{ id: 14092 }];
      jest.spyOn(sanctionService, 'query').mockReturnValue(of(new HttpResponse({ body: sanctionCollection })));
      const additionalSanctions = [sanction];
      const expectedCollection: ISanction[] = [...additionalSanctions, ...sanctionCollection];
      jest.spyOn(sanctionService, 'addSanctionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paiementSanction });
      comp.ngOnInit();

      expect(sanctionService.query).toHaveBeenCalled();
      expect(sanctionService.addSanctionToCollectionIfMissing).toHaveBeenCalledWith(
        sanctionCollection,
        ...additionalSanctions.map(expect.objectContaining)
      );
      expect(comp.sanctionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const paiementSanction: IPaiementSanction = { id: 456 };
      const sanction: ISanction = { id: 3400 };
      paiementSanction.sanction = sanction;

      activatedRoute.data = of({ paiementSanction });
      comp.ngOnInit();

      expect(comp.sanctionsSharedCollection).toContain(sanction);
      expect(comp.paiementSanction).toEqual(paiementSanction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementSanction>>();
      const paiementSanction = { id: 123 };
      jest.spyOn(paiementSanctionFormService, 'getPaiementSanction').mockReturnValue(paiementSanction);
      jest.spyOn(paiementSanctionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementSanction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiementSanction }));
      saveSubject.complete();

      // THEN
      expect(paiementSanctionFormService.getPaiementSanction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paiementSanctionService.update).toHaveBeenCalledWith(expect.objectContaining(paiementSanction));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementSanction>>();
      const paiementSanction = { id: 123 };
      jest.spyOn(paiementSanctionFormService, 'getPaiementSanction').mockReturnValue({ id: null });
      jest.spyOn(paiementSanctionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementSanction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiementSanction }));
      saveSubject.complete();

      // THEN
      expect(paiementSanctionFormService.getPaiementSanction).toHaveBeenCalled();
      expect(paiementSanctionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementSanction>>();
      const paiementSanction = { id: 123 };
      jest.spyOn(paiementSanctionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementSanction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paiementSanctionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSanction', () => {
      it('Should forward to sanctionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sanctionService, 'compareSanction');
        comp.compareSanction(entity, entity2);
        expect(sanctionService.compareSanction).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
