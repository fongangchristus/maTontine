import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdhesion } from '../adhesion.model';
import { AdhesionService } from '../service/adhesion.service';

@Injectable({ providedIn: 'root' })
export class AdhesionRoutingResolveService implements Resolve<IAdhesion | null> {
  constructor(protected service: AdhesionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAdhesion | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((adhesion: HttpResponse<IAdhesion>) => {
          if (adhesion.body) {
            return of(adhesion.body);
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
