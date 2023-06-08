import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IGestionnaireTontine } from '../gestionnaire-tontine.model';
import { GestionnaireTontineService } from '../service/gestionnaire-tontine.service';

import { GestionnaireTontineRoutingResolveService } from './gestionnaire-tontine-routing-resolve.service';

describe('GestionnaireTontine routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: GestionnaireTontineRoutingResolveService;
  let service: GestionnaireTontineService;
  let resultGestionnaireTontine: IGestionnaireTontine | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(GestionnaireTontineRoutingResolveService);
    service = TestBed.inject(GestionnaireTontineService);
    resultGestionnaireTontine = undefined;
  });

  describe('resolve', () => {
    it('should return IGestionnaireTontine returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGestionnaireTontine = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGestionnaireTontine).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGestionnaireTontine = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultGestionnaireTontine).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IGestionnaireTontine>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGestionnaireTontine = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGestionnaireTontine).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
