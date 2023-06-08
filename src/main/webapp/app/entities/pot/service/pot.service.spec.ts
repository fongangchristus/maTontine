import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPot } from '../pot.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pot.test-samples';

import { PotService, RestPot } from './pot.service';

const requireRestSample: RestPot = {
  ...sampleWithRequiredData,
  dateDebutCollecte: sampleWithRequiredData.dateDebutCollecte?.format(DATE_FORMAT),
  dateFinCollecte: sampleWithRequiredData.dateFinCollecte?.format(DATE_FORMAT),
};

describe('Pot Service', () => {
  let service: PotService;
  let httpMock: HttpTestingController;
  let expectedResult: IPot | IPot[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PotService);
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

    it('should create a Pot', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pot = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pot).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pot', () => {
      const pot = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pot).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pot', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pot', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Pot', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPotToCollectionIfMissing', () => {
      it('should add a Pot to an empty array', () => {
        const pot: IPot = sampleWithRequiredData;
        expectedResult = service.addPotToCollectionIfMissing([], pot);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pot);
      });

      it('should not add a Pot to an array that contains it', () => {
        const pot: IPot = sampleWithRequiredData;
        const potCollection: IPot[] = [
          {
            ...pot,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPotToCollectionIfMissing(potCollection, pot);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pot to an array that doesn't contain it", () => {
        const pot: IPot = sampleWithRequiredData;
        const potCollection: IPot[] = [sampleWithPartialData];
        expectedResult = service.addPotToCollectionIfMissing(potCollection, pot);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pot);
      });

      it('should add only unique Pot to an array', () => {
        const potArray: IPot[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const potCollection: IPot[] = [sampleWithRequiredData];
        expectedResult = service.addPotToCollectionIfMissing(potCollection, ...potArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pot: IPot = sampleWithRequiredData;
        const pot2: IPot = sampleWithPartialData;
        expectedResult = service.addPotToCollectionIfMissing([], pot, pot2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pot);
        expect(expectedResult).toContain(pot2);
      });

      it('should accept null and undefined values', () => {
        const pot: IPot = sampleWithRequiredData;
        expectedResult = service.addPotToCollectionIfMissing([], null, pot, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pot);
      });

      it('should return initial array if no Pot is added', () => {
        const potCollection: IPot[] = [sampleWithRequiredData];
        expectedResult = service.addPotToCollectionIfMissing(potCollection, undefined, null);
        expect(expectedResult).toEqual(potCollection);
      });
    });

    describe('comparePot', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePot(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePot(entity1, entity2);
        const compareResult2 = service.comparePot(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePot(entity1, entity2);
        const compareResult2 = service.comparePot(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePot(entity1, entity2);
        const compareResult2 = service.comparePot(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
