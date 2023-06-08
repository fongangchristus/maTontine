import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HistoriquePersonneFormService } from './historique-personne-form.service';
import { HistoriquePersonneService } from '../service/historique-personne.service';
import { IHistoriquePersonne } from '../historique-personne.model';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

import { HistoriquePersonneUpdateComponent } from './historique-personne-update.component';

describe('HistoriquePersonne Management Update Component', () => {
  let comp: HistoriquePersonneUpdateComponent;
  let fixture: ComponentFixture<HistoriquePersonneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let historiquePersonneFormService: HistoriquePersonneFormService;
  let historiquePersonneService: HistoriquePersonneService;
  let personneService: PersonneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HistoriquePersonneUpdateComponent],
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
      .overrideTemplate(HistoriquePersonneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HistoriquePersonneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    historiquePersonneFormService = TestBed.inject(HistoriquePersonneFormService);
    historiquePersonneService = TestBed.inject(HistoriquePersonneService);
    personneService = TestBed.inject(PersonneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Personne query and add missing value', () => {
      const historiquePersonne: IHistoriquePersonne = { id: 456 };
      const personne: IPersonne = { id: 21898 };
      historiquePersonne.personne = personne;

      const personneCollection: IPersonne[] = [{ id: 61058 }];
      jest.spyOn(personneService, 'query').mockReturnValue(of(new HttpResponse({ body: personneCollection })));
      const additionalPersonnes = [personne];
      const expectedCollection: IPersonne[] = [...additionalPersonnes, ...personneCollection];
      jest.spyOn(personneService, 'addPersonneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ historiquePersonne });
      comp.ngOnInit();

      expect(personneService.query).toHaveBeenCalled();
      expect(personneService.addPersonneToCollectionIfMissing).toHaveBeenCalledWith(
        personneCollection,
        ...additionalPersonnes.map(expect.objectContaining)
      );
      expect(comp.personnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const historiquePersonne: IHistoriquePersonne = { id: 456 };
      const personne: IPersonne = { id: 65659 };
      historiquePersonne.personne = personne;

      activatedRoute.data = of({ historiquePersonne });
      comp.ngOnInit();

      expect(comp.personnesSharedCollection).toContain(personne);
      expect(comp.historiquePersonne).toEqual(historiquePersonne);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistoriquePersonne>>();
      const historiquePersonne = { id: 123 };
      jest.spyOn(historiquePersonneFormService, 'getHistoriquePersonne').mockReturnValue(historiquePersonne);
      jest.spyOn(historiquePersonneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historiquePersonne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: historiquePersonne }));
      saveSubject.complete();

      // THEN
      expect(historiquePersonneFormService.getHistoriquePersonne).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(historiquePersonneService.update).toHaveBeenCalledWith(expect.objectContaining(historiquePersonne));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistoriquePersonne>>();
      const historiquePersonne = { id: 123 };
      jest.spyOn(historiquePersonneFormService, 'getHistoriquePersonne').mockReturnValue({ id: null });
      jest.spyOn(historiquePersonneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historiquePersonne: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: historiquePersonne }));
      saveSubject.complete();

      // THEN
      expect(historiquePersonneFormService.getHistoriquePersonne).toHaveBeenCalled();
      expect(historiquePersonneService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistoriquePersonne>>();
      const historiquePersonne = { id: 123 };
      jest.spyOn(historiquePersonneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historiquePersonne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(historiquePersonneService.update).toHaveBeenCalled();
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
