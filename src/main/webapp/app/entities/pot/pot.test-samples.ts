import dayjs from 'dayjs/esm';

import { StatutPot } from 'app/entities/enumerations/statut-pot.model';

import { IPot, NewPot } from './pot.model';

export const sampleWithRequiredData: IPot = {
  id: 82872,
  codepot: 'Champagne-Ardenne',
};

export const sampleWithPartialData: IPot = {
  id: 87386,
  libele: 'attitude b',
  codepot: 'background AI schemas',
  montantCible: 48577,
  description: 'convergence',
};

export const sampleWithFullData: IPot = {
  id: 95683,
  libele: 'Account',
  codepot: 'Handcrafted',
  montantCible: 14793,
  description: 'frame core',
  dateDebutCollecte: dayjs('2023-06-08'),
  dateFinCollecte: dayjs('2023-06-08'),
  statut: StatutPot['OUVERTE'],
};

export const sampleWithNewData: NewPot = {
  codepot: 'Mouse transmit',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
