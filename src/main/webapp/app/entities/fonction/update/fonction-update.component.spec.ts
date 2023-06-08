import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FonctionFormService } from './fonction-form.service';
import { FonctionService } from '../service/fonction.service';
import { IFonction } from '../fonction.model';

import { FonctionUpdateComponent } from './fonction-update.component';

describe('Fonction Management Update Component', () => {
  let comp: FonctionUpdateComponent;
  let fixture: ComponentFixture<FonctionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fonctionFormService: FonctionFormService;
  let fonctionService: FonctionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FonctionUpdateComponent],
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
      .overrideTemplate(FonctionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FonctionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fonctionFormService = TestBed.inject(FonctionFormService);
    fonctionService = TestBed.inject(FonctionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fonction: IFonction = { id: 456 };

      activatedRoute.data = of({ fonction });
      comp.ngOnInit();

      expect(comp.fonction).toEqual(fonction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFonction>>();
      const fonction = { id: 123 };
      jest.spyOn(fonctionFormService, 'getFonction').mockReturnValue(fonction);
      jest.spyOn(fonctionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fonction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fonction }));
      saveSubject.complete();

      // THEN
      expect(fonctionFormService.getFonction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fonctionService.update).toHaveBeenCalledWith(expect.objectContaining(fonction));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFonction>>();
      const fonction = { id: 123 };
      jest.spyOn(fonctionFormService, 'getFonction').mockReturnValue({ id: null });
      jest.spyOn(fonctionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fonction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fonction }));
      saveSubject.complete();

      // THEN
      expect(fonctionFormService.getFonction).toHaveBeenCalled();
      expect(fonctionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFonction>>();
      const fonction = { id: 123 };
      jest.spyOn(fonctionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fonction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fonctionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
