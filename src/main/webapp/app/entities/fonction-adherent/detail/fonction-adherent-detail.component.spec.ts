import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FonctionAdherentDetailComponent } from './fonction-adherent-detail.component';

describe('FonctionAdherent Management Detail Component', () => {
  let comp: FonctionAdherentDetailComponent;
  let fixture: ComponentFixture<FonctionAdherentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FonctionAdherentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fonctionAdherent: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FonctionAdherentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FonctionAdherentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fonctionAdherent on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fonctionAdherent).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
