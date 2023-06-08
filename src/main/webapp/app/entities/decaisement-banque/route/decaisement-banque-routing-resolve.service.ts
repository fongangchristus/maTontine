import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDecaisementBanque } from '../decaisement-banque.model';
import { DecaisementBanqueService } from '../service/decaisement-banque.service';

@Injectable({ providedIn: 'root' })
export class DecaisementBanqueRoutingResolveService implements Resolve<IDecaisementBanque | null> {
  constructor(protected service: DecaisementBanqueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDecaisementBanque | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((decaisementBanque: HttpResponse<IDecaisementBanque>) => {
          if (decaisementBanque.body) {
            return of(decaisementBanque.body);
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
