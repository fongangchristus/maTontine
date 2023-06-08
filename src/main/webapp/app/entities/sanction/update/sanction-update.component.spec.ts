import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SanctionFormService } from './sanction-form.service';
import { SanctionService } from '../service/sanction.service';
import { ISanction } from '../sanction.model';
import { ISanctionConfiguration } from 'app/entities/sanction-configuration/sanction-configuration.model';
import { SanctionConfigurationService } from 'app/entities/sanction-configuration/service/sanction-configuration.service';

import { SanctionUpdateComponent } from './sanction-update.component';

describe('Sanction Management Update Component', () => {
  let comp: SanctionUpdateComponent;
  let fixture: ComponentFixture<SanctionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sanctionFormService: SanctionFormService;
  let sanctionService: SanctionService;
  let sanctionConfigurationService: SanctionConfigurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SanctionUpdateComponent],
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
      .overrideTemplate(SanctionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SanctionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sanctionFormService = TestBed.inject(SanctionFormService);
    sanctionService = TestBed.inject(SanctionService);
    sanctionConfigurationService = TestBed.inject(SanctionConfigurationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SanctionConfiguration query and add missing value', () => {
      const sanction: ISanction = { id: 456 };
      const sanctionConfig: ISanctionConfiguration = { id: 52630 };
      sanction.sanctionConfig = sanctionConfig;

      const sanctionConfigurationCollection: ISanctionConfiguration[] = [{ id: 61768 }];
      jest.spyOn(sanctionConfigurationService, 'query').mockReturnValue(of(new HttpResponse({ body: sanctionConfigurationCollection })));
      const additionalSanctionConfigurations = [sanctionConfig];
      const expectedCollection: ISanctionConfiguration[] = [...additionalSanctionConfigurations, ...sanctionConfigurationCollection];
      jest.spyOn(sanctionConfigurationService, 'addSanctionConfigurationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sanction });
      comp.ngOnInit();

      expect(sanctionConfigurationService.query).toHaveBeenCalled();
      expect(sanctionConfigurationService.addSanctionConfigurationToCollectionIfMissing).toHaveBeenCalledWith(
        sanctionConfigurationCollection,
        ...additionalSanctionConfigurations.map(expect.objectContaining)
      );
      expect(comp.sanctionConfigurationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sanction: ISanction = { id: 456 };
      const sanctionConfig: ISanctionConfiguration = { id: 60353 };
      sanction.sanctionConfig = sanctionConfig;

      activatedRoute.data = of({ sanction });
      comp.ngOnInit();

      expect(comp.sanctionConfigurationsSharedCollection).toContain(sanctionConfig);
      expect(comp.sanction).toEqual(sanction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISanction>>();
      const sanction = { id: 123 };
      jest.spyOn(sanctionFormService, 'getSanction').mockReturnValue(sanction);
      jest.spyOn(sanctionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sanction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sanction }));
      saveSubject.complete();

      // THEN
      expect(sanctionFormService.getSanction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sanctionService.update).toHaveBeenCalledWith(expect.objectContaining(sanction));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISanction>>();
      const sanction = { id: 123 };
      jest.spyOn(sanctionFormService, 'getSanction').mockReturnValue({ id: null });
      jest.spyOn(sanctionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sanction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sanction }));
      saveSubject.complete();

      // THEN
      expect(sanctionFormService.getSanction).toHaveBeenCalled();
      expect(sanctionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISanction>>();
      const sanction = { id: 123 };
      jest.spyOn(sanctionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sanction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sanctionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSanctionConfiguration', () => {
      it('Should forward to sanctionConfigurationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sanctionConfigurationService, 'compareSanctionConfiguration');
        comp.compareSanctionConfiguration(entity, entity2);
        expect(sanctionConfigurationService.compareSanctionConfiguration).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
