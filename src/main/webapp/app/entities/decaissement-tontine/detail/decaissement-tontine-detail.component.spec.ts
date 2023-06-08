import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DecaissementTontineDetailComponent } from './decaissement-tontine-detail.component';

describe('DecaissementTontine Management Detail Component', () => {
  let comp: DecaissementTontineDetailComponent;
  let fixture: ComponentFixture<DecaissementTontineDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DecaissementTontineDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ decaissementTontine: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DecaissementTontineDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DecaissementTontineDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load decaissementTontine on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.decaissementTontine).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
