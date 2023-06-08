import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CommentairePotDetailComponent } from './commentaire-pot-detail.component';

describe('CommentairePot Management Detail Component', () => {
  let comp: CommentairePotDetailComponent;
  let fixture: ComponentFixture<CommentairePotDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CommentairePotDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ commentairePot: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CommentairePotDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CommentairePotDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load commentairePot on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.commentairePot).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
