import dayjs from 'dayjs/esm';

import { Langue } from 'app/entities/enumerations/langue.model';

import { IAssociation, NewAssociation } from './association.model';

export const sampleWithRequiredData: IAssociation = {
  id: 96936,
  codeAssociation: 'b a pixel',
};

export const sampleWithPartialData: IAssociation = {
  id: 66581,
  codeAssociation: 'synergistic improvement',
  denomination: 'Berkshire a c',
  slogan: 'proactive Berkshire Loan',
  reglementPath: 'Towels',
  statutPath: 'Gloves Rustic Chili',
  description: 'Champagne-Ardenne bricks-and-clicks heuristic',
  fuseauHoraire: 'du Bretagne didactic',
  presentation: 'Cross-platform',
  isActif: false,
};

export const sampleWithFullData: IAssociation = {
  id: 75379,
  codeAssociation: 'matrices',
  denomination: 'incubate',
  slogan: 'Borders Chicken b',
  logoPath: 'Afghanistan Administrateur Rubber',
  reglementPath: 'concept',
  statutPath: 'mobile',
  description: 'e-markets',
  dateCreation: dayjs('2023-06-07'),
  fuseauHoraire: 'Sports implement Poitou-Charentes',
  langue: Langue['FRENCH'],
  presentation: 'Provence directional',
  siegeSocial: 'a THX',
  email: 'Mauricette46@yahoo.fr',
  isActif: true,
};

export const sampleWithNewData: NewAssociation = {
  codeAssociation: 'Rosiers a',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
