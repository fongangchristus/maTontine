import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypePot } from '../type-pot.model';
import { TypePotService } from '../service/type-pot.service';

@Injectable({ providedIn: 'root' })
export class TypePotRoutingResolveService implements Resolve<ITypePot | null> {
  constructor(protected service: TypePotService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypePot | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((typePot: HttpResponse<ITypePot>) => {
          if (typePot.body) {
            return of(typePot.body);
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
