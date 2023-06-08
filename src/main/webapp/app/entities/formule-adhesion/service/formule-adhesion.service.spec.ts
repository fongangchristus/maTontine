import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFormuleAdhesion } from '../formule-adhesion.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../formule-adhesion.test-samples';

import { FormuleAdhesionService, RestFormuleAdhesion } from './formule-adhesion.service';

const requireRestSample: RestFormuleAdhesion = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.toJSON(),
};

describe('FormuleAdhesion Service', () => {
  let service: FormuleAdhesionService;
  let httpMock: HttpTestingController;
  let expectedResult: IFormuleAdhesion | IFormuleAdhesion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FormuleAdhesionService);
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

    it('should create a FormuleAdhesion', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const formuleAdhesion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(formuleAdhesion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FormuleAdhesion', () => {
      const formuleAdhesion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(formuleAdhesion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FormuleAdhesion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FormuleAdhesion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FormuleAdhesion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFormuleAdhesionToCollectionIfMissing', () => {
      it('should add a FormuleAdhesion to an empty array', () => {
        const formuleAdhesion: IFormuleAdhesion = sampleWithRequiredData;
        expectedResult = service.addFormuleAdhesionToCollectionIfMissing([], formuleAdhesion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formuleAdhesion);
      });

      it('should not add a FormuleAdhesion to an array that contains it', () => {
        const formuleAdhesion: IFormuleAdhesion = sampleWithRequiredData;
        const formuleAdhesionCollection: IFormuleAdhesion[] = [
          {
            ...formuleAdhesion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFormuleAdhesionToCollectionIfMissing(formuleAdhesionCollection, formuleAdhesion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FormuleAdhesion to an array that doesn't contain it", () => {
        const formuleAdhesion: IFormuleAdhesion = sampleWithRequiredData;
        const formuleAdhesionCollection: IFormuleAdhesion[] = [sampleWithPartialData];
        expectedResult = service.addFormuleAdhesionToCollectionIfMissing(formuleAdhesionCollection, formuleAdhesion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formuleAdhesion);
      });

      it('should add only unique FormuleAdhesion to an array', () => {
        const formuleAdhesionArray: IFormuleAdhesion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const formuleAdhesionCollection: IFormuleAdhesion[] = [sampleWithRequiredData];
        expectedResult = service.addFormuleAdhesionToCollectionIfMissing(formuleAdhesionCollection, ...formuleAdhesionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const formuleAdhesion: IFormuleAdhesion = sampleWithRequiredData;
        const formuleAdhesion2: IFormuleAdhesion = sampleWithPartialData;
        expectedResult = service.addFormuleAdhesionToCollectionIfMissing([], formuleAdhesion, formuleAdhesion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formuleAdhesion);
        expect(expectedResult).toContain(formuleAdhesion2);
      });

      it('should accept null and undefined values', () => {
        const formuleAdhesion: IFormuleAdhesion = sampleWithRequiredData;
        expectedResult = service.addFormuleAdhesionToCollectionIfMissing([], null, formuleAdhesion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formuleAdhesion);
      });

      it('should return initial array if no FormuleAdhesion is added', () => {
        const formuleAdhesionCollection: IFormuleAdhesion[] = [sampleWithRequiredData];
        expectedResult = service.addFormuleAdhesionToCollectionIfMissing(formuleAdhesionCollection, undefined, null);
        expect(expectedResult).toEqual(formuleAdhesionCollection);
      });
    });

    describe('compareFormuleAdhesion', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFormuleAdhesion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFormuleAdhesion(entity1, entity2);
        const compareResult2 = service.compareFormuleAdhesion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFormuleAdhesion(entity1, entity2);
        const compareResult2 = service.compareFormuleAdhesion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFormuleAdhesion(entity1, entity2);
        const compareResult2 = service.compareFormuleAdhesion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
