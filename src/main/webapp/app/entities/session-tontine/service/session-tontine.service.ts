import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISessionTontine, NewSessionTontine } from '../session-tontine.model';

export type PartialUpdateSessionTontine = Partial<ISessionTontine> & Pick<ISessionTontine, 'id'>;

type RestOf<T extends ISessionTontine | NewSessionTontine> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestSessionTontine = RestOf<ISessionTontine>;

export type NewRestSessionTontine = RestOf<NewSessionTontine>;

export type PartialUpdateRestSessionTontine = RestOf<PartialUpdateSessionTontine>;

export type EntityResponseType = HttpResponse<ISessionTontine>;
export type EntityArrayResponseType = HttpResponse<ISessionTontine[]>;

@Injectable({ providedIn: 'root' })
export class SessionTontineService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/session-tontines');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sessionTontine: NewSessionTontine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sessionTontine);
    return this.http
      .post<RestSessionTontine>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(sessionTontine: ISessionTontine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sessionTontine);
    return this.http
      .put<RestSessionTontine>(`${this.resourceUrl}/${this.getSessionTontineIdentifier(sessionTontine)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(sessionTontine: PartialUpdateSessionTontine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sessionTontine);
    return this.http
      .patch<RestSessionTontine>(`${this.resourceUrl}/${this.getSessionTontineIdentifier(sessionTontine)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSessionTontine>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSessionTontine[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSessionTontineIdentifier(sessionTontine: Pick<ISessionTontine, 'id'>): number {
    return sessionTontine.id;
  }

  compareSessionTontine(o1: Pick<ISessionTontine, 'id'> | null, o2: Pick<ISessionTontine, 'id'> | null): boolean {
    return o1 && o2 ? this.getSessionTontineIdentifier(o1) === this.getSessionTontineIdentifier(o2) : o1 === o2;
  }

  addSessionTontineToCollectionIfMissing<Type extends Pick<ISessionTontine, 'id'>>(
    sessionTontineCollection: Type[],
    ...sessionTontinesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sessionTontines: Type[] = sessionTontinesToCheck.filter(isPresent);
    if (sessionTontines.length > 0) {
      const sessionTontineCollectionIdentifiers = sessionTontineCollection.map(
        sessionTontineItem => this.getSessionTontineIdentifier(sessionTontineItem)!
      );
      const sessionTontinesToAdd = sessionTontines.filter(sessionTontineItem => {
        const sessionTontineIdentifier = this.getSessionTontineIdentifier(sessionTontineItem);
        if (sessionTontineCollectionIdentifiers.includes(sessionTontineIdentifier)) {
          return false;
        }
        sessionTontineCollectionIdentifiers.push(sessionTontineIdentifier);
        return true;
      });
      return [...sessionTontinesToAdd, ...sessionTontineCollection];
    }
    return sessionTontineCollection;
  }

  protected convertDateFromClient<T extends ISessionTontine | NewSessionTontine | PartialUpdateSessionTontine>(
    sessionTontine: T
  ): RestOf<T> {
    return {
      ...sessionTontine,
      dateDebut: sessionTontine.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: sessionTontine.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSessionTontine: RestSessionTontine): ISessionTontine {
    return {
      ...restSessionTontine,
      dateDebut: restSessionTontine.dateDebut ? dayjs(restSessionTontine.dateDebut) : undefined,
      dateFin: restSessionTontine.dateFin ? dayjs(restSessionTontine.dateFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSessionTontine>): HttpResponse<ISessionTontine> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSessionTontine[]>): HttpResponse<ISessionTontine[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
