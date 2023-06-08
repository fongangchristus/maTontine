import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaiementBanque, NewPaiementBanque } from '../paiement-banque.model';

export type PartialUpdatePaiementBanque = Partial<IPaiementBanque> & Pick<IPaiementBanque, 'id'>;

export type EntityResponseType = HttpResponse<IPaiementBanque>;
export type EntityArrayResponseType = HttpResponse<IPaiementBanque[]>;

@Injectable({ providedIn: 'root' })
export class PaiementBanqueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/paiement-banques');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paiementBanque: NewPaiementBanque): Observable<EntityResponseType> {
    return this.http.post<IPaiementBanque>(this.resourceUrl, paiementBanque, { observe: 'response' });
  }

  update(paiementBanque: IPaiementBanque): Observable<EntityResponseType> {
    return this.http.put<IPaiementBanque>(`${this.resourceUrl}/${this.getPaiementBanqueIdentifier(paiementBanque)}`, paiementBanque, {
      observe: 'response',
    });
  }

  partialUpdate(paiementBanque: PartialUpdatePaiementBanque): Observable<EntityResponseType> {
    return this.http.patch<IPaiementBanque>(`${this.resourceUrl}/${this.getPaiementBanqueIdentifier(paiementBanque)}`, paiementBanque, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaiementBanque>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaiementBanque[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPaiementBanqueIdentifier(paiementBanque: Pick<IPaiementBanque, 'id'>): number {
    return paiementBanque.id;
  }

  comparePaiementBanque(o1: Pick<IPaiementBanque, 'id'> | null, o2: Pick<IPaiementBanque, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaiementBanqueIdentifier(o1) === this.getPaiementBanqueIdentifier(o2) : o1 === o2;
  }

  addPaiementBanqueToCollectionIfMissing<Type extends Pick<IPaiementBanque, 'id'>>(
    paiementBanqueCollection: Type[],
    ...paiementBanquesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paiementBanques: Type[] = paiementBanquesToCheck.filter(isPresent);
    if (paiementBanques.length > 0) {
      const paiementBanqueCollectionIdentifiers = paiementBanqueCollection.map(
        paiementBanqueItem => this.getPaiementBanqueIdentifier(paiementBanqueItem)!
      );
      const paiementBanquesToAdd = paiementBanques.filter(paiementBanqueItem => {
        const paiementBanqueIdentifier = this.getPaiementBanqueIdentifier(paiementBanqueItem);
        if (paiementBanqueCollectionIdentifiers.includes(paiementBanqueIdentifier)) {
          return false;
        }
        paiementBanqueCollectionIdentifiers.push(paiementBanqueIdentifier);
        return true;
      });
      return [...paiementBanquesToAdd, ...paiementBanqueCollection];
    }
    return paiementBanqueCollection;
  }
}
