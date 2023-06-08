import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FonctionAdherentFormService } from './fonction-adherent-form.service';
import { FonctionAdherentService } from '../service/fonction-adherent.service';
import { IFonctionAdherent } from '../fonction-adherent.model';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { IFonction } from 'app/entities/fonction/fonction.model';
import { FonctionService } from 'app/entities/fonction/service/fonction.service';

import { FonctionAdherentUpdateComponent } from './fonction-adherent-update.component';

describe('FonctionAdherent Management Update Component', () => {
  let comp: FonctionAdherentUpdateComponent;
  let fixture: ComponentFixture<FonctionAdherentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fonctionAdherentFormService: FonctionAdherentFormService;
  let fonctionAdherentService: FonctionAdherentService;
  let personneService: PersonneService;
  let fonctionService: FonctionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FonctionAdherentUpdateComponent],
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
      .overrideTemplate(FonctionAdherentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FonctionAdherentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fonctionAdherentFormService = TestBed.inject(FonctionAdherentFormService);
    fonctionAdherentService = TestBed.inject(FonctionAdherentService);
    personneService = TestBed.inject(PersonneService);
    fonctionService = TestBed.inject(FonctionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Personne query and add missing value', () => {
      const fonctionAdherent: IFonctionAdherent = { id: 456 };
      const adherent: IPersonne = { id: 85845 };
      fonctionAdherent.adherent = adherent;

      const personneCollection: IPersonne[] = [{ id: 38332 }];
      jest.spyOn(personneService, 'query').mockReturnValue(of(new HttpResponse({ body: personneCollection })));
      const additionalPersonnes = [adherent];
      const expectedCollection: IPersonne[] = [...additionalPersonnes, ...personneCollection];
      jest.spyOn(personneService, 'addPersonneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fonctionAdherent });
      comp.ngOnInit();

      expect(personneService.query).toHaveBeenCalled();
      expect(personneService.addPersonneToCollectionIfMissing).toHaveBeenCalledWith(
        personneCollection,
        ...additionalPersonnes.map(expect.objectContaining)
      );
      expect(comp.personnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Fonction query and add missing value', () => {
      const fonctionAdherent: IFonctionAdherent = { id: 456 };
      const fonction: IFonction = { id: 74337 };
      fonctionAdherent.fonction = fonction;

      const fonctionCollection: IFonction[] = [{ id: 33219 }];
      jest.spyOn(fonctionService, 'query').mockReturnValue(of(new HttpResponse({ body: fonctionCollection })));
      const additionalFonctions = [fonction];
      const expectedCollection: IFonction[] = [...additionalFonctions, ...fonctionCollection];
      jest.spyOn(fonctionService, 'addFonctionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fonctionAdherent });
      comp.ngOnInit();

      expect(fonctionService.query).toHaveBeenCalled();
      expect(fonctionService.addFonctionToCollectionIfMissing).toHaveBeenCalledWith(
        fonctionCollection,
        ...additionalFonctions.map(expect.objectContaining)
      );
      expect(comp.fonctionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const fonctionAdherent: IFonctionAdherent = { id: 456 };
      const adherent: IPersonne = { id: 85 };
      fonctionAdherent.adherent = adherent;
      const fonction: IFonction = { id: 21552 };
      fonctionAdherent.fonction = fonction;

      activatedRoute.data = of({ fonctionAdherent });
      comp.ngOnInit();

      expect(comp.personnesSharedCollection).toContain(adherent);
      expect(comp.fonctionsSharedCollection).toContain(fonction);
      expect(comp.fonctionAdherent).toEqual(fonctionAdherent);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFonctionAdherent>>();
      const fonctionAdherent = { id: 123 };
      jest.spyOn(fonctionAdherentFormService, 'getFonctionAdherent').mockReturnValue(fonctionAdherent);
      jest.spyOn(fonctionAdherentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fonctionAdherent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fonctionAdherent }));
      saveSubject.complete();

      // THEN
      expect(fonctionAdherentFormService.getFonctionAdherent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fonctionAdherentService.update).toHaveBeenCalledWith(expect.objectContaining(fonctionAdherent));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFonctionAdherent>>();
      const fonctionAdherent = { id: 123 };
      jest.spyOn(fonctionAdherentFormService, 'getFonctionAdherent').mockReturnValue({ id: null });
      jest.spyOn(fonctionAdherentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fonctionAdherent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fonctionAdherent }));
      saveSubject.complete();

      // THEN
      expect(fonctionAdherentFormService.getFonctionAdherent).toHaveBeenCalled();
      expect(fonctionAdherentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFonctionAdherent>>();
      const fonctionAdherent = { id: 123 };
      jest.spyOn(fonctionAdherentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fonctionAdherent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fonctionAdherentService.update).toHaveBeenCalled();
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

    describe('compareFonction', () => {
      it('Should forward to fonctionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(fonctionService, 'compareFonction');
        comp.compareFonction(entity, entity2);
        expect(fonctionService.compareFonction).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
