import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEvenement } from '../evenement.model';
import { EvenementService } from '../service/evenement.service';

@Injectable({ providedIn: 'root' })
export class EvenementRoutingResolveService implements Resolve<IEvenement | null> {
  constructor(protected service: EvenementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEvenement | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((evenement: HttpResponse<IEvenement>) => {
          if (evenement.body) {
            return of(evenement.body);
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
