import dayjs from 'dayjs/esm';

import { Langue } from 'app/entities/enumerations/langue.model';

import { IAssociation, NewAssociation } from './association.model';

export const sampleWithRequiredData: IAssociation = {
  id: 96936,
  codeAssociation: 'b a pixel',
};

export const sampleWithPartialData: IAssociation = {
  id: 19978,
  codeAssociation: 'Lorraine',
  denomination: 'port parse',
  slogan: 'Borders red',
  reglementPath: 'Provence-Alpes-Côte Granite Loan',
  statutPath: 'Towels',
  description: 'Gloves Rustic Chili',
  fuseauHoraire: 'Champagne-Ardenne bricks-and-clicks heuristic',
  presentation: 'du Bretagne didactic',
};

export const sampleWithFullData: IAssociation = {
  id: 8976,
  codeAssociation: 'Wooden',
  denomination: 'Incredible microchip Bretagne',
  slogan: 'invoice circuit',
  logoPath: 'Granite Administrateur',
  reglementPath: 'Inde',
  statutPath: 'plum',
  description: 'Money',
  dateCreation: dayjs('2023-06-08'),
  fuseauHoraire: 'Chair Iceland',
  langue: Langue['ENGLISH'],
  presentation: 'SAS Berkshire',
};

export const sampleWithNewData: NewAssociation = {
  codeAssociation: 'directional Personal',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
