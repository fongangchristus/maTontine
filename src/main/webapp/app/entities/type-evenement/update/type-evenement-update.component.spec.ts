import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeEvenementFormService } from './type-evenement-form.service';
import { TypeEvenementService } from '../service/type-evenement.service';
import { ITypeEvenement } from '../type-evenement.model';

import { TypeEvenementUpdateComponent } from './type-evenement-update.component';

describe('TypeEvenement Management Update Component', () => {
  let comp: TypeEvenementUpdateComponent;
  let fixture: ComponentFixture<TypeEvenementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeEvenementFormService: TypeEvenementFormService;
  let typeEvenementService: TypeEvenementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TypeEvenementUpdateComponent],
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
      .overrideTemplate(TypeEvenementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeEvenementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeEvenementFormService = TestBed.inject(TypeEvenementFormService);
    typeEvenementService = TestBed.inject(TypeEvenementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeEvenement: ITypeEvenement = { id: 456 };

      activatedRoute.data = of({ typeEvenement });
      comp.ngOnInit();

      expect(comp.typeEvenement).toEqual(typeEvenement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeEvenement>>();
      const typeEvenement = { id: 123 };
      jest.spyOn(typeEvenementFormService, 'getTypeEvenement').mockReturnValue(typeEvenement);
      jest.spyOn(typeEvenementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeEvenement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeEvenement }));
      saveSubject.complete();

      // THEN
      expect(typeEvenementFormService.getTypeEvenement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeEvenementService.update).toHaveBeenCalledWith(expect.objectContaining(typeEvenement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeEvenement>>();
      const typeEvenement = { id: 123 };
      jest.spyOn(typeEvenementFormService, 'getTypeEvenement').mockReturnValue({ id: null });
      jest.spyOn(typeEvenementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeEvenement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeEvenement }));
      saveSubject.complete();

      // THEN
      expect(typeEvenementFormService.getTypeEvenement).toHaveBeenCalled();
      expect(typeEvenementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeEvenement>>();
      const typeEvenement = { id: 123 };
      jest.spyOn(typeEvenementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeEvenement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeEvenementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
