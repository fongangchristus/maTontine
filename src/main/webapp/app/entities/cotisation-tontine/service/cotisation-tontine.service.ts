import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICotisationTontine, NewCotisationTontine } from '../cotisation-tontine.model';

export type PartialUpdateCotisationTontine = Partial<ICotisationTontine> & Pick<ICotisationTontine, 'id'>;

type RestOf<T extends ICotisationTontine | NewCotisationTontine> = Omit<T, 'dateCotisation' | 'dateValidation'> & {
  dateCotisation?: string | null;
  dateValidation?: string | null;
};

export type RestCotisationTontine = RestOf<ICotisationTontine>;

export type NewRestCotisationTontine = RestOf<NewCotisationTontine>;

export type PartialUpdateRestCotisationTontine = RestOf<PartialUpdateCotisationTontine>;

export type EntityResponseType = HttpResponse<ICotisationTontine>;
export type EntityArrayResponseType = HttpResponse<ICotisationTontine[]>;

@Injectable({ providedIn: 'root' })
export class CotisationTontineService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cotisation-tontines');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cotisationTontine: NewCotisationTontine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cotisationTontine);
    return this.http
      .post<RestCotisationTontine>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cotisationTontine: ICotisationTontine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cotisationTontine);
    return this.http
      .put<RestCotisationTontine>(`${this.resourceUrl}/${this.getCotisationTontineIdentifier(cotisationTontine)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cotisationTontine: PartialUpdateCotisationTontine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cotisationTontine);
    return this.http
      .patch<RestCotisationTontine>(`${this.resourceUrl}/${this.getCotisationTontineIdentifier(cotisationTontine)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCotisationTontine>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCotisationTontine[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCotisationTontineIdentifier(cotisationTontine: Pick<ICotisationTontine, 'id'>): number {
    return cotisationTontine.id;
  }

  compareCotisationTontine(o1: Pick<ICotisationTontine, 'id'> | null, o2: Pick<ICotisationTontine, 'id'> | null): boolean {
    return o1 && o2 ? this.getCotisationTontineIdentifier(o1) === this.getCotisationTontineIdentifier(o2) : o1 === o2;
  }

  addCotisationTontineToCollectionIfMissing<Type extends Pick<ICotisationTontine, 'id'>>(
    cotisationTontineCollection: Type[],
    ...cotisationTontinesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cotisationTontines: Type[] = cotisationTontinesToCheck.filter(isPresent);
    if (cotisationTontines.length > 0) {
      const cotisationTontineCollectionIdentifiers = cotisationTontineCollection.map(
        cotisationTontineItem => this.getCotisationTontineIdentifier(cotisationTontineItem)!
      );
      const cotisationTontinesToAdd = cotisationTontines.filter(cotisationTontineItem => {
        const cotisationTontineIdentifier = this.getCotisationTontineIdentifier(cotisationTontineItem);
        if (cotisationTontineCollectionIdentifiers.includes(cotisationTontineIdentifier)) {
          return false;
        }
        cotisationTontineCollectionIdentifiers.push(cotisationTontineIdentifier);
        return true;
      });
      return [...cotisationTontinesToAdd, ...cotisationTontineCollection];
    }
    return cotisationTontineCollection;
  }

  protected convertDateFromClient<T extends ICotisationTontine | NewCotisationTontine | PartialUpdateCotisationTontine>(
    cotisationTontine: T
  ): RestOf<T> {
    return {
      ...cotisationTontine,
      dateCotisation: cotisationTontine.dateCotisation?.format(DATE_FORMAT) ?? null,
      dateValidation: cotisationTontine.dateValidation?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCotisationTontine: RestCotisationTontine): ICotisationTontine {
    return {
      ...restCotisationTontine,
      dateCotisation: restCotisationTontine.dateCotisation ? dayjs(restCotisationTontine.dateCotisation) : undefined,
      dateValidation: restCotisationTontine.dateValidation ? dayjs(restCotisationTontine.dateValidation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCotisationTontine>): HttpResponse<ICotisationTontine> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCotisationTontine[]>): HttpResponse<ICotisationTontine[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
