import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICotisationTontine } from '../cotisation-tontine.model';
import { CotisationTontineService } from '../service/cotisation-tontine.service';

@Injectable({ providedIn: 'root' })
export class CotisationTontineRoutingResolveService implements Resolve<ICotisationTontine | null> {
  constructor(protected service: CotisationTontineService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICotisationTontine | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cotisationTontine: HttpResponse<ICotisationTontine>) => {
          if (cotisationTontine.body) {
            return of(cotisationTontine.body);
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
