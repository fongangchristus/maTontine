import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompteRIB, NewCompteRIB } from '../compte-rib.model';

export type PartialUpdateCompteRIB = Partial<ICompteRIB> & Pick<ICompteRIB, 'id'>;

export type EntityResponseType = HttpResponse<ICompteRIB>;
export type EntityArrayResponseType = HttpResponse<ICompteRIB[]>;

@Injectable({ providedIn: 'root' })
export class CompteRIBService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/compte-ribs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(compteRIB: NewCompteRIB): Observable<EntityResponseType> {
    return this.http.post<ICompteRIB>(this.resourceUrl, compteRIB, { observe: 'response' });
  }

  update(compteRIB: ICompteRIB): Observable<EntityResponseType> {
    return this.http.put<ICompteRIB>(`${this.resourceUrl}/${this.getCompteRIBIdentifier(compteRIB)}`, compteRIB, { observe: 'response' });
  }

  partialUpdate(compteRIB: PartialUpdateCompteRIB): Observable<EntityResponseType> {
    return this.http.patch<ICompteRIB>(`${this.resourceUrl}/${this.getCompteRIBIdentifier(compteRIB)}`, compteRIB, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompteRIB>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompteRIB[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCompteRIBIdentifier(compteRIB: Pick<ICompteRIB, 'id'>): number {
    return compteRIB.id;
  }

  compareCompteRIB(o1: Pick<ICompteRIB, 'id'> | null, o2: Pick<ICompteRIB, 'id'> | null): boolean {
    return o1 && o2 ? this.getCompteRIBIdentifier(o1) === this.getCompteRIBIdentifier(o2) : o1 === o2;
  }

  addCompteRIBToCollectionIfMissing<Type extends Pick<ICompteRIB, 'id'>>(
    compteRIBCollection: Type[],
    ...compteRIBSToCheck: (Type | null | undefined)[]
  ): Type[] {
    const compteRIBS: Type[] = compteRIBSToCheck.filter(isPresent);
    if (compteRIBS.length > 0) {
      const compteRIBCollectionIdentifiers = compteRIBCollection.map(compteRIBItem => this.getCompteRIBIdentifier(compteRIBItem)!);
      const compteRIBSToAdd = compteRIBS.filter(compteRIBItem => {
        const compteRIBIdentifier = this.getCompteRIBIdentifier(compteRIBItem);
        if (compteRIBCollectionIdentifiers.includes(compteRIBIdentifier)) {
          return false;
        }
        compteRIBCollectionIdentifiers.push(compteRIBIdentifier);
        return true;
      });
      return [...compteRIBSToAdd, ...compteRIBCollection];
    }
    return compteRIBCollection;
  }
}
