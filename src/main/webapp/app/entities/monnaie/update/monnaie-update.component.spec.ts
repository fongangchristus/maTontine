import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MonnaieFormService } from './monnaie-form.service';
import { MonnaieService } from '../service/monnaie.service';
import { IMonnaie } from '../monnaie.model';

import { MonnaieUpdateComponent } from './monnaie-update.component';

describe('Monnaie Management Update Component', () => {
  let comp: MonnaieUpdateComponent;
  let fixture: ComponentFixture<MonnaieUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let monnaieFormService: MonnaieFormService;
  let monnaieService: MonnaieService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MonnaieUpdateComponent],
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
      .overrideTemplate(MonnaieUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MonnaieUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    monnaieFormService = TestBed.inject(MonnaieFormService);
    monnaieService = TestBed.inject(MonnaieService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const monnaie: IMonnaie = { id: 456 };

      activatedRoute.data = of({ monnaie });
      comp.ngOnInit();

      expect(comp.monnaie).toEqual(monnaie);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonnaie>>();
      const monnaie = { id: 123 };
      jest.spyOn(monnaieFormService, 'getMonnaie').mockReturnValue(monnaie);
      jest.spyOn(monnaieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monnaie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monnaie }));
      saveSubject.complete();

      // THEN
      expect(monnaieFormService.getMonnaie).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(monnaieService.update).toHaveBeenCalledWith(expect.objectContaining(monnaie));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonnaie>>();
      const monnaie = { id: 123 };
      jest.spyOn(monnaieFormService, 'getMonnaie').mockReturnValue({ id: null });
      jest.spyOn(monnaieService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monnaie: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monnaie }));
      saveSubject.complete();

      // THEN
      expect(monnaieFormService.getMonnaie).toHaveBeenCalled();
      expect(monnaieService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonnaie>>();
      const monnaie = { id: 123 };
      jest.spyOn(monnaieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monnaie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(monnaieService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
