import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaiementTontineDetailComponent } from './paiement-tontine-detail.component';

describe('PaiementTontine Management Detail Component', () => {
  let comp: PaiementTontineDetailComponent;
  let fixture: ComponentFixture<PaiementTontineDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaiementTontineDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ paiementTontine: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PaiementTontineDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PaiementTontineDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load paiementTontine on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.paiementTontine).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
