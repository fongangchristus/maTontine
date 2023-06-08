import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISessionTontine } from '../session-tontine.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../session-tontine.test-samples';

import { SessionTontineService, RestSessionTontine } from './session-tontine.service';

const requireRestSample: RestSessionTontine = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.format(DATE_FORMAT),
  dateFin: sampleWithRequiredData.dateFin?.format(DATE_FORMAT),
};

describe('SessionTontine Service', () => {
  let service: SessionTontineService;
  let httpMock: HttpTestingController;
  let expectedResult: ISessionTontine | ISessionTontine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SessionTontineService);
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

    it('should create a SessionTontine', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const sessionTontine = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sessionTontine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SessionTontine', () => {
      const sessionTontine = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sessionTontine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SessionTontine', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SessionTontine', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SessionTontine', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSessionTontineToCollectionIfMissing', () => {
      it('should add a SessionTontine to an empty array', () => {
        const sessionTontine: ISessionTontine = sampleWithRequiredData;
        expectedResult = service.addSessionTontineToCollectionIfMissing([], sessionTontine);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sessionTontine);
      });

      it('should not add a SessionTontine to an array that contains it', () => {
        const sessionTontine: ISessionTontine = sampleWithRequiredData;
        const sessionTontineCollection: ISessionTontine[] = [
          {
            ...sessionTontine,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSessionTontineToCollectionIfMissing(sessionTontineCollection, sessionTontine);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SessionTontine to an array that doesn't contain it", () => {
        const sessionTontine: ISessionTontine = sampleWithRequiredData;
        const sessionTontineCollection: ISessionTontine[] = [sampleWithPartialData];
        expectedResult = service.addSessionTontineToCollectionIfMissing(sessionTontineCollection, sessionTontine);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sessionTontine);
      });

      it('should add only unique SessionTontine to an array', () => {
        const sessionTontineArray: ISessionTontine[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sessionTontineCollection: ISessionTontine[] = [sampleWithRequiredData];
        expectedResult = service.addSessionTontineToCollectionIfMissing(sessionTontineCollection, ...sessionTontineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sessionTontine: ISessionTontine = sampleWithRequiredData;
        const sessionTontine2: ISessionTontine = sampleWithPartialData;
        expectedResult = service.addSessionTontineToCollectionIfMissing([], sessionTontine, sessionTontine2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sessionTontine);
        expect(expectedResult).toContain(sessionTontine2);
      });

      it('should accept null and undefined values', () => {
        const sessionTontine: ISessionTontine = sampleWithRequiredData;
        expectedResult = service.addSessionTontineToCollectionIfMissing([], null, sessionTontine, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sessionTontine);
      });

      it('should return initial array if no SessionTontine is added', () => {
        const sessionTontineCollection: ISessionTontine[] = [sampleWithRequiredData];
        expectedResult = service.addSessionTontineToCollectionIfMissing(sessionTontineCollection, undefined, null);
        expect(expectedResult).toEqual(sessionTontineCollection);
      });
    });

    describe('compareSessionTontine', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSessionTontine(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSessionTontine(entity1, entity2);
        const compareResult2 = service.compareSessionTontine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSessionTontine(entity1, entity2);
        const compareResult2 = service.compareSessionTontine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSessionTontine(entity1, entity2);
        const compareResult2 = service.compareSessionTontine(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
