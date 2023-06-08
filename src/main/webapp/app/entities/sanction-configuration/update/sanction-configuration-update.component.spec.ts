import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SanctionConfigurationFormService } from './sanction-configuration-form.service';
import { SanctionConfigurationService } from '../service/sanction-configuration.service';
import { ISanctionConfiguration } from '../sanction-configuration.model';

import { SanctionConfigurationUpdateComponent } from './sanction-configuration-update.component';

describe('SanctionConfiguration Management Update Component', () => {
  let comp: SanctionConfigurationUpdateComponent;
  let fixture: ComponentFixture<SanctionConfigurationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sanctionConfigurationFormService: SanctionConfigurationFormService;
  let sanctionConfigurationService: SanctionConfigurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SanctionConfigurationUpdateComponent],
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
      .overrideTemplate(SanctionConfigurationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SanctionConfigurationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sanctionConfigurationFormService = TestBed.inject(SanctionConfigurationFormService);
    sanctionConfigurationService = TestBed.inject(SanctionConfigurationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sanctionConfiguration: ISanctionConfiguration = { id: 456 };

      activatedRoute.data = of({ sanctionConfiguration });
      comp.ngOnInit();

      expect(comp.sanctionConfiguration).toEqual(sanctionConfiguration);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISanctionConfiguration>>();
      const sanctionConfiguration = { id: 123 };
      jest.spyOn(sanctionConfigurationFormService, 'getSanctionConfiguration').mockReturnValue(sanctionConfiguration);
      jest.spyOn(sanctionConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sanctionConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sanctionConfiguration }));
      saveSubject.complete();

      // THEN
      expect(sanctionConfigurationFormService.getSanctionConfiguration).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sanctionConfigurationService.update).toHaveBeenCalledWith(expect.objectContaining(sanctionConfiguration));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISanctionConfiguration>>();
      const sanctionConfiguration = { id: 123 };
      jest.spyOn(sanctionConfigurationFormService, 'getSanctionConfiguration').mockReturnValue({ id: null });
      jest.spyOn(sanctionConfigurationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sanctionConfiguration: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sanctionConfiguration }));
      saveSubject.complete();

      // THEN
      expect(sanctionConfigurationFormService.getSanctionConfiguration).toHaveBeenCalled();
      expect(sanctionConfigurationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISanctionConfiguration>>();
      const sanctionConfiguration = { id: 123 };
      jest.spyOn(sanctionConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sanctionConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sanctionConfigurationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
