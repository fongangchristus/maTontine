import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CotisationTontineDetailComponent } from './cotisation-tontine-detail.component';

describe('CotisationTontine Management Detail Component', () => {
  let comp: CotisationTontineDetailComponent;
  let fixture: ComponentFixture<CotisationTontineDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CotisationTontineDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cotisationTontine: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CotisationTontineDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CotisationTontineDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cotisationTontine on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cotisationTontine).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
