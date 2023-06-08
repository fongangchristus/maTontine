import dayjs from 'dayjs/esm';

import { IContributionPot, NewContributionPot } from './contribution-pot.model';

export const sampleWithRequiredData: IContributionPot = {
  id: 90869,
  matriculeContributeur: 'Gorgeous hacking',
};

export const sampleWithPartialData: IContributionPot = {
  id: 83322,
  identifiant: 'b architectures',
  matriculeContributeur: 'open-source interface Gabon',
};

export const sampleWithFullData: IContributionPot = {
  id: 83931,
  identifiant: 'Latvian Bretagne',
  matriculeContributeur: 'Tools',
  montantContribution: 46686,
  dateContribution: dayjs('2023-06-08T00:24'),
};

export const sampleWithNewData: NewContributionPot = {
  matriculeContributeur: 'Generic matrix Virtual',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
