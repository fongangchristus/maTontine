import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDecaissementTontine } from '../decaissement-tontine.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../decaissement-tontine.test-samples';

import { DecaissementTontineService, RestDecaissementTontine } from './decaissement-tontine.service';

const requireRestSample: RestDecaissementTontine = {
  ...sampleWithRequiredData,
  dateDecaissement: sampleWithRequiredData.dateDecaissement?.format(DATE_FORMAT),
};

describe('DecaissementTontine Service', () => {
  let service: DecaissementTontineService;
  let httpMock: HttpTestingController;
  let expectedResult: IDecaissementTontine | IDecaissementTontine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DecaissementTontineService);
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

    it('should create a DecaissementTontine', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const decaissementTontine = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(decaissementTontine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DecaissementTontine', () => {
      const decaissementTontine = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(decaissementTontine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DecaissementTontine', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DecaissementTontine', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DecaissementTontine', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDecaissementTontineToCollectionIfMissing', () => {
      it('should add a DecaissementTontine to an empty array', () => {
        const decaissementTontine: IDecaissementTontine = sampleWithRequiredData;
        expectedResult = service.addDecaissementTontineToCollectionIfMissing([], decaissementTontine);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(decaissementTontine);
      });

      it('should not add a DecaissementTontine to an array that contains it', () => {
        const decaissementTontine: IDecaissementTontine = sampleWithRequiredData;
        const decaissementTontineCollection: IDecaissementTontine[] = [
          {
            ...decaissementTontine,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDecaissementTontineToCollectionIfMissing(decaissementTontineCollection, decaissementTontine);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DecaissementTontine to an array that doesn't contain it", () => {
        const decaissementTontine: IDecaissementTontine = sampleWithRequiredData;
        const decaissementTontineCollection: IDecaissementTontine[] = [sampleWithPartialData];
        expectedResult = service.addDecaissementTontineToCollectionIfMissing(decaissementTontineCollection, decaissementTontine);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(decaissementTontine);
      });

      it('should add only unique DecaissementTontine to an array', () => {
        const decaissementTontineArray: IDecaissementTontine[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const decaissementTontineCollection: IDecaissementTontine[] = [sampleWithRequiredData];
        expectedResult = service.addDecaissementTontineToCollectionIfMissing(decaissementTontineCollection, ...decaissementTontineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const decaissementTontine: IDecaissementTontine = sampleWithRequiredData;
        const decaissementTontine2: IDecaissementTontine = sampleWithPartialData;
        expectedResult = service.addDecaissementTontineToCollectionIfMissing([], decaissementTontine, decaissementTontine2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(decaissementTontine);
        expect(expectedResult).toContain(decaissementTontine2);
      });

      it('should accept null and undefined values', () => {
        const decaissementTontine: IDecaissementTontine = sampleWithRequiredData;
        expectedResult = service.addDecaissementTontineToCollectionIfMissing([], null, decaissementTontine, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(decaissementTontine);
      });

      it('should return initial array if no DecaissementTontine is added', () => {
        const decaissementTontineCollection: IDecaissementTontine[] = [sampleWithRequiredData];
        expectedResult = service.addDecaissementTontineToCollectionIfMissing(decaissementTontineCollection, undefined, null);
        expect(expectedResult).toEqual(decaissementTontineCollection);
      });
    });

    describe('compareDecaissementTontine', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDecaissementTontine(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDecaissementTontine(entity1, entity2);
        const compareResult2 = service.compareDecaissementTontine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDecaissementTontine(entity1, entity2);
        const compareResult2 = service.compareDecaissementTontine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDecaissementTontine(entity1, entity2);
        const compareResult2 = service.compareDecaissementTontine(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
