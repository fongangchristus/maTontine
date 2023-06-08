import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFichePresence } from '../fiche-presence.model';
import { FichePresenceService } from '../service/fiche-presence.service';

@Injectable({ providedIn: 'root' })
export class FichePresenceRoutingResolveService implements Resolve<IFichePresence | null> {
  constructor(protected service: FichePresenceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFichePresence | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fichePresence: HttpResponse<IFichePresence>) => {
          if (fichePresence.body) {
            return of(fichePresence.body);
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
