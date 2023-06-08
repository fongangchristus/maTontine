import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaiementSanction } from '../paiement-sanction.model';
import { PaiementSanctionService } from '../service/paiement-sanction.service';

@Injectable({ providedIn: 'root' })
export class PaiementSanctionRoutingResolveService implements Resolve<IPaiementSanction | null> {
  constructor(protected service: PaiementSanctionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaiementSanction | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paiementSanction: HttpResponse<IPaiementSanction>) => {
          if (paiementSanction.body) {
            return of(paiementSanction.body);
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
