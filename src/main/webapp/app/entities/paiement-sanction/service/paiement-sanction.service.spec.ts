import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPaiementSanction } from '../paiement-sanction.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../paiement-sanction.test-samples';

import { PaiementSanctionService } from './paiement-sanction.service';

const requireRestSample: IPaiementSanction = {
  ...sampleWithRequiredData,
};

describe('PaiementSanction Service', () => {
  let service: PaiementSanctionService;
  let httpMock: HttpTestingController;
  let expectedResult: IPaiementSanction | IPaiementSanction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PaiementSanctionService);
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

    it('should create a PaiementSanction', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const paiementSanction = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(paiementSanction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaiementSanction', () => {
      const paiementSanction = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(paiementSanction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaiementSanction', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaiementSanction', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PaiementSanction', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPaiementSanctionToCollectionIfMissing', () => {
      it('should add a PaiementSanction to an empty array', () => {
        const paiementSanction: IPaiementSanction = sampleWithRequiredData;
        expectedResult = service.addPaiementSanctionToCollectionIfMissing([], paiementSanction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paiementSanction);
      });

      it('should not add a PaiementSanction to an array that contains it', () => {
        const paiementSanction: IPaiementSanction = sampleWithRequiredData;
        const paiementSanctionCollection: IPaiementSanction[] = [
          {
            ...paiementSanction,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPaiementSanctionToCollectionIfMissing(paiementSanctionCollection, paiementSanction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaiementSanction to an array that doesn't contain it", () => {
        const paiementSanction: IPaiementSanction = sampleWithRequiredData;
        const paiementSanctionCollection: IPaiementSanction[] = [sampleWithPartialData];
        expectedResult = service.addPaiementSanctionToCollectionIfMissing(paiementSanctionCollection, paiementSanction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paiementSanction);
      });

      it('should add only unique PaiementSanction to an array', () => {
        const paiementSanctionArray: IPaiementSanction[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const paiementSanctionCollection: IPaiementSanction[] = [sampleWithRequiredData];
        expectedResult = service.addPaiementSanctionToCollectionIfMissing(paiementSanctionCollection, ...paiementSanctionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paiementSanction: IPaiementSanction = sampleWithRequiredData;
        const paiementSanction2: IPaiementSanction = sampleWithPartialData;
        expectedResult = service.addPaiementSanctionToCollectionIfMissing([], paiementSanction, paiementSanction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paiementSanction);
        expect(expectedResult).toContain(paiementSanction2);
      });

      it('should accept null and undefined values', () => {
        const paiementSanction: IPaiementSanction = sampleWithRequiredData;
        expectedResult = service.addPaiementSanctionToCollectionIfMissing([], null, paiementSanction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paiementSanction);
      });

      it('should return initial array if no PaiementSanction is added', () => {
        const paiementSanctionCollection: IPaiementSanction[] = [sampleWithRequiredData];
        expectedResult = service.addPaiementSanctionToCollectionIfMissing(paiementSanctionCollection, undefined, null);
        expect(expectedResult).toEqual(paiementSanctionCollection);
      });
    });

    describe('comparePaiementSanction', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePaiementSanction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePaiementSanction(entity1, entity2);
        const compareResult2 = service.comparePaiementSanction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePaiementSanction(entity1, entity2);
        const compareResult2 = service.comparePaiementSanction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePaiementSanction(entity1, entity2);
        const compareResult2 = service.comparePaiementSanction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
