import dayjs from 'dayjs/esm';

import { IBanque, NewBanque } from './banque.model';

export const sampleWithRequiredData: IBanque = {
  id: 83469,
  codeAssociation: 'Laos',
};

export const sampleWithPartialData: IBanque = {
  id: 78703,
  codeAssociation: 'Île-de-France Developpeur',
  dateOuverture: dayjs('2023-06-07T16:52'),
  dateCloture: dayjs('2023-06-07T17:10'),
};

export const sampleWithFullData: IBanque = {
  id: 87398,
  codeAssociation: 'Chair portals',
  libelle: 'capacitor parse bandwidth',
  description: 'des monitoring',
  dateOuverture: dayjs('2023-06-07T14:59'),
  dateCloture: dayjs('2023-06-08T10:20'),
};

export const sampleWithNewData: NewBanque = {
  codeAssociation: 'cross-platform intuitive',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
