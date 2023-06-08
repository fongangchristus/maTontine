import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaiementTontine } from '../paiement-tontine.model';
import { PaiementTontineService } from '../service/paiement-tontine.service';

@Injectable({ providedIn: 'root' })
export class PaiementTontineRoutingResolveService implements Resolve<IPaiementTontine | null> {
  constructor(protected service: PaiementTontineService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaiementTontine | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paiementTontine: HttpResponse<IPaiementTontine>) => {
          if (paiementTontine.body) {
            return of(paiementTontine.body);
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
