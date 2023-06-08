import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssemble } from '../assemble.model';
import { AssembleService } from '../service/assemble.service';

@Injectable({ providedIn: 'root' })
export class AssembleRoutingResolveService implements Resolve<IAssemble | null> {
  constructor(protected service: AssembleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssemble | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((assemble: HttpResponse<IAssemble>) => {
          if (assemble.body) {
            return of(assemble.body);
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
