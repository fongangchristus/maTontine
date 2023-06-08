import dayjs from 'dayjs/esm';

import { IHistoriquePersonne, NewHistoriquePersonne } from './historique-personne.model';

export const sampleWithRequiredData: IHistoriquePersonne = {
  id: 51514,
  matriculePersonne: 'Joubert',
};

export const sampleWithPartialData: IHistoriquePersonne = {
  id: 32180,
  matriculePersonne: 'holistic teal indexing',
};

export const sampleWithFullData: IHistoriquePersonne = {
  id: 12327,
  dateAction: dayjs('2023-06-08T09:56'),
  matriculePersonne: 'Producteur',
  action: 'Analyste Plastic',
  result: 'embrace Wooden web',
  description: 'Royale Birr',
};

export const sampleWithNewData: NewHistoriquePersonne = {
  matriculePersonne: 'monitor',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
