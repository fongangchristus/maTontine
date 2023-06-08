import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AdhesionFormService } from './adhesion-form.service';
import { AdhesionService } from '../service/adhesion.service';
import { IAdhesion } from '../adhesion.model';

import { AdhesionUpdateComponent } from './adhesion-update.component';

describe('Adhesion Management Update Component', () => {
  let comp: AdhesionUpdateComponent;
  let fixture: ComponentFixture<AdhesionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let adhesionFormService: AdhesionFormService;
  let adhesionService: AdhesionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AdhesionUpdateComponent],
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
      .overrideTemplate(AdhesionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AdhesionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    adhesionFormService = TestBed.inject(AdhesionFormService);
    adhesionService = TestBed.inject(AdhesionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const adhesion: IAdhesion = { id: 456 };

      activatedRoute.data = of({ adhesion });
      comp.ngOnInit();

      expect(comp.adhesion).toEqual(adhesion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdhesion>>();
      const adhesion = { id: 123 };
      jest.spyOn(adhesionFormService, 'getAdhesion').mockReturnValue(adhesion);
      jest.spyOn(adhesionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ adhesion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: adhesion }));
      saveSubject.complete();

      // THEN
      expect(adhesionFormService.getAdhesion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(adhesionService.update).toHaveBeenCalledWith(expect.objectContaining(adhesion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdhesion>>();
      const adhesion = { id: 123 };
      jest.spyOn(adhesionFormService, 'getAdhesion').mockReturnValue({ id: null });
      jest.spyOn(adhesionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ adhesion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: adhesion }));
      saveSubject.complete();

      // THEN
      expect(adhesionFormService.getAdhesion).toHaveBeenCalled();
      expect(adhesionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdhesion>>();
      const adhesion = { id: 123 };
      jest.spyOn(adhesionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ adhesion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(adhesionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
