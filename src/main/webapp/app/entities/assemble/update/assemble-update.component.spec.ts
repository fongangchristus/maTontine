import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AssembleFormService } from './assemble-form.service';
import { AssembleService } from '../service/assemble.service';
import { IAssemble } from '../assemble.model';

import { AssembleUpdateComponent } from './assemble-update.component';

describe('Assemble Management Update Component', () => {
  let comp: AssembleUpdateComponent;
  let fixture: ComponentFixture<AssembleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assembleFormService: AssembleFormService;
  let assembleService: AssembleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AssembleUpdateComponent],
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
      .overrideTemplate(AssembleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssembleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assembleFormService = TestBed.inject(AssembleFormService);
    assembleService = TestBed.inject(AssembleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const assemble: IAssemble = { id: 456 };

      activatedRoute.data = of({ assemble });
      comp.ngOnInit();

      expect(comp.assemble).toEqual(assemble);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssemble>>();
      const assemble = { id: 123 };
      jest.spyOn(assembleFormService, 'getAssemble').mockReturnValue(assemble);
      jest.spyOn(assembleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assemble });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assemble }));
      saveSubject.complete();

      // THEN
      expect(assembleFormService.getAssemble).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(assembleService.update).toHaveBeenCalledWith(expect.objectContaining(assemble));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssemble>>();
      const assemble = { id: 123 };
      jest.spyOn(assembleFormService, 'getAssemble').mockReturnValue({ id: null });
      jest.spyOn(assembleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assemble: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assemble }));
      saveSubject.complete();

      // THEN
      expect(assembleFormService.getAssemble).toHaveBeenCalled();
      expect(assembleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssemble>>();
      const assemble = { id: 123 };
      jest.spyOn(assembleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assemble });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assembleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
