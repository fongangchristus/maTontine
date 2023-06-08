import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BanqueFormService } from './banque-form.service';
import { BanqueService } from '../service/banque.service';
import { IBanque } from '../banque.model';

import { BanqueUpdateComponent } from './banque-update.component';

describe('Banque Management Update Component', () => {
  let comp: BanqueUpdateComponent;
  let fixture: ComponentFixture<BanqueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let banqueFormService: BanqueFormService;
  let banqueService: BanqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BanqueUpdateComponent],
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
      .overrideTemplate(BanqueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BanqueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    banqueFormService = TestBed.inject(BanqueFormService);
    banqueService = TestBed.inject(BanqueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const banque: IBanque = { id: 456 };

      activatedRoute.data = of({ banque });
      comp.ngOnInit();

      expect(comp.banque).toEqual(banque);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBanque>>();
      const banque = { id: 123 };
      jest.spyOn(banqueFormService, 'getBanque').mockReturnValue(banque);
      jest.spyOn(banqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ banque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: banque }));
      saveSubject.complete();

      // THEN
      expect(banqueFormService.getBanque).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(banqueService.update).toHaveBeenCalledWith(expect.objectContaining(banque));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBanque>>();
      const banque = { id: 123 };
      jest.spyOn(banqueFormService, 'getBanque').mockReturnValue({ id: null });
      jest.spyOn(banqueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ banque: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: banque }));
      saveSubject.complete();

      // THEN
      expect(banqueFormService.getBanque).toHaveBeenCalled();
      expect(banqueService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBanque>>();
      const banque = { id: 123 };
      jest.spyOn(banqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ banque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(banqueService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
