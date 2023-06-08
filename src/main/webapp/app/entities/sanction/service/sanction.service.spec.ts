import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISanction } from '../sanction.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sanction.test-samples';

import { SanctionService, RestSanction } from './sanction.service';

const requireRestSample: RestSanction = {
  ...sampleWithRequiredData,
  dateSanction: sampleWithRequiredData.dateSanction?.format(DATE_FORMAT),
};

describe('Sanction Service', () => {
  let service: SanctionService;
  let httpMock: HttpTestingController;
  let expectedResult: ISanction | ISanction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SanctionService);
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

    it('should create a Sanction', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const sanction = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sanction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sanction', () => {
      const sanction = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sanction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sanction', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sanction', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Sanction', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSanctionToCollectionIfMissing', () => {
      it('should add a Sanction to an empty array', () => {
        const sanction: ISanction = sampleWithRequiredData;
        expectedResult = service.addSanctionToCollectionIfMissing([], sanction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sanction);
      });

      it('should not add a Sanction to an array that contains it', () => {
        const sanction: ISanction = sampleWithRequiredData;
        const sanctionCollection: ISanction[] = [
          {
            ...sanction,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSanctionToCollectionIfMissing(sanctionCollection, sanction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sanction to an array that doesn't contain it", () => {
        const sanction: ISanction = sampleWithRequiredData;
        const sanctionCollection: ISanction[] = [sampleWithPartialData];
        expectedResult = service.addSanctionToCollectionIfMissing(sanctionCollection, sanction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sanction);
      });

      it('should add only unique Sanction to an array', () => {
        const sanctionArray: ISanction[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sanctionCollection: ISanction[] = [sampleWithRequiredData];
        expectedResult = service.addSanctionToCollectionIfMissing(sanctionCollection, ...sanctionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sanction: ISanction = sampleWithRequiredData;
        const sanction2: ISanction = sampleWithPartialData;
        expectedResult = service.addSanctionToCollectionIfMissing([], sanction, sanction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sanction);
        expect(expectedResult).toContain(sanction2);
      });

      it('should accept null and undefined values', () => {
        const sanction: ISanction = sampleWithRequiredData;
        expectedResult = service.addSanctionToCollectionIfMissing([], null, sanction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sanction);
      });

      it('should return initial array if no Sanction is added', () => {
        const sanctionCollection: ISanction[] = [sampleWithRequiredData];
        expectedResult = service.addSanctionToCollectionIfMissing(sanctionCollection, undefined, null);
        expect(expectedResult).toEqual(sanctionCollection);
      });
    });

    describe('compareSanction', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSanction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSanction(entity1, entity2);
        const compareResult2 = service.compareSanction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSanction(entity1, entity2);
        const compareResult2 = service.compareSanction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSanction(entity1, entity2);
        const compareResult2 = service.compareSanction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
