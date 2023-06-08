import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaiementAdhesion } from '../paiement-adhesion.model';
import { PaiementAdhesionService } from '../service/paiement-adhesion.service';

@Injectable({ providedIn: 'root' })
export class PaiementAdhesionRoutingResolveService implements Resolve<IPaiementAdhesion | null> {
  constructor(protected service: PaiementAdhesionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaiementAdhesion | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paiementAdhesion: HttpResponse<IPaiementAdhesion>) => {
          if (paiementAdhesion.body) {
            return of(paiementAdhesion.body);
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
