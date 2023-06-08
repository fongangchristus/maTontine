import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFonctionAdherent } from '../fonction-adherent.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../fonction-adherent.test-samples';

import { FonctionAdherentService, RestFonctionAdherent } from './fonction-adherent.service';

const requireRestSample: RestFonctionAdherent = {
  ...sampleWithRequiredData,
  datePriseFonction: sampleWithRequiredData.datePriseFonction?.format(DATE_FORMAT),
  dateFinFonction: sampleWithRequiredData.dateFinFonction?.format(DATE_FORMAT),
};

describe('FonctionAdherent Service', () => {
  let service: FonctionAdherentService;
  let httpMock: HttpTestingController;
  let expectedResult: IFonctionAdherent | IFonctionAdherent[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FonctionAdherentService);
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

    it('should create a FonctionAdherent', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const fonctionAdherent = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fonctionAdherent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FonctionAdherent', () => {
      const fonctionAdherent = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fonctionAdherent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FonctionAdherent', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FonctionAdherent', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FonctionAdherent', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFonctionAdherentToCollectionIfMissing', () => {
      it('should add a FonctionAdherent to an empty array', () => {
        const fonctionAdherent: IFonctionAdherent = sampleWithRequiredData;
        expectedResult = service.addFonctionAdherentToCollectionIfMissing([], fonctionAdherent);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fonctionAdherent);
      });

      it('should not add a FonctionAdherent to an array that contains it', () => {
        const fonctionAdherent: IFonctionAdherent = sampleWithRequiredData;
        const fonctionAdherentCollection: IFonctionAdherent[] = [
          {
            ...fonctionAdherent,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFonctionAdherentToCollectionIfMissing(fonctionAdherentCollection, fonctionAdherent);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FonctionAdherent to an array that doesn't contain it", () => {
        const fonctionAdherent: IFonctionAdherent = sampleWithRequiredData;
        const fonctionAdherentCollection: IFonctionAdherent[] = [sampleWithPartialData];
        expectedResult = service.addFonctionAdherentToCollectionIfMissing(fonctionAdherentCollection, fonctionAdherent);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fonctionAdherent);
      });

      it('should add only unique FonctionAdherent to an array', () => {
        const fonctionAdherentArray: IFonctionAdherent[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fonctionAdherentCollection: IFonctionAdherent[] = [sampleWithRequiredData];
        expectedResult = service.addFonctionAdherentToCollectionIfMissing(fonctionAdherentCollection, ...fonctionAdherentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fonctionAdherent: IFonctionAdherent = sampleWithRequiredData;
        const fonctionAdherent2: IFonctionAdherent = sampleWithPartialData;
        expectedResult = service.addFonctionAdherentToCollectionIfMissing([], fonctionAdherent, fonctionAdherent2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fonctionAdherent);
        expect(expectedResult).toContain(fonctionAdherent2);
      });

      it('should accept null and undefined values', () => {
        const fonctionAdherent: IFonctionAdherent = sampleWithRequiredData;
        expectedResult = service.addFonctionAdherentToCollectionIfMissing([], null, fonctionAdherent, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fonctionAdherent);
      });

      it('should return initial array if no FonctionAdherent is added', () => {
        const fonctionAdherentCollection: IFonctionAdherent[] = [sampleWithRequiredData];
        expectedResult = service.addFonctionAdherentToCollectionIfMissing(fonctionAdherentCollection, undefined, null);
        expect(expectedResult).toEqual(fonctionAdherentCollection);
      });
    });

    describe('compareFonctionAdherent', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFonctionAdherent(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFonctionAdherent(entity1, entity2);
        const compareResult2 = service.compareFonctionAdherent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFonctionAdherent(entity1, entity2);
        const compareResult2 = service.compareFonctionAdherent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFonctionAdherent(entity1, entity2);
        const compareResult2 = service.compareFonctionAdherent(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
