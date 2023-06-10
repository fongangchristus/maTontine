import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITontine } from '../tontine.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tontine.test-samples';

import { TontineService, RestTontine } from './tontine.service';

const requireRestSample: RestTontine = {
  ...sampleWithRequiredData,
  dateCreation: sampleWithRequiredData.dateCreation?.format(DATE_FORMAT),
  datePremierTour: sampleWithRequiredData.datePremierTour?.format(DATE_FORMAT),
  dateDernierTour: sampleWithRequiredData.dateDernierTour?.format(DATE_FORMAT),
};

describe('Tontine Service', () => {
  let service: TontineService;
  let httpMock: HttpTestingController;
  let expectedResult: ITontine | ITontine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TontineService);
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

    it('should create a Tontine', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tontine = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tontine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tontine', () => {
      const tontine = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tontine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tontine', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tontine', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Tontine', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTontineToCollectionIfMissing', () => {
      it('should add a Tontine to an empty array', () => {
        const tontine: ITontine = sampleWithRequiredData;
        expectedResult = service.addTontineToCollectionIfMissing([], tontine);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tontine);
      });

      it('should not add a Tontine to an array that contains it', () => {
        const tontine: ITontine = sampleWithRequiredData;
        const tontineCollection: ITontine[] = [
          {
            ...tontine,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTontineToCollectionIfMissing(tontineCollection, tontine);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tontine to an array that doesn't contain it", () => {
        const tontine: ITontine = sampleWithRequiredData;
        const tontineCollection: ITontine[] = [sampleWithPartialData];
        expectedResult = service.addTontineToCollectionIfMissing(tontineCollection, tontine);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tontine);
      });

      it('should add only unique Tontine to an array', () => {
        const tontineArray: ITontine[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tontineCollection: ITontine[] = [sampleWithRequiredData];
        expectedResult = service.addTontineToCollectionIfMissing(tontineCollection, ...tontineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tontine: ITontine = sampleWithRequiredData;
        const tontine2: ITontine = sampleWithPartialData;
        expectedResult = service.addTontineToCollectionIfMissing([], tontine, tontine2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tontine);
        expect(expectedResult).toContain(tontine2);
      });

      it('should accept null and undefined values', () => {
        const tontine: ITontine = sampleWithRequiredData;
        expectedResult = service.addTontineToCollectionIfMissing([], null, tontine, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tontine);
      });

      it('should return initial array if no Tontine is added', () => {
        const tontineCollection: ITontine[] = [sampleWithRequiredData];
        expectedResult = service.addTontineToCollectionIfMissing(tontineCollection, undefined, null);
        expect(expectedResult).toEqual(tontineCollection);
      });
    });

    describe('compareTontine', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTontine(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTontine(entity1, entity2);
        const compareResult2 = service.compareTontine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTontine(entity1, entity2);
        const compareResult2 = service.compareTontine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTontine(entity1, entity2);
        const compareResult2 = service.compareTontine(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
