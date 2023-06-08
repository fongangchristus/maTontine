import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICommentairePot } from '../commentaire-pot.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../commentaire-pot.test-samples';

import { CommentairePotService, RestCommentairePot } from './commentaire-pot.service';

const requireRestSample: RestCommentairePot = {
  ...sampleWithRequiredData,
  dateComentaire: sampleWithRequiredData.dateComentaire?.toJSON(),
};

describe('CommentairePot Service', () => {
  let service: CommentairePotService;
  let httpMock: HttpTestingController;
  let expectedResult: ICommentairePot | ICommentairePot[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CommentairePotService);
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

    it('should create a CommentairePot', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const commentairePot = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(commentairePot).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CommentairePot', () => {
      const commentairePot = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(commentairePot).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CommentairePot', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CommentairePot', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CommentairePot', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCommentairePotToCollectionIfMissing', () => {
      it('should add a CommentairePot to an empty array', () => {
        const commentairePot: ICommentairePot = sampleWithRequiredData;
        expectedResult = service.addCommentairePotToCollectionIfMissing([], commentairePot);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commentairePot);
      });

      it('should not add a CommentairePot to an array that contains it', () => {
        const commentairePot: ICommentairePot = sampleWithRequiredData;
        const commentairePotCollection: ICommentairePot[] = [
          {
            ...commentairePot,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCommentairePotToCollectionIfMissing(commentairePotCollection, commentairePot);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CommentairePot to an array that doesn't contain it", () => {
        const commentairePot: ICommentairePot = sampleWithRequiredData;
        const commentairePotCollection: ICommentairePot[] = [sampleWithPartialData];
        expectedResult = service.addCommentairePotToCollectionIfMissing(commentairePotCollection, commentairePot);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commentairePot);
      });

      it('should add only unique CommentairePot to an array', () => {
        const commentairePotArray: ICommentairePot[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const commentairePotCollection: ICommentairePot[] = [sampleWithRequiredData];
        expectedResult = service.addCommentairePotToCollectionIfMissing(commentairePotCollection, ...commentairePotArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const commentairePot: ICommentairePot = sampleWithRequiredData;
        const commentairePot2: ICommentairePot = sampleWithPartialData;
        expectedResult = service.addCommentairePotToCollectionIfMissing([], commentairePot, commentairePot2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commentairePot);
        expect(expectedResult).toContain(commentairePot2);
      });

      it('should accept null and undefined values', () => {
        const commentairePot: ICommentairePot = sampleWithRequiredData;
        expectedResult = service.addCommentairePotToCollectionIfMissing([], null, commentairePot, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commentairePot);
      });

      it('should return initial array if no CommentairePot is added', () => {
        const commentairePotCollection: ICommentairePot[] = [sampleWithRequiredData];
        expectedResult = service.addCommentairePotToCollectionIfMissing(commentairePotCollection, undefined, null);
        expect(expectedResult).toEqual(commentairePotCollection);
      });
    });

    describe('compareCommentairePot', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCommentairePot(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCommentairePot(entity1, entity2);
        const compareResult2 = service.compareCommentairePot(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCommentairePot(entity1, entity2);
        const compareResult2 = service.compareCommentairePot(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCommentairePot(entity1, entity2);
        const compareResult2 = service.compareCommentairePot(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
