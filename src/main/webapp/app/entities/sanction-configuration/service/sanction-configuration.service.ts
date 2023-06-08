import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISanctionConfiguration, NewSanctionConfiguration } from '../sanction-configuration.model';

export type PartialUpdateSanctionConfiguration = Partial<ISanctionConfiguration> & Pick<ISanctionConfiguration, 'id'>;

export type EntityResponseType = HttpResponse<ISanctionConfiguration>;
export type EntityArrayResponseType = HttpResponse<ISanctionConfiguration[]>;

@Injectable({ providedIn: 'root' })
export class SanctionConfigurationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sanction-configurations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sanctionConfiguration: NewSanctionConfiguration): Observable<EntityResponseType> {
    return this.http.post<ISanctionConfiguration>(this.resourceUrl, sanctionConfiguration, { observe: 'response' });
  }

  update(sanctionConfiguration: ISanctionConfiguration): Observable<EntityResponseType> {
    return this.http.put<ISanctionConfiguration>(
      `${this.resourceUrl}/${this.getSanctionConfigurationIdentifier(sanctionConfiguration)}`,
      sanctionConfiguration,
      { observe: 'response' }
    );
  }

  partialUpdate(sanctionConfiguration: PartialUpdateSanctionConfiguration): Observable<EntityResponseType> {
    return this.http.patch<ISanctionConfiguration>(
      `${this.resourceUrl}/${this.getSanctionConfigurationIdentifier(sanctionConfiguration)}`,
      sanctionConfiguration,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISanctionConfiguration>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISanctionConfiguration[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSanctionConfigurationIdentifier(sanctionConfiguration: Pick<ISanctionConfiguration, 'id'>): number {
    return sanctionConfiguration.id;
  }

  compareSanctionConfiguration(o1: Pick<ISanctionConfiguration, 'id'> | null, o2: Pick<ISanctionConfiguration, 'id'> | null): boolean {
    return o1 && o2 ? this.getSanctionConfigurationIdentifier(o1) === this.getSanctionConfigurationIdentifier(o2) : o1 === o2;
  }

  addSanctionConfigurationToCollectionIfMissing<Type extends Pick<ISanctionConfiguration, 'id'>>(
    sanctionConfigurationCollection: Type[],
    ...sanctionConfigurationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sanctionConfigurations: Type[] = sanctionConfigurationsToCheck.filter(isPresent);
    if (sanctionConfigurations.length > 0) {
      const sanctionConfigurationCollectionIdentifiers = sanctionConfigurationCollection.map(
        sanctionConfigurationItem => this.getSanctionConfigurationIdentifier(sanctionConfigurationItem)!
      );
      const sanctionConfigurationsToAdd = sanctionConfigurations.filter(sanctionConfigurationItem => {
        const sanctionConfigurationIdentifier = this.getSanctionConfigurationIdentifier(sanctionConfigurationItem);
        if (sanctionConfigurationCollectionIdentifiers.includes(sanctionConfigurationIdentifier)) {
          return false;
        }
        sanctionConfigurationCollectionIdentifiers.push(sanctionConfigurationIdentifier);
        return true;
      });
      return [...sanctionConfigurationsToAdd, ...sanctionConfigurationCollection];
    }
    return sanctionConfigurationCollection;
  }
}
