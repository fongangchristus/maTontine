import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeEvenement } from '../type-evenement.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-evenement.test-samples';

import { TypeEvenementService } from './type-evenement.service';

const requireRestSample: ITypeEvenement = {
  ...sampleWithRequiredData,
};

describe('TypeEvenement Service', () => {
  let service: TypeEvenementService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeEvenement | ITypeEvenement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeEvenementService);
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

    it('should create a TypeEvenement', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const typeEvenement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeEvenement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeEvenement', () => {
      const typeEvenement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeEvenement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeEvenement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeEvenement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeEvenement', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTypeEvenementToCollectionIfMissing', () => {
      it('should add a TypeEvenement to an empty array', () => {
        const typeEvenement: ITypeEvenement = sampleWithRequiredData;
        expectedResult = service.addTypeEvenementToCollectionIfMissing([], typeEvenement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeEvenement);
      });

      it('should not add a TypeEvenement to an array that contains it', () => {
        const typeEvenement: ITypeEvenement = sampleWithRequiredData;
        const typeEvenementCollection: ITypeEvenement[] = [
          {
            ...typeEvenement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeEvenementToCollectionIfMissing(typeEvenementCollection, typeEvenement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeEvenement to an array that doesn't contain it", () => {
        const typeEvenement: ITypeEvenement = sampleWithRequiredData;
        const typeEvenementCollection: ITypeEvenement[] = [sampleWithPartialData];
        expectedResult = service.addTypeEvenementToCollectionIfMissing(typeEvenementCollection, typeEvenement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeEvenement);
      });

      it('should add only unique TypeEvenement to an array', () => {
        const typeEvenementArray: ITypeEvenement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeEvenementCollection: ITypeEvenement[] = [sampleWithRequiredData];
        expectedResult = service.addTypeEvenementToCollectionIfMissing(typeEvenementCollection, ...typeEvenementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeEvenement: ITypeEvenement = sampleWithRequiredData;
        const typeEvenement2: ITypeEvenement = sampleWithPartialData;
        expectedResult = service.addTypeEvenementToCollectionIfMissing([], typeEvenement, typeEvenement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeEvenement);
        expect(expectedResult).toContain(typeEvenement2);
      });

      it('should accept null and undefined values', () => {
        const typeEvenement: ITypeEvenement = sampleWithRequiredData;
        expectedResult = service.addTypeEvenementToCollectionIfMissing([], null, typeEvenement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeEvenement);
      });

      it('should return initial array if no TypeEvenement is added', () => {
        const typeEvenementCollection: ITypeEvenement[] = [sampleWithRequiredData];
        expectedResult = service.addTypeEvenementToCollectionIfMissing(typeEvenementCollection, undefined, null);
        expect(expectedResult).toEqual(typeEvenementCollection);
      });
    });

    describe('compareTypeEvenement', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeEvenement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypeEvenement(entity1, entity2);
        const compareResult2 = service.compareTypeEvenement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypeEvenement(entity1, entity2);
        const compareResult2 = service.compareTypeEvenement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypeEvenement(entity1, entity2);
        const compareResult2 = service.compareTypeEvenement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
