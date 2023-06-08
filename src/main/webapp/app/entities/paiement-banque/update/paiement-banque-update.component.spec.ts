import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PaiementBanqueFormService } from './paiement-banque-form.service';
import { PaiementBanqueService } from '../service/paiement-banque.service';
import { IPaiementBanque } from '../paiement-banque.model';

import { PaiementBanqueUpdateComponent } from './paiement-banque-update.component';

describe('PaiementBanque Management Update Component', () => {
  let comp: PaiementBanqueUpdateComponent;
  let fixture: ComponentFixture<PaiementBanqueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paiementBanqueFormService: PaiementBanqueFormService;
  let paiementBanqueService: PaiementBanqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PaiementBanqueUpdateComponent],
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
      .overrideTemplate(PaiementBanqueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaiementBanqueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paiementBanqueFormService = TestBed.inject(PaiementBanqueFormService);
    paiementBanqueService = TestBed.inject(PaiementBanqueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const paiementBanque: IPaiementBanque = { id: 456 };

      activatedRoute.data = of({ paiementBanque });
      comp.ngOnInit();

      expect(comp.paiementBanque).toEqual(paiementBanque);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementBanque>>();
      const paiementBanque = { id: 123 };
      jest.spyOn(paiementBanqueFormService, 'getPaiementBanque').mockReturnValue(paiementBanque);
      jest.spyOn(paiementBanqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementBanque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiementBanque }));
      saveSubject.complete();

      // THEN
      expect(paiementBanqueFormService.getPaiementBanque).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paiementBanqueService.update).toHaveBeenCalledWith(expect.objectContaining(paiementBanque));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementBanque>>();
      const paiementBanque = { id: 123 };
      jest.spyOn(paiementBanqueFormService, 'getPaiementBanque').mockReturnValue({ id: null });
      jest.spyOn(paiementBanqueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementBanque: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiementBanque }));
      saveSubject.complete();

      // THEN
      expect(paiementBanqueFormService.getPaiementBanque).toHaveBeenCalled();
      expect(paiementBanqueService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementBanque>>();
      const paiementBanque = { id: 123 };
      jest.spyOn(paiementBanqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementBanque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paiementBanqueService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
