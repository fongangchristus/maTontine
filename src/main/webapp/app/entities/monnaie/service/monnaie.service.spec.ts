import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMonnaie } from '../monnaie.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../monnaie.test-samples';

import { MonnaieService } from './monnaie.service';

const requireRestSample: IMonnaie = {
  ...sampleWithRequiredData,
};

describe('Monnaie Service', () => {
  let service: MonnaieService;
  let httpMock: HttpTestingController;
  let expectedResult: IMonnaie | IMonnaie[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MonnaieService);
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

    it('should create a Monnaie', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const monnaie = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(monnaie).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Monnaie', () => {
      const monnaie = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(monnaie).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Monnaie', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Monnaie', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Monnaie', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMonnaieToCollectionIfMissing', () => {
      it('should add a Monnaie to an empty array', () => {
        const monnaie: IMonnaie = sampleWithRequiredData;
        expectedResult = service.addMonnaieToCollectionIfMissing([], monnaie);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monnaie);
      });

      it('should not add a Monnaie to an array that contains it', () => {
        const monnaie: IMonnaie = sampleWithRequiredData;
        const monnaieCollection: IMonnaie[] = [
          {
            ...monnaie,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMonnaieToCollectionIfMissing(monnaieCollection, monnaie);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Monnaie to an array that doesn't contain it", () => {
        const monnaie: IMonnaie = sampleWithRequiredData;
        const monnaieCollection: IMonnaie[] = [sampleWithPartialData];
        expectedResult = service.addMonnaieToCollectionIfMissing(monnaieCollection, monnaie);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monnaie);
      });

      it('should add only unique Monnaie to an array', () => {
        const monnaieArray: IMonnaie[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const monnaieCollection: IMonnaie[] = [sampleWithRequiredData];
        expectedResult = service.addMonnaieToCollectionIfMissing(monnaieCollection, ...monnaieArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const monnaie: IMonnaie = sampleWithRequiredData;
        const monnaie2: IMonnaie = sampleWithPartialData;
        expectedResult = service.addMonnaieToCollectionIfMissing([], monnaie, monnaie2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monnaie);
        expect(expectedResult).toContain(monnaie2);
      });

      it('should accept null and undefined values', () => {
        const monnaie: IMonnaie = sampleWithRequiredData;
        expectedResult = service.addMonnaieToCollectionIfMissing([], null, monnaie, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monnaie);
      });

      it('should return initial array if no Monnaie is added', () => {
        const monnaieCollection: IMonnaie[] = [sampleWithRequiredData];
        expectedResult = service.addMonnaieToCollectionIfMissing(monnaieCollection, undefined, null);
        expect(expectedResult).toEqual(monnaieCollection);
      });
    });

    describe('compareMonnaie', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMonnaie(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMonnaie(entity1, entity2);
        const compareResult2 = service.compareMonnaie(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMonnaie(entity1, entity2);
        const compareResult2 = service.compareMonnaie(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMonnaie(entity1, entity2);
        const compareResult2 = service.compareMonnaie(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
