import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDecaissementTontine, NewDecaissementTontine } from '../decaissement-tontine.model';

export type PartialUpdateDecaissementTontine = Partial<IDecaissementTontine> & Pick<IDecaissementTontine, 'id'>;

type RestOf<T extends IDecaissementTontine | NewDecaissementTontine> = Omit<T, 'dateDecaissement'> & {
  dateDecaissement?: string | null;
};

export type RestDecaissementTontine = RestOf<IDecaissementTontine>;

export type NewRestDecaissementTontine = RestOf<NewDecaissementTontine>;

export type PartialUpdateRestDecaissementTontine = RestOf<PartialUpdateDecaissementTontine>;

export type EntityResponseType = HttpResponse<IDecaissementTontine>;
export type EntityArrayResponseType = HttpResponse<IDecaissementTontine[]>;

@Injectable({ providedIn: 'root' })
export class DecaissementTontineService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/decaissement-tontines');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(decaissementTontine: NewDecaissementTontine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decaissementTontine);
    return this.http
      .post<RestDecaissementTontine>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(decaissementTontine: IDecaissementTontine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decaissementTontine);
    return this.http
      .put<RestDecaissementTontine>(`${this.resourceUrl}/${this.getDecaissementTontineIdentifier(decaissementTontine)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(decaissementTontine: PartialUpdateDecaissementTontine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decaissementTontine);
    return this.http
      .patch<RestDecaissementTontine>(`${this.resourceUrl}/${this.getDecaissementTontineIdentifier(decaissementTontine)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDecaissementTontine>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDecaissementTontine[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDecaissementTontineIdentifier(decaissementTontine: Pick<IDecaissementTontine, 'id'>): number {
    return decaissementTontine.id;
  }

  compareDecaissementTontine(o1: Pick<IDecaissementTontine, 'id'> | null, o2: Pick<IDecaissementTontine, 'id'> | null): boolean {
    return o1 && o2 ? this.getDecaissementTontineIdentifier(o1) === this.getDecaissementTontineIdentifier(o2) : o1 === o2;
  }

  addDecaissementTontineToCollectionIfMissing<Type extends Pick<IDecaissementTontine, 'id'>>(
    decaissementTontineCollection: Type[],
    ...decaissementTontinesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const decaissementTontines: Type[] = decaissementTontinesToCheck.filter(isPresent);
    if (decaissementTontines.length > 0) {
      const decaissementTontineCollectionIdentifiers = decaissementTontineCollection.map(
        decaissementTontineItem => this.getDecaissementTontineIdentifier(decaissementTontineItem)!
      );
      const decaissementTontinesToAdd = decaissementTontines.filter(decaissementTontineItem => {
        const decaissementTontineIdentifier = this.getDecaissementTontineIdentifier(decaissementTontineItem);
        if (decaissementTontineCollectionIdentifiers.includes(decaissementTontineIdentifier)) {
          return false;
        }
        decaissementTontineCollectionIdentifiers.push(decaissementTontineIdentifier);
        return true;
      });
      return [...decaissementTontinesToAdd, ...decaissementTontineCollection];
    }
    return decaissementTontineCollection;
  }

  protected convertDateFromClient<T extends IDecaissementTontine | NewDecaissementTontine | PartialUpdateDecaissementTontine>(
    decaissementTontine: T
  ): RestOf<T> {
    return {
      ...decaissementTontine,
      dateDecaissement: decaissementTontine.dateDecaissement?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restDecaissementTontine: RestDecaissementTontine): IDecaissementTontine {
    return {
      ...restDecaissementTontine,
      dateDecaissement: restDecaissementTontine.dateDecaissement ? dayjs(restDecaissementTontine.dateDecaissement) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDecaissementTontine>): HttpResponse<IDecaissementTontine> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDecaissementTontine[]>): HttpResponse<IDecaissementTontine[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
