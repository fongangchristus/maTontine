import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFormuleAdhesion, NewFormuleAdhesion } from '../formule-adhesion.model';

export type PartialUpdateFormuleAdhesion = Partial<IFormuleAdhesion> & Pick<IFormuleAdhesion, 'id'>;

type RestOf<T extends IFormuleAdhesion | NewFormuleAdhesion> = Omit<T, 'dateDebut'> & {
  dateDebut?: string | null;
};

export type RestFormuleAdhesion = RestOf<IFormuleAdhesion>;

export type NewRestFormuleAdhesion = RestOf<NewFormuleAdhesion>;

export type PartialUpdateRestFormuleAdhesion = RestOf<PartialUpdateFormuleAdhesion>;

export type EntityResponseType = HttpResponse<IFormuleAdhesion>;
export type EntityArrayResponseType = HttpResponse<IFormuleAdhesion[]>;

@Injectable({ providedIn: 'root' })
export class FormuleAdhesionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/formule-adhesions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(formuleAdhesion: NewFormuleAdhesion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formuleAdhesion);
    return this.http
      .post<RestFormuleAdhesion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(formuleAdhesion: IFormuleAdhesion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formuleAdhesion);
    return this.http
      .put<RestFormuleAdhesion>(`${this.resourceUrl}/${this.getFormuleAdhesionIdentifier(formuleAdhesion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(formuleAdhesion: PartialUpdateFormuleAdhesion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formuleAdhesion);
    return this.http
      .patch<RestFormuleAdhesion>(`${this.resourceUrl}/${this.getFormuleAdhesionIdentifier(formuleAdhesion)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFormuleAdhesion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFormuleAdhesion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFormuleAdhesionIdentifier(formuleAdhesion: Pick<IFormuleAdhesion, 'id'>): number {
    return formuleAdhesion.id;
  }

  compareFormuleAdhesion(o1: Pick<IFormuleAdhesion, 'id'> | null, o2: Pick<IFormuleAdhesion, 'id'> | null): boolean {
    return o1 && o2 ? this.getFormuleAdhesionIdentifier(o1) === this.getFormuleAdhesionIdentifier(o2) : o1 === o2;
  }

  addFormuleAdhesionToCollectionIfMissing<Type extends Pick<IFormuleAdhesion, 'id'>>(
    formuleAdhesionCollection: Type[],
    ...formuleAdhesionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const formuleAdhesions: Type[] = formuleAdhesionsToCheck.filter(isPresent);
    if (formuleAdhesions.length > 0) {
      const formuleAdhesionCollectionIdentifiers = formuleAdhesionCollection.map(
        formuleAdhesionItem => this.getFormuleAdhesionIdentifier(formuleAdhesionItem)!
      );
      const formuleAdhesionsToAdd = formuleAdhesions.filter(formuleAdhesionItem => {
        const formuleAdhesionIdentifier = this.getFormuleAdhesionIdentifier(formuleAdhesionItem);
        if (formuleAdhesionCollectionIdentifiers.includes(formuleAdhesionIdentifier)) {
          return false;
        }
        formuleAdhesionCollectionIdentifiers.push(formuleAdhesionIdentifier);
        return true;
      });
      return [...formuleAdhesionsToAdd, ...formuleAdhesionCollection];
    }
    return formuleAdhesionCollection;
  }

  protected convertDateFromClient<T extends IFormuleAdhesion | NewFormuleAdhesion | PartialUpdateFormuleAdhesion>(
    formuleAdhesion: T
  ): RestOf<T> {
    return {
      ...formuleAdhesion,
      dateDebut: formuleAdhesion.dateDebut?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFormuleAdhesion: RestFormuleAdhesion): IFormuleAdhesion {
    return {
      ...restFormuleAdhesion,
      dateDebut: restFormuleAdhesion.dateDebut ? dayjs(restFormuleAdhesion.dateDebut) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFormuleAdhesion>): HttpResponse<IFormuleAdhesion> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFormuleAdhesion[]>): HttpResponse<IFormuleAdhesion[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
