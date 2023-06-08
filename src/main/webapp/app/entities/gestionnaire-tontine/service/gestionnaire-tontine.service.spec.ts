import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IGestionnaireTontine } from '../gestionnaire-tontine.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../gestionnaire-tontine.test-samples';

import { GestionnaireTontineService, RestGestionnaireTontine } from './gestionnaire-tontine.service';

const requireRestSample: RestGestionnaireTontine = {
  ...sampleWithRequiredData,
  datePriseFonction: sampleWithRequiredData.datePriseFonction?.format(DATE_FORMAT),
  dateFinFonction: sampleWithRequiredData.dateFinFonction?.format(DATE_FORMAT),
};

describe('GestionnaireTontine Service', () => {
  let service: GestionnaireTontineService;
  let httpMock: HttpTestingController;
  let expectedResult: IGestionnaireTontine | IGestionnaireTontine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GestionnaireTontineService);
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

    it('should create a GestionnaireTontine', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const gestionnaireTontine = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(gestionnaireTontine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GestionnaireTontine', () => {
      const gestionnaireTontine = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(gestionnaireTontine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GestionnaireTontine', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GestionnaireTontine', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GestionnaireTontine', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGestionnaireTontineToCollectionIfMissing', () => {
      it('should add a GestionnaireTontine to an empty array', () => {
        const gestionnaireTontine: IGestionnaireTontine = sampleWithRequiredData;
        expectedResult = service.addGestionnaireTontineToCollectionIfMissing([], gestionnaireTontine);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gestionnaireTontine);
      });

      it('should not add a GestionnaireTontine to an array that contains it', () => {
        const gestionnaireTontine: IGestionnaireTontine = sampleWithRequiredData;
        const gestionnaireTontineCollection: IGestionnaireTontine[] = [
          {
            ...gestionnaireTontine,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGestionnaireTontineToCollectionIfMissing(gestionnaireTontineCollection, gestionnaireTontine);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GestionnaireTontine to an array that doesn't contain it", () => {
        const gestionnaireTontine: IGestionnaireTontine = sampleWithRequiredData;
        const gestionnaireTontineCollection: IGestionnaireTontine[] = [sampleWithPartialData];
        expectedResult = service.addGestionnaireTontineToCollectionIfMissing(gestionnaireTontineCollection, gestionnaireTontine);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gestionnaireTontine);
      });

      it('should add only unique GestionnaireTontine to an array', () => {
        const gestionnaireTontineArray: IGestionnaireTontine[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const gestionnaireTontineCollection: IGestionnaireTontine[] = [sampleWithRequiredData];
        expectedResult = service.addGestionnaireTontineToCollectionIfMissing(gestionnaireTontineCollection, ...gestionnaireTontineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gestionnaireTontine: IGestionnaireTontine = sampleWithRequiredData;
        const gestionnaireTontine2: IGestionnaireTontine = sampleWithPartialData;
        expectedResult = service.addGestionnaireTontineToCollectionIfMissing([], gestionnaireTontine, gestionnaireTontine2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gestionnaireTontine);
        expect(expectedResult).toContain(gestionnaireTontine2);
      });

      it('should accept null and undefined values', () => {
        const gestionnaireTontine: IGestionnaireTontine = sampleWithRequiredData;
        expectedResult = service.addGestionnaireTontineToCollectionIfMissing([], null, gestionnaireTontine, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gestionnaireTontine);
      });

      it('should return initial array if no GestionnaireTontine is added', () => {
        const gestionnaireTontineCollection: IGestionnaireTontine[] = [sampleWithRequiredData];
        expectedResult = service.addGestionnaireTontineToCollectionIfMissing(gestionnaireTontineCollection, undefined, null);
        expect(expectedResult).toEqual(gestionnaireTontineCollection);
      });
    });

    describe('compareGestionnaireTontine', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGestionnaireTontine(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGestionnaireTontine(entity1, entity2);
        const compareResult2 = service.compareGestionnaireTontine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGestionnaireTontine(entity1, entity2);
        const compareResult2 = service.compareGestionnaireTontine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGestionnaireTontine(entity1, entity2);
        const compareResult2 = service.compareGestionnaireTontine(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
