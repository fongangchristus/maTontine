import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPot } from '../pot.model';
import { PotService } from '../service/pot.service';

@Injectable({ providedIn: 'root' })
export class PotRoutingResolveService implements Resolve<IPot | null> {
  constructor(protected service: PotService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPot | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pot: HttpResponse<IPot>) => {
          if (pot.body) {
            return of(pot.body);
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
