import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAssociation } from '../association.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../association.test-samples';

import { AssociationService, RestAssociation } from './association.service';

const requireRestSample: RestAssociation = {
  ...sampleWithRequiredData,
  dateCreation: sampleWithRequiredData.dateCreation?.format(DATE_FORMAT),
};

describe('Association Service', () => {
  let service: AssociationService;
  let httpMock: HttpTestingController;
  let expectedResult: IAssociation | IAssociation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssociationService);
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

    it('should create a Association', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const association = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(association).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Association', () => {
      const association = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(association).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Association', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Association', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Association', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAssociationToCollectionIfMissing', () => {
      it('should add a Association to an empty array', () => {
        const association: IAssociation = sampleWithRequiredData;
        expectedResult = service.addAssociationToCollectionIfMissing([], association);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(association);
      });

      it('should not add a Association to an array that contains it', () => {
        const association: IAssociation = sampleWithRequiredData;
        const associationCollection: IAssociation[] = [
          {
            ...association,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAssociationToCollectionIfMissing(associationCollection, association);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Association to an array that doesn't contain it", () => {
        const association: IAssociation = sampleWithRequiredData;
        const associationCollection: IAssociation[] = [sampleWithPartialData];
        expectedResult = service.addAssociationToCollectionIfMissing(associationCollection, association);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(association);
      });

      it('should add only unique Association to an array', () => {
        const associationArray: IAssociation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const associationCollection: IAssociation[] = [sampleWithRequiredData];
        expectedResult = service.addAssociationToCollectionIfMissing(associationCollection, ...associationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const association: IAssociation = sampleWithRequiredData;
        const association2: IAssociation = sampleWithPartialData;
        expectedResult = service.addAssociationToCollectionIfMissing([], association, association2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(association);
        expect(expectedResult).toContain(association2);
      });

      it('should accept null and undefined values', () => {
        const association: IAssociation = sampleWithRequiredData;
        expectedResult = service.addAssociationToCollectionIfMissing([], null, association, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(association);
      });

      it('should return initial array if no Association is added', () => {
        const associationCollection: IAssociation[] = [sampleWithRequiredData];
        expectedResult = service.addAssociationToCollectionIfMissing(associationCollection, undefined, null);
        expect(expectedResult).toEqual(associationCollection);
      });
    });

    describe('compareAssociation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAssociation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAssociation(entity1, entity2);
        const compareResult2 = service.compareAssociation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAssociation(entity1, entity2);
        const compareResult2 = service.compareAssociation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAssociation(entity1, entity2);
        const compareResult2 = service.compareAssociation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
