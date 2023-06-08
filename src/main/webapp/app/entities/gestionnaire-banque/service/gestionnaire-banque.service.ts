import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGestionnaireBanque, NewGestionnaireBanque } from '../gestionnaire-banque.model';

export type PartialUpdateGestionnaireBanque = Partial<IGestionnaireBanque> & Pick<IGestionnaireBanque, 'id'>;

export type EntityResponseType = HttpResponse<IGestionnaireBanque>;
export type EntityArrayResponseType = HttpResponse<IGestionnaireBanque[]>;

@Injectable({ providedIn: 'root' })
export class GestionnaireBanqueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/gestionnaire-banques');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gestionnaireBanque: NewGestionnaireBanque): Observable<EntityResponseType> {
    return this.http.post<IGestionnaireBanque>(this.resourceUrl, gestionnaireBanque, { observe: 'response' });
  }

  update(gestionnaireBanque: IGestionnaireBanque): Observable<EntityResponseType> {
    return this.http.put<IGestionnaireBanque>(
      `${this.resourceUrl}/${this.getGestionnaireBanqueIdentifier(gestionnaireBanque)}`,
      gestionnaireBanque,
      { observe: 'response' }
    );
  }

  partialUpdate(gestionnaireBanque: PartialUpdateGestionnaireBanque): Observable<EntityResponseType> {
    return this.http.patch<IGestionnaireBanque>(
      `${this.resourceUrl}/${this.getGestionnaireBanqueIdentifier(gestionnaireBanque)}`,
      gestionnaireBanque,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGestionnaireBanque>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGestionnaireBanque[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGestionnaireBanqueIdentifier(gestionnaireBanque: Pick<IGestionnaireBanque, 'id'>): number {
    return gestionnaireBanque.id;
  }

  compareGestionnaireBanque(o1: Pick<IGestionnaireBanque, 'id'> | null, o2: Pick<IGestionnaireBanque, 'id'> | null): boolean {
    return o1 && o2 ? this.getGestionnaireBanqueIdentifier(o1) === this.getGestionnaireBanqueIdentifier(o2) : o1 === o2;
  }

  addGestionnaireBanqueToCollectionIfMissing<Type extends Pick<IGestionnaireBanque, 'id'>>(
    gestionnaireBanqueCollection: Type[],
    ...gestionnaireBanquesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const gestionnaireBanques: Type[] = gestionnaireBanquesToCheck.filter(isPresent);
    if (gestionnaireBanques.length > 0) {
      const gestionnaireBanqueCollectionIdentifiers = gestionnaireBanqueCollection.map(
        gestionnaireBanqueItem => this.getGestionnaireBanqueIdentifier(gestionnaireBanqueItem)!
      );
      const gestionnaireBanquesToAdd = gestionnaireBanques.filter(gestionnaireBanqueItem => {
        const gestionnaireBanqueIdentifier = this.getGestionnaireBanqueIdentifier(gestionnaireBanqueItem);
        if (gestionnaireBanqueCollectionIdentifiers.includes(gestionnaireBanqueIdentifier)) {
          return false;
        }
        gestionnaireBanqueCollectionIdentifiers.push(gestionnaireBanqueIdentifier);
        return true;
      });
      return [...gestionnaireBanquesToAdd, ...gestionnaireBanqueCollection];
    }
    return gestionnaireBanqueCollection;
  }
}
