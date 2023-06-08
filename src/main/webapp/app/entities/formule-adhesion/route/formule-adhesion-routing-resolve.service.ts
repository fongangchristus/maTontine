import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormuleAdhesion } from '../formule-adhesion.model';
import { FormuleAdhesionService } from '../service/formule-adhesion.service';

@Injectable({ providedIn: 'root' })
export class FormuleAdhesionRoutingResolveService implements Resolve<IFormuleAdhesion | null> {
  constructor(protected service: FormuleAdhesionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFormuleAdhesion | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((formuleAdhesion: HttpResponse<IFormuleAdhesion>) => {
          if (formuleAdhesion.body) {
            return of(formuleAdhesion.body);
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
