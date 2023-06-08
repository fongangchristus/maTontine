import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FormuleAdhesionFormService } from './formule-adhesion-form.service';
import { FormuleAdhesionService } from '../service/formule-adhesion.service';
import { IFormuleAdhesion } from '../formule-adhesion.model';
import { IAdhesion } from 'app/entities/adhesion/adhesion.model';
import { AdhesionService } from 'app/entities/adhesion/service/adhesion.service';

import { FormuleAdhesionUpdateComponent } from './formule-adhesion-update.component';

describe('FormuleAdhesion Management Update Component', () => {
  let comp: FormuleAdhesionUpdateComponent;
  let fixture: ComponentFixture<FormuleAdhesionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let formuleAdhesionFormService: FormuleAdhesionFormService;
  let formuleAdhesionService: FormuleAdhesionService;
  let adhesionService: AdhesionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FormuleAdhesionUpdateComponent],
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
      .overrideTemplate(FormuleAdhesionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormuleAdhesionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    formuleAdhesionFormService = TestBed.inject(FormuleAdhesionFormService);
    formuleAdhesionService = TestBed.inject(FormuleAdhesionService);
    adhesionService = TestBed.inject(AdhesionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Adhesion query and add missing value', () => {
      const formuleAdhesion: IFormuleAdhesion = { id: 456 };
      const adhesion: IAdhesion = { id: 36495 };
      formuleAdhesion.adhesion = adhesion;

      const adhesionCollection: IAdhesion[] = [{ id: 68153 }];
      jest.spyOn(adhesionService, 'query').mockReturnValue(of(new HttpResponse({ body: adhesionCollection })));
      const additionalAdhesions = [adhesion];
      const expectedCollection: IAdhesion[] = [...additionalAdhesions, ...adhesionCollection];
      jest.spyOn(adhesionService, 'addAdhesionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formuleAdhesion });
      comp.ngOnInit();

      expect(adhesionService.query).toHaveBeenCalled();
      expect(adhesionService.addAdhesionToCollectionIfMissing).toHaveBeenCalledWith(
        adhesionCollection,
        ...additionalAdhesions.map(expect.objectContaining)
      );
      expect(comp.adhesionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const formuleAdhesion: IFormuleAdhesion = { id: 456 };
      const adhesion: IAdhesion = { id: 36389 };
      formuleAdhesion.adhesion = adhesion;

      activatedRoute.data = of({ formuleAdhesion });
      comp.ngOnInit();

      expect(comp.adhesionsSharedCollection).toContain(adhesion);
      expect(comp.formuleAdhesion).toEqual(formuleAdhesion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormuleAdhesion>>();
      const formuleAdhesion = { id: 123 };
      jest.spyOn(formuleAdhesionFormService, 'getFormuleAdhesion').mockReturnValue(formuleAdhesion);
      jest.spyOn(formuleAdhesionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formuleAdhesion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formuleAdhesion }));
      saveSubject.complete();

      // THEN
      expect(formuleAdhesionFormService.getFormuleAdhesion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(formuleAdhesionService.update).toHaveBeenCalledWith(expect.objectContaining(formuleAdhesion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormuleAdhesion>>();
      const formuleAdhesion = { id: 123 };
      jest.spyOn(formuleAdhesionFormService, 'getFormuleAdhesion').mockReturnValue({ id: null });
      jest.spyOn(formuleAdhesionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formuleAdhesion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formuleAdhesion }));
      saveSubject.complete();

      // THEN
      expect(formuleAdhesionFormService.getFormuleAdhesion).toHaveBeenCalled();
      expect(formuleAdhesionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormuleAdhesion>>();
      const formuleAdhesion = { id: 123 };
      jest.spyOn(formuleAdhesionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formuleAdhesion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(formuleAdhesionService.update).toHaveBeenCalled();
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
