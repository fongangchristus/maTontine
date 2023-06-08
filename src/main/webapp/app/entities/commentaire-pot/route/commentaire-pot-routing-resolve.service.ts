import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommentairePot } from '../commentaire-pot.model';
import { CommentairePotService } from '../service/commentaire-pot.service';

@Injectable({ providedIn: 'root' })
export class CommentairePotRoutingResolveService implements Resolve<ICommentairePot | null> {
  constructor(protected service: CommentairePotService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommentairePot | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((commentairePot: HttpResponse<ICommentairePot>) => {
          if (commentairePot.body) {
            return of(commentairePot.body);
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
