import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGestionnaireBanque } from '../gestionnaire-banque.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../gestionnaire-banque.test-samples';

import { GestionnaireBanqueService } from './gestionnaire-banque.service';

const requireRestSample: IGestionnaireBanque = {
  ...sampleWithRequiredData,
};

describe('GestionnaireBanque Service', () => {
  let service: GestionnaireBanqueService;
  let httpMock: HttpTestingController;
  let expectedResult: IGestionnaireBanque | IGestionnaireBanque[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GestionnaireBanqueService);
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

    it('should create a GestionnaireBanque', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const gestionnaireBanque = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(gestionnaireBanque).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GestionnaireBanque', () => {
      const gestionnaireBanque = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(gestionnaireBanque).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GestionnaireBanque', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GestionnaireBanque', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GestionnaireBanque', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGestionnaireBanqueToCollectionIfMissing', () => {
      it('should add a GestionnaireBanque to an empty array', () => {
        const gestionnaireBanque: IGestionnaireBanque = sampleWithRequiredData;
        expectedResult = service.addGestionnaireBanqueToCollectionIfMissing([], gestionnaireBanque);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gestionnaireBanque);
      });

      it('should not add a GestionnaireBanque to an array that contains it', () => {
        const gestionnaireBanque: IGestionnaireBanque = sampleWithRequiredData;
        const gestionnaireBanqueCollection: IGestionnaireBanque[] = [
          {
            ...gestionnaireBanque,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGestionnaireBanqueToCollectionIfMissing(gestionnaireBanqueCollection, gestionnaireBanque);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GestionnaireBanque to an array that doesn't contain it", () => {
        const gestionnaireBanque: IGestionnaireBanque = sampleWithRequiredData;
        const gestionnaireBanqueCollection: IGestionnaireBanque[] = [sampleWithPartialData];
        expectedResult = service.addGestionnaireBanqueToCollectionIfMissing(gestionnaireBanqueCollection, gestionnaireBanque);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gestionnaireBanque);
      });

      it('should add only unique GestionnaireBanque to an array', () => {
        const gestionnaireBanqueArray: IGestionnaireBanque[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const gestionnaireBanqueCollection: IGestionnaireBanque[] = [sampleWithRequiredData];
        expectedResult = service.addGestionnaireBanqueToCollectionIfMissing(gestionnaireBanqueCollection, ...gestionnaireBanqueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gestionnaireBanque: IGestionnaireBanque = sampleWithRequiredData;
        const gestionnaireBanque2: IGestionnaireBanque = sampleWithPartialData;
        expectedResult = service.addGestionnaireBanqueToCollectionIfMissing([], gestionnaireBanque, gestionnaireBanque2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gestionnaireBanque);
        expect(expectedResult).toContain(gestionnaireBanque2);
      });

      it('should accept null and undefined values', () => {
        const gestionnaireBanque: IGestionnaireBanque = sampleWithRequiredData;
        expectedResult = service.addGestionnaireBanqueToCollectionIfMissing([], null, gestionnaireBanque, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gestionnaireBanque);
      });

      it('should return initial array if no GestionnaireBanque is added', () => {
        const gestionnaireBanqueCollection: IGestionnaireBanque[] = [sampleWithRequiredData];
        expectedResult = service.addGestionnaireBanqueToCollectionIfMissing(gestionnaireBanqueCollection, undefined, null);
        expect(expectedResult).toEqual(gestionnaireBanqueCollection);
      });
    });

    describe('compareGestionnaireBanque', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGestionnaireBanque(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGestionnaireBanque(entity1, entity2);
        const compareResult2 = service.compareGestionnaireBanque(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGestionnaireBanque(entity1, entity2);
        const compareResult2 = service.compareGestionnaireBanque(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGestionnaireBanque(entity1, entity2);
        const compareResult2 = service.compareGestionnaireBanque(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
