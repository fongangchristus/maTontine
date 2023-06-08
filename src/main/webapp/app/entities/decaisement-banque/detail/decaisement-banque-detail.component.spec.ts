import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DecaisementBanqueDetailComponent } from './decaisement-banque-detail.component';

describe('DecaisementBanque Management Detail Component', () => {
  let comp: DecaisementBanqueDetailComponent;
  let fixture: ComponentFixture<DecaisementBanqueDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DecaisementBanqueDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ decaisementBanque: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DecaisementBanqueDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DecaisementBanqueDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load decaisementBanque on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.decaisementBanque).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
