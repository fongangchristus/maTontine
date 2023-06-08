import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentAssociation } from '../document-association.model';
import { DocumentAssociationService } from '../service/document-association.service';

@Injectable({ providedIn: 'root' })
export class DocumentAssociationRoutingResolveService implements Resolve<IDocumentAssociation | null> {
  constructor(protected service: DocumentAssociationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentAssociation | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentAssociation: HttpResponse<IDocumentAssociation>) => {
          if (documentAssociation.body) {
            return of(documentAssociation.body);
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
