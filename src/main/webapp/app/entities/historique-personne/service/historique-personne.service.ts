import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHistoriquePersonne, NewHistoriquePersonne } from '../historique-personne.model';

export type PartialUpdateHistoriquePersonne = Partial<IHistoriquePersonne> & Pick<IHistoriquePersonne, 'id'>;

type RestOf<T extends IHistoriquePersonne | NewHistoriquePersonne> = Omit<T, 'dateAction'> & {
  dateAction?: string | null;
};

export type RestHistoriquePersonne = RestOf<IHistoriquePersonne>;

export type NewRestHistoriquePersonne = RestOf<NewHistoriquePersonne>;

export type PartialUpdateRestHistoriquePersonne = RestOf<PartialUpdateHistoriquePersonne>;

export type EntityResponseType = HttpResponse<IHistoriquePersonne>;
export type EntityArrayResponseType = HttpResponse<IHistoriquePersonne[]>;

@Injectable({ providedIn: 'root' })
export class HistoriquePersonneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/historique-personnes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(historiquePersonne: NewHistoriquePersonne): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historiquePersonne);
    return this.http
      .post<RestHistoriquePersonne>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(historiquePersonne: IHistoriquePersonne): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historiquePersonne);
    return this.http
      .put<RestHistoriquePersonne>(`${this.resourceUrl}/${this.getHistoriquePersonneIdentifier(historiquePersonne)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(historiquePersonne: PartialUpdateHistoriquePersonne): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historiquePersonne);
    return this.http
      .patch<RestHistoriquePersonne>(`${this.resourceUrl}/${this.getHistoriquePersonneIdentifier(historiquePersonne)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHistoriquePersonne>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHistoriquePersonne[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHistoriquePersonneIdentifier(historiquePersonne: Pick<IHistoriquePersonne, 'id'>): number {
    return historiquePersonne.id;
  }

  compareHistoriquePersonne(o1: Pick<IHistoriquePersonne, 'id'> | null, o2: Pick<IHistoriquePersonne, 'id'> | null): boolean {
    return o1 && o2 ? this.getHistoriquePersonneIdentifier(o1) === this.getHistoriquePersonneIdentifier(o2) : o1 === o2;
  }

  addHistoriquePersonneToCollectionIfMissing<Type extends Pick<IHistoriquePersonne, 'id'>>(
    historiquePersonneCollection: Type[],
    ...historiquePersonnesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const historiquePersonnes: Type[] = historiquePersonnesToCheck.filter(isPresent);
    if (historiquePersonnes.length > 0) {
      const historiquePersonneCollectionIdentifiers = historiquePersonneCollection.map(
        historiquePersonneItem => this.getHistoriquePersonneIdentifier(historiquePersonneItem)!
      );
      const historiquePersonnesToAdd = historiquePersonnes.filter(historiquePersonneItem => {
        const historiquePersonneIdentifier = this.getHistoriquePersonneIdentifier(historiquePersonneItem);
        if (historiquePersonneCollectionIdentifiers.includes(historiquePersonneIdentifier)) {
          return false;
        }
        historiquePersonneCollectionIdentifiers.push(historiquePersonneIdentifier);
        return true;
      });
      return [...historiquePersonnesToAdd, ...historiquePersonneCollection];
    }
    return historiquePersonneCollection;
  }

  protected convertDateFromClient<T extends IHistoriquePersonne | NewHistoriquePersonne | PartialUpdateHistoriquePersonne>(
    historiquePersonne: T
  ): RestOf<T> {
    return {
      ...historiquePersonne,
      dateAction: historiquePersonne.dateAction?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restHistoriquePersonne: RestHistoriquePersonne): IHistoriquePersonne {
    return {
      ...restHistoriquePersonne,
      dateAction: restHistoriquePersonne.dateAction ? dayjs(restHistoriquePersonne.dateAction) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHistoriquePersonne>): HttpResponse<IHistoriquePersonne> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHistoriquePersonne[]>): HttpResponse<IHistoriquePersonne[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
