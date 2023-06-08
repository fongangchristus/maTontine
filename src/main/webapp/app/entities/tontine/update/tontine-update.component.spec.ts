import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TontineFormService } from './tontine-form.service';
import { TontineService } from '../service/tontine.service';
import { ITontine } from '../tontine.model';

import { TontineUpdateComponent } from './tontine-update.component';

describe('Tontine Management Update Component', () => {
  let comp: TontineUpdateComponent;
  let fixture: ComponentFixture<TontineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tontineFormService: TontineFormService;
  let tontineService: TontineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TontineUpdateComponent],
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
      .overrideTemplate(TontineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TontineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tontineFormService = TestBed.inject(TontineFormService);
    tontineService = TestBed.inject(TontineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tontine: ITontine = { id: 456 };

      activatedRoute.data = of({ tontine });
      comp.ngOnInit();

      expect(comp.tontine).toEqual(tontine);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITontine>>();
      const tontine = { id: 123 };
      jest.spyOn(tontineFormService, 'getTontine').mockReturnValue(tontine);
      jest.spyOn(tontineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tontine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tontine }));
      saveSubject.complete();

      // THEN
      expect(tontineFormService.getTontine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tontineService.update).toHaveBeenCalledWith(expect.objectContaining(tontine));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITontine>>();
      const tontine = { id: 123 };
      jest.spyOn(tontineFormService, 'getTontine').mockReturnValue({ id: null });
      jest.spyOn(tontineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tontine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tontine }));
      saveSubject.complete();

      // THEN
      expect(tontineFormService.getTontine).toHaveBeenCalled();
      expect(tontineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITontine>>();
      const tontine = { id: 123 };
      jest.spyOn(tontineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tontine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tontineService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
