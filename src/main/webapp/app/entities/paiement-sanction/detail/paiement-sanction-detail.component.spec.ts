import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaiementSanctionDetailComponent } from './paiement-sanction-detail.component';

describe('PaiementSanction Management Detail Component', () => {
  let comp: PaiementSanctionDetailComponent;
  let fixture: ComponentFixture<PaiementSanctionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaiementSanctionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ paiementSanction: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PaiementSanctionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PaiementSanctionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load paiementSanction on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.paiementSanction).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
