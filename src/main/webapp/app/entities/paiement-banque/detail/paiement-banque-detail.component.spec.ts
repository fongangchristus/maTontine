import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaiementBanqueDetailComponent } from './paiement-banque-detail.component';

describe('PaiementBanque Management Detail Component', () => {
  let comp: PaiementBanqueDetailComponent;
  let fixture: ComponentFixture<PaiementBanqueDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaiementBanqueDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ paiementBanque: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PaiementBanqueDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PaiementBanqueDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load paiementBanque on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.paiementBanque).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
