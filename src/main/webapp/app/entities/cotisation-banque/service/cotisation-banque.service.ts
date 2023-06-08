import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICotisationBanque, NewCotisationBanque } from '../cotisation-banque.model';

export type PartialUpdateCotisationBanque = Partial<ICotisationBanque> & Pick<ICotisationBanque, 'id'>;

type RestOf<T extends ICotisationBanque | NewCotisationBanque> = Omit<T, 'dateCotisation'> & {
  dateCotisation?: string | null;
};

export type RestCotisationBanque = RestOf<ICotisationBanque>;

export type NewRestCotisationBanque = RestOf<NewCotisationBanque>;

export type PartialUpdateRestCotisationBanque = RestOf<PartialUpdateCotisationBanque>;

export type EntityResponseType = HttpResponse<ICotisationBanque>;
export type EntityArrayResponseType = HttpResponse<ICotisationBanque[]>;

@Injectable({ providedIn: 'root' })
export class CotisationBanqueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cotisation-banques');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cotisationBanque: NewCotisationBanque): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cotisationBanque);
    return this.http
      .post<RestCotisationBanque>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cotisationBanque: ICotisationBanque): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cotisationBanque);
    return this.http
      .put<RestCotisationBanque>(`${this.resourceUrl}/${this.getCotisationBanqueIdentifier(cotisationBanque)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cotisationBanque: PartialUpdateCotisationBanque): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cotisationBanque);
    return this.http
      .patch<RestCotisationBanque>(`${this.resourceUrl}/${this.getCotisationBanqueIdentifier(cotisationBanque)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCotisationBanque>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCotisationBanque[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCotisationBanqueIdentifier(cotisationBanque: Pick<ICotisationBanque, 'id'>): number {
    return cotisationBanque.id;
  }

  compareCotisationBanque(o1: Pick<ICotisationBanque, 'id'> | null, o2: Pick<ICotisationBanque, 'id'> | null): boolean {
    return o1 && o2 ? this.getCotisationBanqueIdentifier(o1) === this.getCotisationBanqueIdentifier(o2) : o1 === o2;
  }

  addCotisationBanqueToCollectionIfMissing<Type extends Pick<ICotisationBanque, 'id'>>(
    cotisationBanqueCollection: Type[],
    ...cotisationBanquesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cotisationBanques: Type[] = cotisationBanquesToCheck.filter(isPresent);
    if (cotisationBanques.length > 0) {
      const cotisationBanqueCollectionIdentifiers = cotisationBanqueCollection.map(
        cotisationBanqueItem => this.getCotisationBanqueIdentifier(cotisationBanqueItem)!
      );
      const cotisationBanquesToAdd = cotisationBanques.filter(cotisationBanqueItem => {
        const cotisationBanqueIdentifier = this.getCotisationBanqueIdentifier(cotisationBanqueItem);
        if (cotisationBanqueCollectionIdentifiers.includes(cotisationBanqueIdentifier)) {
          return false;
        }
        cotisationBanqueCollectionIdentifiers.push(cotisationBanqueIdentifier);
        return true;
      });
      return [...cotisationBanquesToAdd, ...cotisationBanqueCollection];
    }
    return cotisationBanqueCollection;
  }

  protected convertDateFromClient<T extends ICotisationBanque | NewCotisationBanque | PartialUpdateCotisationBanque>(
    cotisationBanque: T
  ): RestOf<T> {
    return {
      ...cotisationBanque,
      dateCotisation: cotisationBanque.dateCotisation?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCotisationBanque: RestCotisationBanque): ICotisationBanque {
    return {
      ...restCotisationBanque,
      dateCotisation: restCotisationBanque.dateCotisation ? dayjs(restCotisationBanque.dateCotisation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCotisationBanque>): HttpResponse<ICotisationBanque> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCotisationBanque[]>): HttpResponse<ICotisationBanque[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
