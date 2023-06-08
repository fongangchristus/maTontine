import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TontineDetailComponent } from './tontine-detail.component';

describe('Tontine Management Detail Component', () => {
  let comp: TontineDetailComponent;
  let fixture: ComponentFixture<TontineDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TontineDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tontine: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TontineDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TontineDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tontine on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tontine).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
