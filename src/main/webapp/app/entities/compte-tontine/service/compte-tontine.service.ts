import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompteTontine, NewCompteTontine } from '../compte-tontine.model';

export type PartialUpdateCompteTontine = Partial<ICompteTontine> & Pick<ICompteTontine, 'id'>;

export type EntityResponseType = HttpResponse<ICompteTontine>;
export type EntityArrayResponseType = HttpResponse<ICompteTontine[]>;

@Injectable({ providedIn: 'root' })
export class CompteTontineService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/compte-tontines');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(compteTontine: NewCompteTontine): Observable<EntityResponseType> {
    return this.http.post<ICompteTontine>(this.resourceUrl, compteTontine, { observe: 'response' });
  }

  update(compteTontine: ICompteTontine): Observable<EntityResponseType> {
    return this.http.put<ICompteTontine>(`${this.resourceUrl}/${this.getCompteTontineIdentifier(compteTontine)}`, compteTontine, {
      observe: 'response',
    });
  }

  partialUpdate(compteTontine: PartialUpdateCompteTontine): Observable<EntityResponseType> {
    return this.http.patch<ICompteTontine>(`${this.resourceUrl}/${this.getCompteTontineIdentifier(compteTontine)}`, compteTontine, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompteTontine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompteTontine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCompteTontineIdentifier(compteTontine: Pick<ICompteTontine, 'id'>): number {
    return compteTontine.id;
  }

  compareCompteTontine(o1: Pick<ICompteTontine, 'id'> | null, o2: Pick<ICompteTontine, 'id'> | null): boolean {
    return o1 && o2 ? this.getCompteTontineIdentifier(o1) === this.getCompteTontineIdentifier(o2) : o1 === o2;
  }

  addCompteTontineToCollectionIfMissing<Type extends Pick<ICompteTontine, 'id'>>(
    compteTontineCollection: Type[],
    ...compteTontinesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const compteTontines: Type[] = compteTontinesToCheck.filter(isPresent);
    if (compteTontines.length > 0) {
      const compteTontineCollectionIdentifiers = compteTontineCollection.map(
        compteTontineItem => this.getCompteTontineIdentifier(compteTontineItem)!
      );
      const compteTontinesToAdd = compteTontines.filter(compteTontineItem => {
        const compteTontineIdentifier = this.getCompteTontineIdentifier(compteTontineItem);
        if (compteTontineCollectionIdentifiers.includes(compteTontineIdentifier)) {
          return false;
        }
        compteTontineCollectionIdentifiers.push(compteTontineIdentifier);
        return true;
      });
      return [...compteTontinesToAdd, ...compteTontineCollection];
    }
    return compteTontineCollection;
  }
}
