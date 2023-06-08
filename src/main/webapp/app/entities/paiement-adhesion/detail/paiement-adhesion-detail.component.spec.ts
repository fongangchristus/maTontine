import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaiementAdhesionDetailComponent } from './paiement-adhesion-detail.component';

describe('PaiementAdhesion Management Detail Component', () => {
  let comp: PaiementAdhesionDetailComponent;
  let fixture: ComponentFixture<PaiementAdhesionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaiementAdhesionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ paiementAdhesion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PaiementAdhesionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PaiementAdhesionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load paiementAdhesion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.paiementAdhesion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
