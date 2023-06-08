import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SanctionConfigurationDetailComponent } from './sanction-configuration-detail.component';

describe('SanctionConfiguration Management Detail Component', () => {
  let comp: SanctionConfigurationDetailComponent;
  let fixture: ComponentFixture<SanctionConfigurationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SanctionConfigurationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sanctionConfiguration: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SanctionConfigurationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SanctionConfigurationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sanctionConfiguration on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sanctionConfiguration).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
