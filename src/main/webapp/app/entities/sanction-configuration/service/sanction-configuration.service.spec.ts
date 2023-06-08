import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISanctionConfiguration } from '../sanction-configuration.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../sanction-configuration.test-samples';

import { SanctionConfigurationService } from './sanction-configuration.service';

const requireRestSample: ISanctionConfiguration = {
  ...sampleWithRequiredData,
};

describe('SanctionConfiguration Service', () => {
  let service: SanctionConfigurationService;
  let httpMock: HttpTestingController;
  let expectedResult: ISanctionConfiguration | ISanctionConfiguration[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SanctionConfigurationService);
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

    it('should create a SanctionConfiguration', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const sanctionConfiguration = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sanctionConfiguration).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SanctionConfiguration', () => {
      const sanctionConfiguration = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sanctionConfiguration).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SanctionConfiguration', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SanctionConfiguration', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SanctionConfiguration', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSanctionConfigurationToCollectionIfMissing', () => {
      it('should add a SanctionConfiguration to an empty array', () => {
        const sanctionConfiguration: ISanctionConfiguration = sampleWithRequiredData;
        expectedResult = service.addSanctionConfigurationToCollectionIfMissing([], sanctionConfiguration);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sanctionConfiguration);
      });

      it('should not add a SanctionConfiguration to an array that contains it', () => {
        const sanctionConfiguration: ISanctionConfiguration = sampleWithRequiredData;
        const sanctionConfigurationCollection: ISanctionConfiguration[] = [
          {
            ...sanctionConfiguration,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSanctionConfigurationToCollectionIfMissing(sanctionConfigurationCollection, sanctionConfiguration);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SanctionConfiguration to an array that doesn't contain it", () => {
        const sanctionConfiguration: ISanctionConfiguration = sampleWithRequiredData;
        const sanctionConfigurationCollection: ISanctionConfiguration[] = [sampleWithPartialData];
        expectedResult = service.addSanctionConfigurationToCollectionIfMissing(sanctionConfigurationCollection, sanctionConfiguration);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sanctionConfiguration);
      });

      it('should add only unique SanctionConfiguration to an array', () => {
        const sanctionConfigurationArray: ISanctionConfiguration[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sanctionConfigurationCollection: ISanctionConfiguration[] = [sampleWithRequiredData];
        expectedResult = service.addSanctionConfigurationToCollectionIfMissing(
          sanctionConfigurationCollection,
          ...sanctionConfigurationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sanctionConfiguration: ISanctionConfiguration = sampleWithRequiredData;
        const sanctionConfiguration2: ISanctionConfiguration = sampleWithPartialData;
        expectedResult = service.addSanctionConfigurationToCollectionIfMissing([], sanctionConfiguration, sanctionConfiguration2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sanctionConfiguration);
        expect(expectedResult).toContain(sanctionConfiguration2);
      });

      it('should accept null and undefined values', () => {
        const sanctionConfiguration: ISanctionConfiguration = sampleWithRequiredData;
        expectedResult = service.addSanctionConfigurationToCollectionIfMissing([], null, sanctionConfiguration, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sanctionConfiguration);
      });

      it('should return initial array if no SanctionConfiguration is added', () => {
        const sanctionConfigurationCollection: ISanctionConfiguration[] = [sampleWithRequiredData];
        expectedResult = service.addSanctionConfigurationToCollectionIfMissing(sanctionConfigurationCollection, undefined, null);
        expect(expectedResult).toEqual(sanctionConfigurationCollection);
      });
    });

    describe('compareSanctionConfiguration', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSanctionConfiguration(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSanctionConfiguration(entity1, entity2);
        const compareResult2 = service.compareSanctionConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSanctionConfiguration(entity1, entity2);
        const compareResult2 = service.compareSanctionConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSanctionConfiguration(entity1, entity2);
        const compareResult2 = service.compareSanctionConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
