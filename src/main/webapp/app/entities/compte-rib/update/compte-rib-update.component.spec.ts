import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompteRIBFormService } from './compte-rib-form.service';
import { CompteRIBService } from '../service/compte-rib.service';
import { ICompteRIB } from '../compte-rib.model';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

import { CompteRIBUpdateComponent } from './compte-rib-update.component';

describe('CompteRIB Management Update Component', () => {
  let comp: CompteRIBUpdateComponent;
  let fixture: ComponentFixture<CompteRIBUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let compteRIBFormService: CompteRIBFormService;
  let compteRIBService: CompteRIBService;
  let personneService: PersonneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompteRIBUpdateComponent],
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
      .overrideTemplate(CompteRIBUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompteRIBUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    compteRIBFormService = TestBed.inject(CompteRIBFormService);
    compteRIBService = TestBed.inject(CompteRIBService);
    personneService = TestBed.inject(PersonneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Personne query and add missing value', () => {
      const compteRIB: ICompteRIB = { id: 456 };
      const adherent: IPersonne = { id: 58992 };
      compteRIB.adherent = adherent;

      const personneCollection: IPersonne[] = [{ id: 81493 }];
      jest.spyOn(personneService, 'query').mockReturnValue(of(new HttpResponse({ body: personneCollection })));
      const additionalPersonnes = [adherent];
      const expectedCollection: IPersonne[] = [...additionalPersonnes, ...personneCollection];
      jest.spyOn(personneService, 'addPersonneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ compteRIB });
      comp.ngOnInit();

      expect(personneService.query).toHaveBeenCalled();
      expect(personneService.addPersonneToCollectionIfMissing).toHaveBeenCalledWith(
        personneCollection,
        ...additionalPersonnes.map(expect.objectContaining)
      );
      expect(comp.personnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const compteRIB: ICompteRIB = { id: 456 };
      const adherent: IPersonne = { id: 41342 };
      compteRIB.adherent = adherent;

      activatedRoute.data = of({ compteRIB });
      comp.ngOnInit();

      expect(comp.personnesSharedCollection).toContain(adherent);
      expect(comp.compteRIB).toEqual(compteRIB);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompteRIB>>();
      const compteRIB = { id: 123 };
      jest.spyOn(compteRIBFormService, 'getCompteRIB').mockReturnValue(compteRIB);
      jest.spyOn(compteRIBService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compteRIB });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compteRIB }));
      saveSubject.complete();

      // THEN
      expect(compteRIBFormService.getCompteRIB).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(compteRIBService.update).toHaveBeenCalledWith(expect.objectContaining(compteRIB));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompteRIB>>();
      const compteRIB = { id: 123 };
      jest.spyOn(compteRIBFormService, 'getCompteRIB').mockReturnValue({ id: null });
      jest.spyOn(compteRIBService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compteRIB: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compteRIB }));
      saveSubject.complete();

      // THEN
      expect(compteRIBFormService.getCompteRIB).toHaveBeenCalled();
      expect(compteRIBService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompteRIB>>();
      const compteRIB = { id: 123 };
      jest.spyOn(compteRIBService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compteRIB });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(compteRIBService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePersonne', () => {
      it('Should forward to personneService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(personneService, 'comparePersonne');
        comp.comparePersonne(entity, entity2);
        expect(personneService.comparePersonne).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
