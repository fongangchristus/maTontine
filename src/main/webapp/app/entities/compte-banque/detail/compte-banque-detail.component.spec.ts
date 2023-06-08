import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompteBanqueDetailComponent } from './compte-banque-detail.component';

describe('CompteBanque Management Detail Component', () => {
  let comp: CompteBanqueDetailComponent;
  let fixture: ComponentFixture<CompteBanqueDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompteBanqueDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ compteBanque: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CompteBanqueDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CompteBanqueDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load compteBanque on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.compteBanque).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
