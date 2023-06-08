import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICotisationTontine } from '../cotisation-tontine.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../cotisation-tontine.test-samples';

import { CotisationTontineService, RestCotisationTontine } from './cotisation-tontine.service';

const requireRestSample: RestCotisationTontine = {
  ...sampleWithRequiredData,
  dateCotisation: sampleWithRequiredData.dateCotisation?.format(DATE_FORMAT),
  dateValidation: sampleWithRequiredData.dateValidation?.format(DATE_FORMAT),
};

describe('CotisationTontine Service', () => {
  let service: CotisationTontineService;
  let httpMock: HttpTestingController;
  let expectedResult: ICotisationTontine | ICotisationTontine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CotisationTontineService);
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

    it('should create a CotisationTontine', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const cotisationTontine = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cotisationTontine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CotisationTontine', () => {
      const cotisationTontine = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cotisationTontine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CotisationTontine', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CotisationTontine', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CotisationTontine', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCotisationTontineToCollectionIfMissing', () => {
      it('should add a CotisationTontine to an empty array', () => {
        const cotisationTontine: ICotisationTontine = sampleWithRequiredData;
        expectedResult = service.addCotisationTontineToCollectionIfMissing([], cotisationTontine);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cotisationTontine);
      });

      it('should not add a CotisationTontine to an array that contains it', () => {
        const cotisationTontine: ICotisationTontine = sampleWithRequiredData;
        const cotisationTontineCollection: ICotisationTontine[] = [
          {
            ...cotisationTontine,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCotisationTontineToCollectionIfMissing(cotisationTontineCollection, cotisationTontine);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CotisationTontine to an array that doesn't contain it", () => {
        const cotisationTontine: ICotisationTontine = sampleWithRequiredData;
        const cotisationTontineCollection: ICotisationTontine[] = [sampleWithPartialData];
        expectedResult = service.addCotisationTontineToCollectionIfMissing(cotisationTontineCollection, cotisationTontine);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cotisationTontine);
      });

      it('should add only unique CotisationTontine to an array', () => {
        const cotisationTontineArray: ICotisationTontine[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cotisationTontineCollection: ICotisationTontine[] = [sampleWithRequiredData];
        expectedResult = service.addCotisationTontineToCollectionIfMissing(cotisationTontineCollection, ...cotisationTontineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cotisationTontine: ICotisationTontine = sampleWithRequiredData;
        const cotisationTontine2: ICotisationTontine = sampleWithPartialData;
        expectedResult = service.addCotisationTontineToCollectionIfMissing([], cotisationTontine, cotisationTontine2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cotisationTontine);
        expect(expectedResult).toContain(cotisationTontine2);
      });

      it('should accept null and undefined values', () => {
        const cotisationTontine: ICotisationTontine = sampleWithRequiredData;
        expectedResult = service.addCotisationTontineToCollectionIfMissing([], null, cotisationTontine, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cotisationTontine);
      });

      it('should return initial array if no CotisationTontine is added', () => {
        const cotisationTontineCollection: ICotisationTontine[] = [sampleWithRequiredData];
        expectedResult = service.addCotisationTontineToCollectionIfMissing(cotisationTontineCollection, undefined, null);
        expect(expectedResult).toEqual(cotisationTontineCollection);
      });
    });

    describe('compareCotisationTontine', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCotisationTontine(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCotisationTontine(entity1, entity2);
        const compareResult2 = service.compareCotisationTontine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCotisationTontine(entity1, entity2);
        const compareResult2 = service.compareCotisationTontine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCotisationTontine(entity1, entity2);
        const compareResult2 = service.compareCotisationTontine(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
