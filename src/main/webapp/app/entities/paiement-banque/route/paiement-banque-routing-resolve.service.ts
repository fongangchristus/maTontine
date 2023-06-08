import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaiementBanque } from '../paiement-banque.model';
import { PaiementBanqueService } from '../service/paiement-banque.service';

@Injectable({ providedIn: 'root' })
export class PaiementBanqueRoutingResolveService implements Resolve<IPaiementBanque | null> {
  constructor(protected service: PaiementBanqueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaiementBanque | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paiementBanque: HttpResponse<IPaiementBanque>) => {
          if (paiementBanque.body) {
            return of(paiementBanque.body);
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
