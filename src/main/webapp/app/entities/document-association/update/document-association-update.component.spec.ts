import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DocumentAssociationFormService } from './document-association-form.service';
import { DocumentAssociationService } from '../service/document-association.service';
import { IDocumentAssociation } from '../document-association.model';
import { IAssociation } from 'app/entities/association/association.model';
import { AssociationService } from 'app/entities/association/service/association.service';

import { DocumentAssociationUpdateComponent } from './document-association-update.component';

describe('DocumentAssociation Management Update Component', () => {
  let comp: DocumentAssociationUpdateComponent;
  let fixture: ComponentFixture<DocumentAssociationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentAssociationFormService: DocumentAssociationFormService;
  let documentAssociationService: DocumentAssociationService;
  let associationService: AssociationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DocumentAssociationUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DocumentAssociationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentAssociationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentAssociationFormService = TestBed.inject(DocumentAssociationFormService);
    documentAssociationService = TestBed.inject(DocumentAssociationService);
    associationService = TestBed.inject(AssociationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Association query and add missing value', () => {
      const documentAssociation: IDocumentAssociation = { id: 456 };
      const association: IAssociation = { id: 87391 };
      documentAssociation.association = association;

      const associationCollection: IAssociation[] = [{ id: 69843 }];
      jest.spyOn(associationService, 'query').mockReturnValue(of(new HttpResponse({ body: associationCollection })));
      const additionalAssociations = [association];
      const expectedCollection: IAssociation[] = [...additionalAssociations, ...associationCollection];
      jest.spyOn(associationService, 'addAssociationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentAssociation });
      comp.ngOnInit();

      expect(associationService.query).toHaveBeenCalled();
      expect(associationService.addAssociationToCollectionIfMissing).toHaveBeenCalledWith(
        associationCollection,
        ...additionalAssociations.map(expect.objectContaining)
      );
      expect(comp.associationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const documentAssociation: IDocumentAssociation = { id: 456 };
      const association: IAssociation = { id: 45498 };
      documentAssociation.association = association;

      activatedRoute.data = of({ documentAssociation });
      comp.ngOnInit();

      expect(comp.associationsSharedCollection).toContain(association);
      expect(comp.documentAssociation).toEqual(documentAssociation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentAssociation>>();
      const documentAssociation = { id: 123 };
      jest.spyOn(documentAssociationFormService, 'getDocumentAssociation').mockReturnValue(documentAssociation);
      jest.spyOn(documentAssociationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentAssociation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentAssociation }));
      saveSubject.complete();

      // THEN
      expect(documentAssociationFormService.getDocumentAssociation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentAssociationService.update).toHaveBeenCalledWith(expect.objectContaining(documentAssociation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentAssociation>>();
      const documentAssociation = { id: 123 };
      jest.spyOn(documentAssociationFormService, 'getDocumentAssociation').mockReturnValue({ id: null });
      jest.spyOn(documentAssociationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentAssociation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentAssociation }));
      saveSubject.complete();

      // THEN
      expect(documentAssociationFormService.getDocumentAssociation).toHaveBeenCalled();
      expect(documentAssociationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentAssociation>>();
      const documentAssociation = { id: 123 };
      jest.spyOn(documentAssociationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentAssociation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentAssociationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAssociation', () => {
      it('Should forward to associationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(associationService, 'compareAssociation');
        comp.compareAssociation(entity, entity2);
        expect(associationService.compareAssociation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
