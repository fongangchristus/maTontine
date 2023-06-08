import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CotisationBanqueDetailComponent } from './cotisation-banque-detail.component';

describe('CotisationBanque Management Detail Component', () => {
  let comp: CotisationBanqueDetailComponent;
  let fixture: ComponentFixture<CotisationBanqueDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CotisationBanqueDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cotisationBanque: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CotisationBanqueDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CotisationBanqueDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cotisationBanque on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cotisationBanque).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
