import dayjs from 'dayjs/esm';

import { IFichePresence, NewFichePresence } from './fiche-presence.model';

export const sampleWithRequiredData: IFichePresence = {
  id: 31148,
};

export const sampleWithPartialData: IFichePresence = {
  id: 77601,
  libelle: 'panel Tasty target',
  description: 'Home Agent',
};

export const sampleWithFullData: IFichePresence = {
  id: 59308,
  libelle: 'Vaneau',
  dateJour: dayjs('2023-06-07T16:31'),
  description: 'Lorraine hard c',
  codeAssemble: 'Ergonomic pixel',
  codeEvenement: 'Designer',
};

export const sampleWithNewData: NewFichePresence = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
