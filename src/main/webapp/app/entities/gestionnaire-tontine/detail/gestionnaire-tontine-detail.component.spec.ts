import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GestionnaireTontineDetailComponent } from './gestionnaire-tontine-detail.component';

describe('GestionnaireTontine Management Detail Component', () => {
  let comp: GestionnaireTontineDetailComponent;
  let fixture: ComponentFixture<GestionnaireTontineDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GestionnaireTontineDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ gestionnaireTontine: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GestionnaireTontineDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GestionnaireTontineDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load gestionnaireTontine on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.gestionnaireTontine).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
