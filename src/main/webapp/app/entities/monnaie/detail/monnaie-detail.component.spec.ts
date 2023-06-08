import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MonnaieDetailComponent } from './monnaie-detail.component';

describe('Monnaie Management Detail Component', () => {
  let comp: MonnaieDetailComponent;
  let fixture: ComponentFixture<MonnaieDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MonnaieDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ monnaie: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MonnaieDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MonnaieDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load monnaie on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.monnaie).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
