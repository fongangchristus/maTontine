import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContributionPotFormService } from './contribution-pot-form.service';
import { ContributionPotService } from '../service/contribution-pot.service';
import { IContributionPot } from '../contribution-pot.model';
import { IPot } from 'app/entities/pot/pot.model';
import { PotService } from 'app/entities/pot/service/pot.service';

import { ContributionPotUpdateComponent } from './contribution-pot-update.component';

describe('ContributionPot Management Update Component', () => {
  let comp: ContributionPotUpdateComponent;
  let fixture: ComponentFixture<ContributionPotUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contributionPotFormService: ContributionPotFormService;
  let contributionPotService: ContributionPotService;
  let potService: PotService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContributionPotUpdateComponent],
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
      .overrideTemplate(ContributionPotUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContributionPotUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contributionPotFormService = TestBed.inject(ContributionPotFormService);
    contributionPotService = TestBed.inject(ContributionPotService);
    potService = TestBed.inject(PotService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Pot query and add missing value', () => {
      const contributionPot: IContributionPot = { id: 456 };
      const pot: IPot = { id: 76992 };
      contributionPot.pot = pot;

      const potCollection: IPot[] = [{ id: 20110 }];
      jest.spyOn(potService, 'query').mockReturnValue(of(new HttpResponse({ body: potCollection })));
      const additionalPots = [pot];
      const expectedCollection: IPot[] = [...additionalPots, ...potCollection];
      jest.spyOn(potService, 'addPotToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contributionPot });
      comp.ngOnInit();

      expect(potService.query).toHaveBeenCalled();
      expect(potService.addPotToCollectionIfMissing).toHaveBeenCalledWith(potCollection, ...additionalPots.map(expect.objectContaining));
      expect(comp.potsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contributionPot: IContributionPot = { id: 456 };
      const pot: IPot = { id: 11003 };
      contributionPot.pot = pot;

      activatedRoute.data = of({ contributionPot });
      comp.ngOnInit();

      expect(comp.potsSharedCollection).toContain(pot);
      expect(comp.contributionPot).toEqual(contributionPot);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContributionPot>>();
      const contributionPot = { id: 123 };
      jest.spyOn(contributionPotFormService, 'getContributionPot').mockReturnValue(contributionPot);
      jest.spyOn(contributionPotService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contributionPot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contributionPot }));
      saveSubject.complete();

      // THEN
      expect(contributionPotFormService.getContributionPot).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contributionPotService.update).toHaveBeenCalledWith(expect.objectContaining(contributionPot));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContributionPot>>();
      const contributionPot = { id: 123 };
      jest.spyOn(contributionPotFormService, 'getContributionPot').mockReturnValue({ id: null });
      jest.spyOn(contributionPotService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contributionPot: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contributionPot }));
      saveSubject.complete();

      // THEN
      expect(contributionPotFormService.getContributionPot).toHaveBeenCalled();
      expect(contributionPotService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContributionPot>>();
      const contributionPot = { id: 123 };
      jest.spyOn(contributionPotService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contributionPot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contributionPotService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePot', () => {
      it('Should forward to potService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(potService, 'comparePot');
        comp.comparePot(entity, entity2);
        expect(potService.comparePot).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
