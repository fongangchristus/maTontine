import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormuleAdhesionDetailComponent } from './formule-adhesion-detail.component';

describe('FormuleAdhesion Management Detail Component', () => {
  let comp: FormuleAdhesionDetailComponent;
  let fixture: ComponentFixture<FormuleAdhesionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormuleAdhesionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ formuleAdhesion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FormuleAdhesionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FormuleAdhesionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load formuleAdhesion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.formuleAdhesion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
