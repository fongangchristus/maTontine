import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompteTontineDetailComponent } from './compte-tontine-detail.component';

describe('CompteTontine Management Detail Component', () => {
  let comp: CompteTontineDetailComponent;
  let fixture: ComponentFixture<CompteTontineDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompteTontineDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ compteTontine: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CompteTontineDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CompteTontineDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load compteTontine on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.compteTontine).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
