import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICompteTontine } from '../compte-tontine.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../compte-tontine.test-samples';

import { CompteTontineService } from './compte-tontine.service';

const requireRestSample: ICompteTontine = {
  ...sampleWithRequiredData,
};

describe('CompteTontine Service', () => {
  let service: CompteTontineService;
  let httpMock: HttpTestingController;
  let expectedResult: ICompteTontine | ICompteTontine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CompteTontineService);
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

    it('should create a CompteTontine', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const compteTontine = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(compteTontine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CompteTontine', () => {
      const compteTontine = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(compteTontine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CompteTontine', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CompteTontine', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CompteTontine', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCompteTontineToCollectionIfMissing', () => {
      it('should add a CompteTontine to an empty array', () => {
        const compteTontine: ICompteTontine = sampleWithRequiredData;
        expectedResult = service.addCompteTontineToCollectionIfMissing([], compteTontine);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compteTontine);
      });

      it('should not add a CompteTontine to an array that contains it', () => {
        const compteTontine: ICompteTontine = sampleWithRequiredData;
        const compteTontineCollection: ICompteTontine[] = [
          {
            ...compteTontine,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCompteTontineToCollectionIfMissing(compteTontineCollection, compteTontine);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CompteTontine to an array that doesn't contain it", () => {
        const compteTontine: ICompteTontine = sampleWithRequiredData;
        const compteTontineCollection: ICompteTontine[] = [sampleWithPartialData];
        expectedResult = service.addCompteTontineToCollectionIfMissing(compteTontineCollection, compteTontine);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compteTontine);
      });

      it('should add only unique CompteTontine to an array', () => {
        const compteTontineArray: ICompteTontine[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const compteTontineCollection: ICompteTontine[] = [sampleWithRequiredData];
        expectedResult = service.addCompteTontineToCollectionIfMissing(compteTontineCollection, ...compteTontineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const compteTontine: ICompteTontine = sampleWithRequiredData;
        const compteTontine2: ICompteTontine = sampleWithPartialData;
        expectedResult = service.addCompteTontineToCollectionIfMissing([], compteTontine, compteTontine2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compteTontine);
        expect(expectedResult).toContain(compteTontine2);
      });

      it('should accept null and undefined values', () => {
        const compteTontine: ICompteTontine = sampleWithRequiredData;
        expectedResult = service.addCompteTontineToCollectionIfMissing([], null, compteTontine, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compteTontine);
      });

      it('should return initial array if no CompteTontine is added', () => {
        const compteTontineCollection: ICompteTontine[] = [sampleWithRequiredData];
        expectedResult = service.addCompteTontineToCollectionIfMissing(compteTontineCollection, undefined, null);
        expect(expectedResult).toEqual(compteTontineCollection);
      });
    });

    describe('compareCompteTontine', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCompteTontine(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCompteTontine(entity1, entity2);
        const compareResult2 = service.compareCompteTontine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCompteTontine(entity1, entity2);
        const compareResult2 = service.compareCompteTontine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCompteTontine(entity1, entity2);
        const compareResult2 = service.compareCompteTontine(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
