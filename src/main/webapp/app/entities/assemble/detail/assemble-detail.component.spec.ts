import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssembleDetailComponent } from './assemble-detail.component';

describe('Assemble Management Detail Component', () => {
  let comp: AssembleDetailComponent;
  let fixture: ComponentFixture<AssembleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssembleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ assemble: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssembleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssembleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assemble on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.assemble).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
