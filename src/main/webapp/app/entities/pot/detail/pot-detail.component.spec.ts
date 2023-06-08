import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PotDetailComponent } from './pot-detail.component';

describe('Pot Management Detail Component', () => {
  let comp: PotDetailComponent;
  let fixture: ComponentFixture<PotDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PotDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pot: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PotDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PotDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pot on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pot).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
