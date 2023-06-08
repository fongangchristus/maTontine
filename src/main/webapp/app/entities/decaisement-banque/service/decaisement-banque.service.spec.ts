import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDecaisementBanque } from '../decaisement-banque.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../decaisement-banque.test-samples';

import { DecaisementBanqueService, RestDecaisementBanque } from './decaisement-banque.service';

const requireRestSample: RestDecaisementBanque = {
  ...sampleWithRequiredData,
  dateDecaissement: sampleWithRequiredData.dateDecaissement?.toJSON(),
};

describe('DecaisementBanque Service', () => {
  let service: DecaisementBanqueService;
  let httpMock: HttpTestingController;
  let expectedResult: IDecaisementBanque | IDecaisementBanque[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DecaisementBanqueService);
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

    it('should create a DecaisementBanque', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const decaisementBanque = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(decaisementBanque).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DecaisementBanque', () => {
      const decaisementBanque = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(decaisementBanque).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DecaisementBanque', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DecaisementBanque', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DecaisementBanque', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDecaisementBanqueToCollectionIfMissing', () => {
      it('should add a DecaisementBanque to an empty array', () => {
        const decaisementBanque: IDecaisementBanque = sampleWithRequiredData;
        expectedResult = service.addDecaisementBanqueToCollectionIfMissing([], decaisementBanque);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(decaisementBanque);
      });

      it('should not add a DecaisementBanque to an array that contains it', () => {
        const decaisementBanque: IDecaisementBanque = sampleWithRequiredData;
        const decaisementBanqueCollection: IDecaisementBanque[] = [
          {
            ...decaisementBanque,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDecaisementBanqueToCollectionIfMissing(decaisementBanqueCollection, decaisementBanque);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DecaisementBanque to an array that doesn't contain it", () => {
        const decaisementBanque: IDecaisementBanque = sampleWithRequiredData;
        const decaisementBanqueCollection: IDecaisementBanque[] = [sampleWithPartialData];
        expectedResult = service.addDecaisementBanqueToCollectionIfMissing(decaisementBanqueCollection, decaisementBanque);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(decaisementBanque);
      });

      it('should add only unique DecaisementBanque to an array', () => {
        const decaisementBanqueArray: IDecaisementBanque[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const decaisementBanqueCollection: IDecaisementBanque[] = [sampleWithRequiredData];
        expectedResult = service.addDecaisementBanqueToCollectionIfMissing(decaisementBanqueCollection, ...decaisementBanqueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const decaisementBanque: IDecaisementBanque = sampleWithRequiredData;
        const decaisementBanque2: IDecaisementBanque = sampleWithPartialData;
        expectedResult = service.addDecaisementBanqueToCollectionIfMissing([], decaisementBanque, decaisementBanque2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(decaisementBanque);
        expect(expectedResult).toContain(decaisementBanque2);
      });

      it('should accept null and undefined values', () => {
        const decaisementBanque: IDecaisementBanque = sampleWithRequiredData;
        expectedResult = service.addDecaisementBanqueToCollectionIfMissing([], null, decaisementBanque, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(decaisementBanque);
      });

      it('should return initial array if no DecaisementBanque is added', () => {
        const decaisementBanqueCollection: IDecaisementBanque[] = [sampleWithRequiredData];
        expectedResult = service.addDecaisementBanqueToCollectionIfMissing(decaisementBanqueCollection, undefined, null);
        expect(expectedResult).toEqual(decaisementBanqueCollection);
      });
    });

    describe('compareDecaisementBanque', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDecaisementBanque(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDecaisementBanque(entity1, entity2);
        const compareResult2 = service.compareDecaisementBanque(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDecaisementBanque(entity1, entity2);
        const compareResult2 = service.compareDecaisementBanque(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDecaisementBanque(entity1, entity2);
        const compareResult2 = service.compareDecaisementBanque(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
