import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICompteRIB } from '../compte-rib.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../compte-rib.test-samples';

import { CompteRIBService } from './compte-rib.service';

const requireRestSample: ICompteRIB = {
  ...sampleWithRequiredData,
};

describe('CompteRIB Service', () => {
  let service: CompteRIBService;
  let httpMock: HttpTestingController;
  let expectedResult: ICompteRIB | ICompteRIB[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CompteRIBService);
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

    it('should create a CompteRIB', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const compteRIB = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(compteRIB).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CompteRIB', () => {
      const compteRIB = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(compteRIB).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CompteRIB', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CompteRIB', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CompteRIB', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCompteRIBToCollectionIfMissing', () => {
      it('should add a CompteRIB to an empty array', () => {
        const compteRIB: ICompteRIB = sampleWithRequiredData;
        expectedResult = service.addCompteRIBToCollectionIfMissing([], compteRIB);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compteRIB);
      });

      it('should not add a CompteRIB to an array that contains it', () => {
        const compteRIB: ICompteRIB = sampleWithRequiredData;
        const compteRIBCollection: ICompteRIB[] = [
          {
            ...compteRIB,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCompteRIBToCollectionIfMissing(compteRIBCollection, compteRIB);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CompteRIB to an array that doesn't contain it", () => {
        const compteRIB: ICompteRIB = sampleWithRequiredData;
        const compteRIBCollection: ICompteRIB[] = [sampleWithPartialData];
        expectedResult = service.addCompteRIBToCollectionIfMissing(compteRIBCollection, compteRIB);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compteRIB);
      });

      it('should add only unique CompteRIB to an array', () => {
        const compteRIBArray: ICompteRIB[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const compteRIBCollection: ICompteRIB[] = [sampleWithRequiredData];
        expectedResult = service.addCompteRIBToCollectionIfMissing(compteRIBCollection, ...compteRIBArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const compteRIB: ICompteRIB = sampleWithRequiredData;
        const compteRIB2: ICompteRIB = sampleWithPartialData;
        expectedResult = service.addCompteRIBToCollectionIfMissing([], compteRIB, compteRIB2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compteRIB);
        expect(expectedResult).toContain(compteRIB2);
      });

      it('should accept null and undefined values', () => {
        const compteRIB: ICompteRIB = sampleWithRequiredData;
        expectedResult = service.addCompteRIBToCollectionIfMissing([], null, compteRIB, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compteRIB);
      });

      it('should return initial array if no CompteRIB is added', () => {
        const compteRIBCollection: ICompteRIB[] = [sampleWithRequiredData];
        expectedResult = service.addCompteRIBToCollectionIfMissing(compteRIBCollection, undefined, null);
        expect(expectedResult).toEqual(compteRIBCollection);
      });
    });

    describe('compareCompteRIB', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCompteRIB(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCompteRIB(entity1, entity2);
        const compareResult2 = service.compareCompteRIB(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCompteRIB(entity1, entity2);
        const compareResult2 = service.compareCompteRIB(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCompteRIB(entity1, entity2);
        const compareResult2 = service.compareCompteRIB(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
