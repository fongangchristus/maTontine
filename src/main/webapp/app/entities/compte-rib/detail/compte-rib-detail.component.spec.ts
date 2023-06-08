import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompteRIBDetailComponent } from './compte-rib-detail.component';

describe('CompteRIB Management Detail Component', () => {
  let comp: CompteRIBDetailComponent;
  let fixture: ComponentFixture<CompteRIBDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompteRIBDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ compteRIB: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CompteRIBDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CompteRIBDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load compteRIB on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.compteRIB).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
