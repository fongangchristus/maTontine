import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITontine } from '../tontine.model';
import { TontineService } from '../service/tontine.service';

@Injectable({ providedIn: 'root' })
export class TontineRoutingResolveService implements Resolve<ITontine | null> {
  constructor(protected service: TontineService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITontine | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tontine: HttpResponse<ITontine>) => {
          if (tontine.body) {
            return of(tontine.body);
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
