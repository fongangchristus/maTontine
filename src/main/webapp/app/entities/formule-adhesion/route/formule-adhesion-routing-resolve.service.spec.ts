import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IFormuleAdhesion } from '../formule-adhesion.model';
import { FormuleAdhesionService } from '../service/formule-adhesion.service';

import { FormuleAdhesionRoutingResolveService } from './formule-adhesion-routing-resolve.service';

describe('FormuleAdhesion routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FormuleAdhesionRoutingResolveService;
  let service: FormuleAdhesionService;
  let resultFormuleAdhesion: IFormuleAdhesion | null | undefined;

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
    routingResolveService = TestBed.inject(FormuleAdhesionRoutingResolveService);
    service = TestBed.inject(FormuleAdhesionService);
    resultFormuleAdhesion = undefined;
  });

  describe('resolve', () => {
    it('should return IFormuleAdhesion returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFormuleAdhesion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFormuleAdhesion).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFormuleAdhesion = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFormuleAdhesion).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IFormuleAdhesion>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFormuleAdhesion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFormuleAdhesion).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
