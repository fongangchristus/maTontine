import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAdhesion } from '../adhesion.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../adhesion.test-samples';

import { AdhesionService, RestAdhesion } from './adhesion.service';

const requireRestSample: RestAdhesion = {
  ...sampleWithRequiredData,
  dateDebutAdhesion: sampleWithRequiredData.dateDebutAdhesion?.toJSON(),
  dateFinAdhesion: sampleWithRequiredData.dateFinAdhesion?.toJSON(),
};

describe('Adhesion Service', () => {
  let service: AdhesionService;
  let httpMock: HttpTestingController;
  let expectedResult: IAdhesion | IAdhesion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AdhesionService);
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

    it('should create a Adhesion', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const adhesion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(adhesion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Adhesion', () => {
      const adhesion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(adhesion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Adhesion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Adhesion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Adhesion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAdhesionToCollectionIfMissing', () => {
      it('should add a Adhesion to an empty array', () => {
        const adhesion: IAdhesion = sampleWithRequiredData;
        expectedResult = service.addAdhesionToCollectionIfMissing([], adhesion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(adhesion);
      });

      it('should not add a Adhesion to an array that contains it', () => {
        const adhesion: IAdhesion = sampleWithRequiredData;
        const adhesionCollection: IAdhesion[] = [
          {
            ...adhesion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAdhesionToCollectionIfMissing(adhesionCollection, adhesion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Adhesion to an array that doesn't contain it", () => {
        const adhesion: IAdhesion = sampleWithRequiredData;
        const adhesionCollection: IAdhesion[] = [sampleWithPartialData];
        expectedResult = service.addAdhesionToCollectionIfMissing(adhesionCollection, adhesion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(adhesion);
      });

      it('should add only unique Adhesion to an array', () => {
        const adhesionArray: IAdhesion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const adhesionCollection: IAdhesion[] = [sampleWithRequiredData];
        expectedResult = service.addAdhesionToCollectionIfMissing(adhesionCollection, ...adhesionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const adhesion: IAdhesion = sampleWithRequiredData;
        const adhesion2: IAdhesion = sampleWithPartialData;
        expectedResult = service.addAdhesionToCollectionIfMissing([], adhesion, adhesion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(adhesion);
        expect(expectedResult).toContain(adhesion2);
      });

      it('should accept null and undefined values', () => {
        const adhesion: IAdhesion = sampleWithRequiredData;
        expectedResult = service.addAdhesionToCollectionIfMissing([], null, adhesion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(adhesion);
      });

      it('should return initial array if no Adhesion is added', () => {
        const adhesionCollection: IAdhesion[] = [sampleWithRequiredData];
        expectedResult = service.addAdhesionToCollectionIfMissing(adhesionCollection, undefined, null);
        expect(expectedResult).toEqual(adhesionCollection);
      });
    });

    describe('compareAdhesion', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAdhesion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAdhesion(entity1, entity2);
        const compareResult2 = service.compareAdhesion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAdhesion(entity1, entity2);
        const compareResult2 = service.compareAdhesion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAdhesion(entity1, entity2);
        const compareResult2 = service.compareAdhesion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
