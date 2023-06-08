import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypePot } from '../type-pot.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-pot.test-samples';

import { TypePotService } from './type-pot.service';

const requireRestSample: ITypePot = {
  ...sampleWithRequiredData,
};

describe('TypePot Service', () => {
  let service: TypePotService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypePot | ITypePot[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypePotService);
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

    it('should create a TypePot', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const typePot = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typePot).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypePot', () => {
      const typePot = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typePot).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypePot', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypePot', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypePot', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTypePotToCollectionIfMissing', () => {
      it('should add a TypePot to an empty array', () => {
        const typePot: ITypePot = sampleWithRequiredData;
        expectedResult = service.addTypePotToCollectionIfMissing([], typePot);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typePot);
      });

      it('should not add a TypePot to an array that contains it', () => {
        const typePot: ITypePot = sampleWithRequiredData;
        const typePotCollection: ITypePot[] = [
          {
            ...typePot,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypePotToCollectionIfMissing(typePotCollection, typePot);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypePot to an array that doesn't contain it", () => {
        const typePot: ITypePot = sampleWithRequiredData;
        const typePotCollection: ITypePot[] = [sampleWithPartialData];
        expectedResult = service.addTypePotToCollectionIfMissing(typePotCollection, typePot);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typePot);
      });

      it('should add only unique TypePot to an array', () => {
        const typePotArray: ITypePot[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typePotCollection: ITypePot[] = [sampleWithRequiredData];
        expectedResult = service.addTypePotToCollectionIfMissing(typePotCollection, ...typePotArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typePot: ITypePot = sampleWithRequiredData;
        const typePot2: ITypePot = sampleWithPartialData;
        expectedResult = service.addTypePotToCollectionIfMissing([], typePot, typePot2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typePot);
        expect(expectedResult).toContain(typePot2);
      });

      it('should accept null and undefined values', () => {
        const typePot: ITypePot = sampleWithRequiredData;
        expectedResult = service.addTypePotToCollectionIfMissing([], null, typePot, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typePot);
      });

      it('should return initial array if no TypePot is added', () => {
        const typePotCollection: ITypePot[] = [sampleWithRequiredData];
        expectedResult = service.addTypePotToCollectionIfMissing(typePotCollection, undefined, null);
        expect(expectedResult).toEqual(typePotCollection);
      });
    });

    describe('compareTypePot', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypePot(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypePot(entity1, entity2);
        const compareResult2 = service.compareTypePot(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypePot(entity1, entity2);
        const compareResult2 = service.compareTypePot(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypePot(entity1, entity2);
        const compareResult2 = service.compareTypePot(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
