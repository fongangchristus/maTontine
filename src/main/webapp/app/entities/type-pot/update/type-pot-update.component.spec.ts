import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypePotFormService } from './type-pot-form.service';
import { TypePotService } from '../service/type-pot.service';
import { ITypePot } from '../type-pot.model';

import { TypePotUpdateComponent } from './type-pot-update.component';

describe('TypePot Management Update Component', () => {
  let comp: TypePotUpdateComponent;
  let fixture: ComponentFixture<TypePotUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typePotFormService: TypePotFormService;
  let typePotService: TypePotService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TypePotUpdateComponent],
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
      .overrideTemplate(TypePotUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypePotUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typePotFormService = TestBed.inject(TypePotFormService);
    typePotService = TestBed.inject(TypePotService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typePot: ITypePot = { id: 456 };

      activatedRoute.data = of({ typePot });
      comp.ngOnInit();

      expect(comp.typePot).toEqual(typePot);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypePot>>();
      const typePot = { id: 123 };
      jest.spyOn(typePotFormService, 'getTypePot').mockReturnValue(typePot);
      jest.spyOn(typePotService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typePot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typePot }));
      saveSubject.complete();

      // THEN
      expect(typePotFormService.getTypePot).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typePotService.update).toHaveBeenCalledWith(expect.objectContaining(typePot));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypePot>>();
      const typePot = { id: 123 };
      jest.spyOn(typePotFormService, 'getTypePot').mockReturnValue({ id: null });
      jest.spyOn(typePotService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typePot: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typePot }));
      saveSubject.complete();

      // THEN
      expect(typePotFormService.getTypePot).toHaveBeenCalled();
      expect(typePotService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypePot>>();
      const typePot = { id: 123 };
      jest.spyOn(typePotService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typePot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typePotService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
