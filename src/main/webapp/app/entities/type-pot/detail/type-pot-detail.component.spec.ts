import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TypePotDetailComponent } from './type-pot-detail.component';

describe('TypePot Management Detail Component', () => {
  let comp: TypePotDetailComponent;
  let fixture: ComponentFixture<TypePotDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TypePotDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ typePot: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TypePotDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TypePotDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load typePot on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.typePot).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
