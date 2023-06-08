import { NatureAssemble } from 'app/entities/enumerations/nature-assemble.model';

import { IAssemble, NewAssemble } from './assemble.model';

export const sampleWithRequiredData: IAssemble = {
  id: 86960,
  codeAssociation: 'primary',
};

export const sampleWithPartialData: IAssemble = {
  id: 29562,
  codeAssociation: 'Music',
  libele: 'invoice',
  enLigne: false,
  dateSeance: 'Rubber architectures',
  lieuSeance: 'haptic Architecte',
  matriculeMembreRecoit: 'Cambridgeshire',
  compteRendu: 'generation array indigo',
  resumeAssemble: 'Buckinghamshire Chypre b',
  documentCRPath: 'Keyboard SSL',
};

export const sampleWithFullData: IAssemble = {
  id: 97178,
  codeAssociation: 'Loan turquoise Car',
  libele: 'Sum disintermediate Denar',
  enLigne: true,
  dateSeance: 'fuchsia wireless Kenya',
  lieuSeance: 'exploit Kids a',
  matriculeMembreRecoit: 'Gibraltar Intelligent',
  nature: NatureAssemble['MIXTE'],
  compteRendu: 'Integrated Wooden',
  resumeAssemble: 'b Fresh deposit',
  documentCRPath: 'copying benchmark XSS',
};

export const sampleWithNewData: NewAssemble = {
  codeAssociation: 'Account redundant generate',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
