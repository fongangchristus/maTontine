import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PotFormService } from './pot-form.service';
import { PotService } from '../service/pot.service';
import { IPot } from '../pot.model';
import { ITypePot } from 'app/entities/type-pot/type-pot.model';
import { TypePotService } from 'app/entities/type-pot/service/type-pot.service';

import { PotUpdateComponent } from './pot-update.component';

describe('Pot Management Update Component', () => {
  let comp: PotUpdateComponent;
  let fixture: ComponentFixture<PotUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let potFormService: PotFormService;
  let potService: PotService;
  let typePotService: TypePotService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PotUpdateComponent],
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
      .overrideTemplate(PotUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PotUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    potFormService = TestBed.inject(PotFormService);
    potService = TestBed.inject(PotService);
    typePotService = TestBed.inject(TypePotService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TypePot query and add missing value', () => {
      const pot: IPot = { id: 456 };
      const typePot: ITypePot = { id: 92905 };
      pot.typePot = typePot;

      const typePotCollection: ITypePot[] = [{ id: 24362 }];
      jest.spyOn(typePotService, 'query').mockReturnValue(of(new HttpResponse({ body: typePotCollection })));
      const additionalTypePots = [typePot];
      const expectedCollection: ITypePot[] = [...additionalTypePots, ...typePotCollection];
      jest.spyOn(typePotService, 'addTypePotToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pot });
      comp.ngOnInit();

      expect(typePotService.query).toHaveBeenCalled();
      expect(typePotService.addTypePotToCollectionIfMissing).toHaveBeenCalledWith(
        typePotCollection,
        ...additionalTypePots.map(expect.objectContaining)
      );
      expect(comp.typePotsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pot: IPot = { id: 456 };
      const typePot: ITypePot = { id: 66489 };
      pot.typePot = typePot;

      activatedRoute.data = of({ pot });
      comp.ngOnInit();

      expect(comp.typePotsSharedCollection).toContain(typePot);
      expect(comp.pot).toEqual(pot);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPot>>();
      const pot = { id: 123 };
      jest.spyOn(potFormService, 'getPot').mockReturnValue(pot);
      jest.spyOn(potService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pot }));
      saveSubject.complete();

      // THEN
      expect(potFormService.getPot).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(potService.update).toHaveBeenCalledWith(expect.objectContaining(pot));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPot>>();
      const pot = { id: 123 };
      jest.spyOn(potFormService, 'getPot').mockReturnValue({ id: null });
      jest.spyOn(potService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pot: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pot }));
      saveSubject.complete();

      // THEN
      expect(potFormService.getPot).toHaveBeenCalled();
      expect(potService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPot>>();
      const pot = { id: 123 };
      jest.spyOn(potService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(potService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypePot', () => {
      it('Should forward to typePotService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typePotService, 'compareTypePot');
        comp.compareTypePot(entity, entity2);
        expect(typePotService.compareTypePot).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
