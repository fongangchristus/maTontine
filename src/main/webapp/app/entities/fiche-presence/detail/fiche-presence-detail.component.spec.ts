import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FichePresenceDetailComponent } from './fiche-presence-detail.component';

describe('FichePresence Management Detail Component', () => {
  let comp: FichePresenceDetailComponent;
  let fixture: ComponentFixture<FichePresenceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FichePresenceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fichePresence: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FichePresenceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FichePresenceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fichePresence on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fichePresence).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
