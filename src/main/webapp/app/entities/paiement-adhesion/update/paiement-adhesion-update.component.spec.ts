import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PaiementAdhesionFormService } from './paiement-adhesion-form.service';
import { PaiementAdhesionService } from '../service/paiement-adhesion.service';
import { IPaiementAdhesion } from '../paiement-adhesion.model';
import { IAdhesion } from 'app/entities/adhesion/adhesion.model';
import { AdhesionService } from 'app/entities/adhesion/service/adhesion.service';

import { PaiementAdhesionUpdateComponent } from './paiement-adhesion-update.component';

describe('PaiementAdhesion Management Update Component', () => {
  let comp: PaiementAdhesionUpdateComponent;
  let fixture: ComponentFixture<PaiementAdhesionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paiementAdhesionFormService: PaiementAdhesionFormService;
  let paiementAdhesionService: PaiementAdhesionService;
  let adhesionService: AdhesionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PaiementAdhesionUpdateComponent],
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
      .overrideTemplate(PaiementAdhesionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaiementAdhesionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paiementAdhesionFormService = TestBed.inject(PaiementAdhesionFormService);
    paiementAdhesionService = TestBed.inject(PaiementAdhesionService);
    adhesionService = TestBed.inject(AdhesionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Adhesion query and add missing value', () => {
      const paiementAdhesion: IPaiementAdhesion = { id: 456 };
      const adhesion: IAdhesion = { id: 97352 };
      paiementAdhesion.adhesion = adhesion;

      const adhesionCollection: IAdhesion[] = [{ id: 57808 }];
      jest.spyOn(adhesionService, 'query').mockReturnValue(of(new HttpResponse({ body: adhesionCollection })));
      const additionalAdhesions = [adhesion];
      const expectedCollection: IAdhesion[] = [...additionalAdhesions, ...adhesionCollection];
      jest.spyOn(adhesionService, 'addAdhesionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paiementAdhesion });
      comp.ngOnInit();

      expect(adhesionService.query).toHaveBeenCalled();
      expect(adhesionService.addAdhesionToCollectionIfMissing).toHaveBeenCalledWith(
        adhesionCollection,
        ...additionalAdhesions.map(expect.objectContaining)
      );
      expect(comp.adhesionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const paiementAdhesion: IPaiementAdhesion = { id: 456 };
      const adhesion: IAdhesion = { id: 39667 };
      paiementAdhesion.adhesion = adhesion;

      activatedRoute.data = of({ paiementAdhesion });
      comp.ngOnInit();

      expect(comp.adhesionsSharedCollection).toContain(adhesion);
      expect(comp.paiementAdhesion).toEqual(paiementAdhesion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementAdhesion>>();
      const paiementAdhesion = { id: 123 };
      jest.spyOn(paiementAdhesionFormService, 'getPaiementAdhesion').mockReturnValue(paiementAdhesion);
      jest.spyOn(paiementAdhesionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementAdhesion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiementAdhesion }));
      saveSubject.complete();

      // THEN
      expect(paiementAdhesionFormService.getPaiementAdhesion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paiementAdhesionService.update).toHaveBeenCalledWith(expect.objectContaining(paiementAdhesion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementAdhesion>>();
      const paiementAdhesion = { id: 123 };
      jest.spyOn(paiementAdhesionFormService, 'getPaiementAdhesion').mockReturnValue({ id: null });
      jest.spyOn(paiementAdhesionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementAdhesion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiementAdhesion }));
      saveSubject.complete();

      // THEN
      expect(paiementAdhesionFormService.getPaiementAdhesion).toHaveBeenCalled();
      expect(paiementAdhesionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementAdhesion>>();
      const paiementAdhesion = { id: 123 };
      jest.spyOn(paiementAdhesionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementAdhesion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paiementAdhesionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAdhesion', () => {
      it('Should forward to adhesionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(adhesionService, 'compareAdhesion');
        comp.compareAdhesion(entity, entity2);
        expect(adhesionService.compareAdhesion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
