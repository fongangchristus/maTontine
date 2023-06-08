import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICotisationBanque } from '../cotisation-banque.model';
import { CotisationBanqueService } from '../service/cotisation-banque.service';

@Injectable({ providedIn: 'root' })
export class CotisationBanqueRoutingResolveService implements Resolve<ICotisationBanque | null> {
  constructor(protected service: CotisationBanqueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICotisationBanque | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cotisationBanque: HttpResponse<ICotisationBanque>) => {
          if (cotisationBanque.body) {
            return of(cotisationBanque.body);
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
