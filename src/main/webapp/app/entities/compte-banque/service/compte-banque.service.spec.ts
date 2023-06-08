import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICompteBanque } from '../compte-banque.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../compte-banque.test-samples';

import { CompteBanqueService } from './compte-banque.service';

const requireRestSample: ICompteBanque = {
  ...sampleWithRequiredData,
};

describe('CompteBanque Service', () => {
  let service: CompteBanqueService;
  let httpMock: HttpTestingController;
  let expectedResult: ICompteBanque | ICompteBanque[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CompteBanqueService);
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

    it('should create a CompteBanque', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const compteBanque = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(compteBanque).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CompteBanque', () => {
      const compteBanque = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(compteBanque).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CompteBanque', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CompteBanque', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CompteBanque', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCompteBanqueToCollectionIfMissing', () => {
      it('should add a CompteBanque to an empty array', () => {
        const compteBanque: ICompteBanque = sampleWithRequiredData;
        expectedResult = service.addCompteBanqueToCollectionIfMissing([], compteBanque);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compteBanque);
      });

      it('should not add a CompteBanque to an array that contains it', () => {
        const compteBanque: ICompteBanque = sampleWithRequiredData;
        const compteBanqueCollection: ICompteBanque[] = [
          {
            ...compteBanque,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCompteBanqueToCollectionIfMissing(compteBanqueCollection, compteBanque);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CompteBanque to an array that doesn't contain it", () => {
        const compteBanque: ICompteBanque = sampleWithRequiredData;
        const compteBanqueCollection: ICompteBanque[] = [sampleWithPartialData];
        expectedResult = service.addCompteBanqueToCollectionIfMissing(compteBanqueCollection, compteBanque);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compteBanque);
      });

      it('should add only unique CompteBanque to an array', () => {
        const compteBanqueArray: ICompteBanque[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const compteBanqueCollection: ICompteBanque[] = [sampleWithRequiredData];
        expectedResult = service.addCompteBanqueToCollectionIfMissing(compteBanqueCollection, ...compteBanqueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const compteBanque: ICompteBanque = sampleWithRequiredData;
        const compteBanque2: ICompteBanque = sampleWithPartialData;
        expectedResult = service.addCompteBanqueToCollectionIfMissing([], compteBanque, compteBanque2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compteBanque);
        expect(expectedResult).toContain(compteBanque2);
      });

      it('should accept null and undefined values', () => {
        const compteBanque: ICompteBanque = sampleWithRequiredData;
        expectedResult = service.addCompteBanqueToCollectionIfMissing([], null, compteBanque, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compteBanque);
      });

      it('should return initial array if no CompteBanque is added', () => {
        const compteBanqueCollection: ICompteBanque[] = [sampleWithRequiredData];
        expectedResult = service.addCompteBanqueToCollectionIfMissing(compteBanqueCollection, undefined, null);
        expect(expectedResult).toEqual(compteBanqueCollection);
      });
    });

    describe('compareCompteBanque', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCompteBanque(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCompteBanque(entity1, entity2);
        const compareResult2 = service.compareCompteBanque(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCompteBanque(entity1, entity2);
        const compareResult2 = service.compareCompteBanque(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCompteBanque(entity1, entity2);
        const compareResult2 = service.compareCompteBanque(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
