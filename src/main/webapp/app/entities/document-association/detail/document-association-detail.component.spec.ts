import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentAssociationDetailComponent } from './document-association-detail.component';

describe('DocumentAssociation Management Detail Component', () => {
  let comp: DocumentAssociationDetailComponent;
  let fixture: ComponentFixture<DocumentAssociationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DocumentAssociationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ documentAssociation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DocumentAssociationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DocumentAssociationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load documentAssociation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.documentAssociation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
