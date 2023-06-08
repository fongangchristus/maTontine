import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPaiementAdhesion } from '../paiement-adhesion.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../paiement-adhesion.test-samples';

import { PaiementAdhesionService } from './paiement-adhesion.service';

const requireRestSample: IPaiementAdhesion = {
  ...sampleWithRequiredData,
};

describe('PaiementAdhesion Service', () => {
  let service: PaiementAdhesionService;
  let httpMock: HttpTestingController;
  let expectedResult: IPaiementAdhesion | IPaiementAdhesion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PaiementAdhesionService);
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

    it('should create a PaiementAdhesion', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const paiementAdhesion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(paiementAdhesion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaiementAdhesion', () => {
      const paiementAdhesion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(paiementAdhesion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaiementAdhesion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaiementAdhesion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PaiementAdhesion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPaiementAdhesionToCollectionIfMissing', () => {
      it('should add a PaiementAdhesion to an empty array', () => {
        const paiementAdhesion: IPaiementAdhesion = sampleWithRequiredData;
        expectedResult = service.addPaiementAdhesionToCollectionIfMissing([], paiementAdhesion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paiementAdhesion);
      });

      it('should not add a PaiementAdhesion to an array that contains it', () => {
        const paiementAdhesion: IPaiementAdhesion = sampleWithRequiredData;
        const paiementAdhesionCollection: IPaiementAdhesion[] = [
          {
            ...paiementAdhesion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPaiementAdhesionToCollectionIfMissing(paiementAdhesionCollection, paiementAdhesion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaiementAdhesion to an array that doesn't contain it", () => {
        const paiementAdhesion: IPaiementAdhesion = sampleWithRequiredData;
        const paiementAdhesionCollection: IPaiementAdhesion[] = [sampleWithPartialData];
        expectedResult = service.addPaiementAdhesionToCollectionIfMissing(paiementAdhesionCollection, paiementAdhesion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paiementAdhesion);
      });

      it('should add only unique PaiementAdhesion to an array', () => {
        const paiementAdhesionArray: IPaiementAdhesion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const paiementAdhesionCollection: IPaiementAdhesion[] = [sampleWithRequiredData];
        expectedResult = service.addPaiementAdhesionToCollectionIfMissing(paiementAdhesionCollection, ...paiementAdhesionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paiementAdhesion: IPaiementAdhesion = sampleWithRequiredData;
        const paiementAdhesion2: IPaiementAdhesion = sampleWithPartialData;
        expectedResult = service.addPaiementAdhesionToCollectionIfMissing([], paiementAdhesion, paiementAdhesion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paiementAdhesion);
        expect(expectedResult).toContain(paiementAdhesion2);
      });

      it('should accept null and undefined values', () => {
        const paiementAdhesion: IPaiementAdhesion = sampleWithRequiredData;
        expectedResult = service.addPaiementAdhesionToCollectionIfMissing([], null, paiementAdhesion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paiementAdhesion);
      });

      it('should return initial array if no PaiementAdhesion is added', () => {
        const paiementAdhesionCollection: IPaiementAdhesion[] = [sampleWithRequiredData];
        expectedResult = service.addPaiementAdhesionToCollectionIfMissing(paiementAdhesionCollection, undefined, null);
        expect(expectedResult).toEqual(paiementAdhesionCollection);
      });
    });

    describe('comparePaiementAdhesion', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePaiementAdhesion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePaiementAdhesion(entity1, entity2);
        const compareResult2 = service.comparePaiementAdhesion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePaiementAdhesion(entity1, entity2);
        const compareResult2 = service.comparePaiementAdhesion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePaiementAdhesion(entity1, entity2);
        const compareResult2 = service.comparePaiementAdhesion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
