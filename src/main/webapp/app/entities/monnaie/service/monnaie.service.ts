import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMonnaie, NewMonnaie } from '../monnaie.model';

export type PartialUpdateMonnaie = Partial<IMonnaie> & Pick<IMonnaie, 'id'>;

export type EntityResponseType = HttpResponse<IMonnaie>;
export type EntityArrayResponseType = HttpResponse<IMonnaie[]>;

@Injectable({ providedIn: 'root' })
export class MonnaieService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/monnaies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(monnaie: NewMonnaie): Observable<EntityResponseType> {
    return this.http.post<IMonnaie>(this.resourceUrl, monnaie, { observe: 'response' });
  }

  update(monnaie: IMonnaie): Observable<EntityResponseType> {
    return this.http.put<IMonnaie>(`${this.resourceUrl}/${this.getMonnaieIdentifier(monnaie)}`, monnaie, { observe: 'response' });
  }

  partialUpdate(monnaie: PartialUpdateMonnaie): Observable<EntityResponseType> {
    return this.http.patch<IMonnaie>(`${this.resourceUrl}/${this.getMonnaieIdentifier(monnaie)}`, monnaie, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMonnaie>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMonnaie[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMonnaieIdentifier(monnaie: Pick<IMonnaie, 'id'>): number {
    return monnaie.id;
  }

  compareMonnaie(o1: Pick<IMonnaie, 'id'> | null, o2: Pick<IMonnaie, 'id'> | null): boolean {
    return o1 && o2 ? this.getMonnaieIdentifier(o1) === this.getMonnaieIdentifier(o2) : o1 === o2;
  }

  addMonnaieToCollectionIfMissing<Type extends Pick<IMonnaie, 'id'>>(
    monnaieCollection: Type[],
    ...monnaiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const monnaies: Type[] = monnaiesToCheck.filter(isPresent);
    if (monnaies.length > 0) {
      const monnaieCollectionIdentifiers = monnaieCollection.map(monnaieItem => this.getMonnaieIdentifier(monnaieItem)!);
      const monnaiesToAdd = monnaies.filter(monnaieItem => {
        const monnaieIdentifier = this.getMonnaieIdentifier(monnaieItem);
        if (monnaieCollectionIdentifiers.includes(monnaieIdentifier)) {
          return false;
        }
        monnaieCollectionIdentifiers.push(monnaieIdentifier);
        return true;
      });
      return [...monnaiesToAdd, ...monnaieCollection];
    }
    return monnaieCollection;
  }
}
