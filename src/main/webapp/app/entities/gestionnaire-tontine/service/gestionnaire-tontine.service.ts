import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGestionnaireTontine, NewGestionnaireTontine } from '../gestionnaire-tontine.model';

export type PartialUpdateGestionnaireTontine = Partial<IGestionnaireTontine> & Pick<IGestionnaireTontine, 'id'>;

type RestOf<T extends IGestionnaireTontine | NewGestionnaireTontine> = Omit<T, 'datePriseFonction' | 'dateFinFonction'> & {
  datePriseFonction?: string | null;
  dateFinFonction?: string | null;
};

export type RestGestionnaireTontine = RestOf<IGestionnaireTontine>;

export type NewRestGestionnaireTontine = RestOf<NewGestionnaireTontine>;

export type PartialUpdateRestGestionnaireTontine = RestOf<PartialUpdateGestionnaireTontine>;

export type EntityResponseType = HttpResponse<IGestionnaireTontine>;
export type EntityArrayResponseType = HttpResponse<IGestionnaireTontine[]>;

@Injectable({ providedIn: 'root' })
export class GestionnaireTontineService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/gestionnaire-tontines');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gestionnaireTontine: NewGestionnaireTontine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gestionnaireTontine);
    return this.http
      .post<RestGestionnaireTontine>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(gestionnaireTontine: IGestionnaireTontine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gestionnaireTontine);
    return this.http
      .put<RestGestionnaireTontine>(`${this.resourceUrl}/${this.getGestionnaireTontineIdentifier(gestionnaireTontine)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(gestionnaireTontine: PartialUpdateGestionnaireTontine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gestionnaireTontine);
    return this.http
      .patch<RestGestionnaireTontine>(`${this.resourceUrl}/${this.getGestionnaireTontineIdentifier(gestionnaireTontine)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestGestionnaireTontine>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestGestionnaireTontine[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGestionnaireTontineIdentifier(gestionnaireTontine: Pick<IGestionnaireTontine, 'id'>): number {
    return gestionnaireTontine.id;
  }

  compareGestionnaireTontine(o1: Pick<IGestionnaireTontine, 'id'> | null, o2: Pick<IGestionnaireTontine, 'id'> | null): boolean {
    return o1 && o2 ? this.getGestionnaireTontineIdentifier(o1) === this.getGestionnaireTontineIdentifier(o2) : o1 === o2;
  }

  addGestionnaireTontineToCollectionIfMissing<Type extends Pick<IGestionnaireTontine, 'id'>>(
    gestionnaireTontineCollection: Type[],
    ...gestionnaireTontinesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const gestionnaireTontines: Type[] = gestionnaireTontinesToCheck.filter(isPresent);
    if (gestionnaireTontines.length > 0) {
      const gestionnaireTontineCollectionIdentifiers = gestionnaireTontineCollection.map(
        gestionnaireTontineItem => this.getGestionnaireTontineIdentifier(gestionnaireTontineItem)!
      );
      const gestionnaireTontinesToAdd = gestionnaireTontines.filter(gestionnaireTontineItem => {
        const gestionnaireTontineIdentifier = this.getGestionnaireTontineIdentifier(gestionnaireTontineItem);
        if (gestionnaireTontineCollectionIdentifiers.includes(gestionnaireTontineIdentifier)) {
          return false;
        }
        gestionnaireTontineCollectionIdentifiers.push(gestionnaireTontineIdentifier);
        return true;
      });
      return [...gestionnaireTontinesToAdd, ...gestionnaireTontineCollection];
    }
    return gestionnaireTontineCollection;
  }

  protected convertDateFromClient<T extends IGestionnaireTontine | NewGestionnaireTontine | PartialUpdateGestionnaireTontine>(
    gestionnaireTontine: T
  ): RestOf<T> {
    return {
      ...gestionnaireTontine,
      datePriseFonction: gestionnaireTontine.datePriseFonction?.format(DATE_FORMAT) ?? null,
      dateFinFonction: gestionnaireTontine.dateFinFonction?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restGestionnaireTontine: RestGestionnaireTontine): IGestionnaireTontine {
    return {
      ...restGestionnaireTontine,
      datePriseFonction: restGestionnaireTontine.datePriseFonction ? dayjs(restGestionnaireTontine.datePriseFonction) : undefined,
      dateFinFonction: restGestionnaireTontine.dateFinFonction ? dayjs(restGestionnaireTontine.dateFinFonction) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestGestionnaireTontine>): HttpResponse<IGestionnaireTontine> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestGestionnaireTontine[]>): HttpResponse<IGestionnaireTontine[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
