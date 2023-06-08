import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssociation } from '../association.model';
import { AssociationService } from '../service/association.service';

@Injectable({ providedIn: 'root' })
export class AssociationRoutingResolveService implements Resolve<IAssociation | null> {
  constructor(protected service: AssociationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssociation | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((association: HttpResponse<IAssociation>) => {
          if (association.body) {
            return of(association.body);
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
