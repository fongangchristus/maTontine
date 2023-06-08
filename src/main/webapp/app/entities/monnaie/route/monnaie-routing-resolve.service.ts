import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMonnaie } from '../monnaie.model';
import { MonnaieService } from '../service/monnaie.service';

@Injectable({ providedIn: 'root' })
export class MonnaieRoutingResolveService implements Resolve<IMonnaie | null> {
  constructor(protected service: MonnaieService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMonnaie | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((monnaie: HttpResponse<IMonnaie>) => {
          if (monnaie.body) {
            return of(monnaie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
