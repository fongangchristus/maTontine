import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SessionTontineDetailComponent } from './session-tontine-detail.component';

describe('SessionTontine Management Detail Component', () => {
  let comp: SessionTontineDetailComponent;
  let fixture: ComponentFixture<SessionTontineDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SessionTontineDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sessionTontine: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SessionTontineDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SessionTontineDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sessionTontine on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sessionTontine).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
