import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGestionnaireBanque } from '../gestionnaire-banque.model';
import { GestionnaireBanqueService } from '../service/gestionnaire-banque.service';

@Injectable({ providedIn: 'root' })
export class GestionnaireBanqueRoutingResolveService implements Resolve<IGestionnaireBanque | null> {
  constructor(protected service: GestionnaireBanqueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGestionnaireBanque | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gestionnaireBanque: HttpResponse<IGestionnaireBanque>) => {
          if (gestionnaireBanque.body) {
            return of(gestionnaireBanque.body);
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
