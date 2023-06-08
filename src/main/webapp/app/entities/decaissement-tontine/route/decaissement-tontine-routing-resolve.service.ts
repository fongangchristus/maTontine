import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDecaissementTontine } from '../decaissement-tontine.model';
import { DecaissementTontineService } from '../service/decaissement-tontine.service';

@Injectable({ providedIn: 'root' })
export class DecaissementTontineRoutingResolveService implements Resolve<IDecaissementTontine | null> {
  constructor(protected service: DecaissementTontineService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDecaissementTontine | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((decaissementTontine: HttpResponse<IDecaissementTontine>) => {
          if (decaissementTontine.body) {
            return of(decaissementTontine.body);
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
