import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SanctionDetailComponent } from './sanction-detail.component';

describe('Sanction Management Detail Component', () => {
  let comp: SanctionDetailComponent;
  let fixture: ComponentFixture<SanctionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SanctionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sanction: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SanctionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SanctionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sanction on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sanction).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
