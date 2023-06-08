import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypePot, NewTypePot } from '../type-pot.model';

export type PartialUpdateTypePot = Partial<ITypePot> & Pick<ITypePot, 'id'>;

export type EntityResponseType = HttpResponse<ITypePot>;
export type EntityArrayResponseType = HttpResponse<ITypePot[]>;

@Injectable({ providedIn: 'root' })
export class TypePotService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-pots');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(typePot: NewTypePot): Observable<EntityResponseType> {
    return this.http.post<ITypePot>(this.resourceUrl, typePot, { observe: 'response' });
  }

  update(typePot: ITypePot): Observable<EntityResponseType> {
    return this.http.put<ITypePot>(`${this.resourceUrl}/${this.getTypePotIdentifier(typePot)}`, typePot, { observe: 'response' });
  }

  partialUpdate(typePot: PartialUpdateTypePot): Observable<EntityResponseType> {
    return this.http.patch<ITypePot>(`${this.resourceUrl}/${this.getTypePotIdentifier(typePot)}`, typePot, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypePot>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypePot[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypePotIdentifier(typePot: Pick<ITypePot, 'id'>): number {
    return typePot.id;
  }

  compareTypePot(o1: Pick<ITypePot, 'id'> | null, o2: Pick<ITypePot, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypePotIdentifier(o1) === this.getTypePotIdentifier(o2) : o1 === o2;
  }

  addTypePotToCollectionIfMissing<Type extends Pick<ITypePot, 'id'>>(
    typePotCollection: Type[],
    ...typePotsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typePots: Type[] = typePotsToCheck.filter(isPresent);
    if (typePots.length > 0) {
      const typePotCollectionIdentifiers = typePotCollection.map(typePotItem => this.getTypePotIdentifier(typePotItem)!);
      const typePotsToAdd = typePots.filter(typePotItem => {
        const typePotIdentifier = this.getTypePotIdentifier(typePotItem);
        if (typePotCollectionIdentifiers.includes(typePotIdentifier)) {
          return false;
        }
        typePotCollectionIdentifiers.push(typePotIdentifier);
        return true;
      });
      return [...typePotsToAdd, ...typePotCollection];
    }
    return typePotCollection;
  }
}
