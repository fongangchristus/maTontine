import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDocumentAssociation } from '../document-association.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../document-association.test-samples';

import { DocumentAssociationService, RestDocumentAssociation } from './document-association.service';

const requireRestSample: RestDocumentAssociation = {
  ...sampleWithRequiredData,
  dateEnregistrement: sampleWithRequiredData.dateEnregistrement?.format(DATE_FORMAT),
  dateArchivage: sampleWithRequiredData.dateArchivage?.format(DATE_FORMAT),
};

describe('DocumentAssociation Service', () => {
  let service: DocumentAssociationService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentAssociation | IDocumentAssociation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentAssociationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a DocumentAssociation', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const documentAssociation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentAssociation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentAssociation', () => {
      const documentAssociation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentAssociation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentAssociation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentAssociation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentAssociation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDocumentAssociationToCollectionIfMissing', () => {
      it('should add a DocumentAssociation to an empty array', () => {
        const documentAssociation: IDocumentAssociation = sampleWithRequiredData;
        expectedResult = service.addDocumentAssociationToCollectionIfMissing([], documentAssociation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentAssociation);
      });

      it('should not add a DocumentAssociation to an array that contains it', () => {
        const documentAssociation: IDocumentAssociation = sampleWithRequiredData;
        const documentAssociationCollection: IDocumentAssociation[] = [
          {
            ...documentAssociation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentAssociationToCollectionIfMissing(documentAssociationCollection, documentAssociation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentAssociation to an array that doesn't contain it", () => {
        const documentAssociation: IDocumentAssociation = sampleWithRequiredData;
        const documentAssociationCollection: IDocumentAssociation[] = [sampleWithPartialData];
        expectedResult = service.addDocumentAssociationToCollectionIfMissing(documentAssociationCollection, documentAssociation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentAssociation);
      });

      it('should add only unique DocumentAssociation to an array', () => {
        const documentAssociationArray: IDocumentAssociation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentAssociationCollection: IDocumentAssociation[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentAssociationToCollectionIfMissing(documentAssociationCollection, ...documentAssociationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentAssociation: IDocumentAssociation = sampleWithRequiredData;
        const documentAssociation2: IDocumentAssociation = sampleWithPartialData;
        expectedResult = service.addDocumentAssociationToCollectionIfMissing([], documentAssociation, documentAssociation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentAssociation);
        expect(expectedResult).toContain(documentAssociation2);
      });

      it('should accept null and undefined values', () => {
        const documentAssociation: IDocumentAssociation = sampleWithRequiredData;
        expectedResult = service.addDocumentAssociationToCollectionIfMissing([], null, documentAssociation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentAssociation);
      });

      it('should return initial array if no DocumentAssociation is added', () => {
        const documentAssociationCollection: IDocumentAssociation[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentAssociationToCollectionIfMissing(documentAssociationCollection, undefined, null);
        expect(expectedResult).toEqual(documentAssociationCollection);
      });
    });

    describe('compareDocumentAssociation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentAssociation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentAssociation(entity1, entity2);
        const compareResult2 = service.compareDocumentAssociation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDocumentAssociation(entity1, entity2);
        const compareResult2 = service.compareDocumentAssociation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDocumentAssociation(entity1, entity2);
        const compareResult2 = service.compareDocumentAssociation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
