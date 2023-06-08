import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaiementTontine, NewPaiementTontine } from '../paiement-tontine.model';

export type PartialUpdatePaiementTontine = Partial<IPaiementTontine> & Pick<IPaiementTontine, 'id'>;

export type EntityResponseType = HttpResponse<IPaiementTontine>;
export type EntityArrayResponseType = HttpResponse<IPaiementTontine[]>;

@Injectable({ providedIn: 'root' })
export class PaiementTontineService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/paiement-tontines');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paiementTontine: NewPaiementTontine): Observable<EntityResponseType> {
    return this.http.post<IPaiementTontine>(this.resourceUrl, paiementTontine, { observe: 'response' });
  }

  update(paiementTontine: IPaiementTontine): Observable<EntityResponseType> {
    return this.http.put<IPaiementTontine>(`${this.resourceUrl}/${this.getPaiementTontineIdentifier(paiementTontine)}`, paiementTontine, {
      observe: 'response',
    });
  }

  partialUpdate(paiementTontine: PartialUpdatePaiementTontine): Observable<EntityResponseType> {
    return this.http.patch<IPaiementTontine>(`${this.resourceUrl}/${this.getPaiementTontineIdentifier(paiementTontine)}`, paiementTontine, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaiementTontine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaiementTontine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPaiementTontineIdentifier(paiementTontine: Pick<IPaiementTontine, 'id'>): number {
    return paiementTontine.id;
  }

  comparePaiementTontine(o1: Pick<IPaiementTontine, 'id'> | null, o2: Pick<IPaiementTontine, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaiementTontineIdentifier(o1) === this.getPaiementTontineIdentifier(o2) : o1 === o2;
  }

  addPaiementTontineToCollectionIfMissing<Type extends Pick<IPaiementTontine, 'id'>>(
    paiementTontineCollection: Type[],
    ...paiementTontinesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paiementTontines: Type[] = paiementTontinesToCheck.filter(isPresent);
    if (paiementTontines.length > 0) {
      const paiementTontineCollectionIdentifiers = paiementTontineCollection.map(
        paiementTontineItem => this.getPaiementTontineIdentifier(paiementTontineItem)!
      );
      const paiementTontinesToAdd = paiementTontines.filter(paiementTontineItem => {
        const paiementTontineIdentifier = this.getPaiementTontineIdentifier(paiementTontineItem);
        if (paiementTontineCollectionIdentifiers.includes(paiementTontineIdentifier)) {
          return false;
        }
        paiementTontineCollectionIdentifiers.push(paiementTontineIdentifier);
        return true;
      });
      return [...paiementTontinesToAdd, ...paiementTontineCollection];
    }
    return paiementTontineCollection;
  }
}
