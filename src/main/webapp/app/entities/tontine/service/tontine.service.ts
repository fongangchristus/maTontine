import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITontine, NewTontine } from '../tontine.model';

export type PartialUpdateTontine = Partial<ITontine> & Pick<ITontine, 'id'>;

type RestOf<T extends ITontine | NewTontine> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestTontine = RestOf<ITontine>;

export type NewRestTontine = RestOf<NewTontine>;

export type PartialUpdateRestTontine = RestOf<PartialUpdateTontine>;

export type EntityResponseType = HttpResponse<ITontine>;
export type EntityArrayResponseType = HttpResponse<ITontine[]>;

@Injectable({ providedIn: 'root' })
export class TontineService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tontines');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tontine: NewTontine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tontine);
    return this.http
      .post<RestTontine>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tontine: ITontine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tontine);
    return this.http
      .put<RestTontine>(`${this.resourceUrl}/${this.getTontineIdentifier(tontine)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tontine: PartialUpdateTontine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tontine);
    return this.http
      .patch<RestTontine>(`${this.resourceUrl}/${this.getTontineIdentifier(tontine)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTontine>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTontine[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTontineIdentifier(tontine: Pick<ITontine, 'id'>): number {
    return tontine.id;
  }

  compareTontine(o1: Pick<ITontine, 'id'> | null, o2: Pick<ITontine, 'id'> | null): boolean {
    return o1 && o2 ? this.getTontineIdentifier(o1) === this.getTontineIdentifier(o2) : o1 === o2;
  }

  addTontineToCollectionIfMissing<Type extends Pick<ITontine, 'id'>>(
    tontineCollection: Type[],
    ...tontinesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tontines: Type[] = tontinesToCheck.filter(isPresent);
    if (tontines.length > 0) {
      const tontineCollectionIdentifiers = tontineCollection.map(tontineItem => this.getTontineIdentifier(tontineItem)!);
      const tontinesToAdd = tontines.filter(tontineItem => {
        const tontineIdentifier = this.getTontineIdentifier(tontineItem);
        if (tontineCollectionIdentifiers.includes(tontineIdentifier)) {
          return false;
        }
        tontineCollectionIdentifiers.push(tontineIdentifier);
        return true;
      });
      return [...tontinesToAdd, ...tontineCollection];
    }
    return tontineCollection;
  }

  protected convertDateFromClient<T extends ITontine | NewTontine | PartialUpdateTontine>(tontine: T): RestOf<T> {
    return {
      ...tontine,
      dateDebut: tontine.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: tontine.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restTontine: RestTontine): ITontine {
    return {
      ...restTontine,
      dateDebut: restTontine.dateDebut ? dayjs(restTontine.dateDebut) : undefined,
      dateFin: restTontine.dateFin ? dayjs(restTontine.dateFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTontine>): HttpResponse<ITontine> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTontine[]>): HttpResponse<ITontine[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
