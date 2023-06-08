import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GestionnaireBanqueDetailComponent } from './gestionnaire-banque-detail.component';

describe('GestionnaireBanque Management Detail Component', () => {
  let comp: GestionnaireBanqueDetailComponent;
  let fixture: ComponentFixture<GestionnaireBanqueDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GestionnaireBanqueDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ gestionnaireBanque: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GestionnaireBanqueDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GestionnaireBanqueDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load gestionnaireBanque on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.gestionnaireBanque).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
