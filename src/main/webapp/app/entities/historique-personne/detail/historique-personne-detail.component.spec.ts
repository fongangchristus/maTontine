import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HistoriquePersonneDetailComponent } from './historique-personne-detail.component';

describe('HistoriquePersonne Management Detail Component', () => {
  let comp: HistoriquePersonneDetailComponent;
  let fixture: ComponentFixture<HistoriquePersonneDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HistoriquePersonneDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ historiquePersonne: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HistoriquePersonneDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HistoriquePersonneDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load historiquePersonne on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.historiquePersonne).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
