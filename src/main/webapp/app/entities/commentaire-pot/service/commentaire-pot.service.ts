import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommentairePot, NewCommentairePot } from '../commentaire-pot.model';

export type PartialUpdateCommentairePot = Partial<ICommentairePot> & Pick<ICommentairePot, 'id'>;

type RestOf<T extends ICommentairePot | NewCommentairePot> = Omit<T, 'dateComentaire'> & {
  dateComentaire?: string | null;
};

export type RestCommentairePot = RestOf<ICommentairePot>;

export type NewRestCommentairePot = RestOf<NewCommentairePot>;

export type PartialUpdateRestCommentairePot = RestOf<PartialUpdateCommentairePot>;

export type EntityResponseType = HttpResponse<ICommentairePot>;
export type EntityArrayResponseType = HttpResponse<ICommentairePot[]>;

@Injectable({ providedIn: 'root' })
export class CommentairePotService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/commentaire-pots');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(commentairePot: NewCommentairePot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(commentairePot);
    return this.http
      .post<RestCommentairePot>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(commentairePot: ICommentairePot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(commentairePot);
    return this.http
      .put<RestCommentairePot>(`${this.resourceUrl}/${this.getCommentairePotIdentifier(commentairePot)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(commentairePot: PartialUpdateCommentairePot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(commentairePot);
    return this.http
      .patch<RestCommentairePot>(`${this.resourceUrl}/${this.getCommentairePotIdentifier(commentairePot)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCommentairePot>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCommentairePot[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCommentairePotIdentifier(commentairePot: Pick<ICommentairePot, 'id'>): number {
    return commentairePot.id;
  }

  compareCommentairePot(o1: Pick<ICommentairePot, 'id'> | null, o2: Pick<ICommentairePot, 'id'> | null): boolean {
    return o1 && o2 ? this.getCommentairePotIdentifier(o1) === this.getCommentairePotIdentifier(o2) : o1 === o2;
  }

  addCommentairePotToCollectionIfMissing<Type extends Pick<ICommentairePot, 'id'>>(
    commentairePotCollection: Type[],
    ...commentairePotsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const commentairePots: Type[] = commentairePotsToCheck.filter(isPresent);
    if (commentairePots.length > 0) {
      const commentairePotCollectionIdentifiers = commentairePotCollection.map(
        commentairePotItem => this.getCommentairePotIdentifier(commentairePotItem)!
      );
      const commentairePotsToAdd = commentairePots.filter(commentairePotItem => {
        const commentairePotIdentifier = this.getCommentairePotIdentifier(commentairePotItem);
        if (commentairePotCollectionIdentifiers.includes(commentairePotIdentifier)) {
          return false;
        }
        commentairePotCollectionIdentifiers.push(commentairePotIdentifier);
        return true;
      });
      return [...commentairePotsToAdd, ...commentairePotCollection];
    }
    return commentairePotCollection;
  }

  protected convertDateFromClient<T extends ICommentairePot | NewCommentairePot | PartialUpdateCommentairePot>(
    commentairePot: T
  ): RestOf<T> {
    return {
      ...commentairePot,
      dateComentaire: commentairePot.dateComentaire?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCommentairePot: RestCommentairePot): ICommentairePot {
    return {
      ...restCommentairePot,
      dateComentaire: restCommentairePot.dateComentaire ? dayjs(restCommentairePot.dateComentaire) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCommentairePot>): HttpResponse<ICommentairePot> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCommentairePot[]>): HttpResponse<ICommentairePot[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
