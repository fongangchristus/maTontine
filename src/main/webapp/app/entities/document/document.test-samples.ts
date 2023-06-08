import { IDocument, NewDocument } from './document.model';

export const sampleWithRequiredData: IDocument = {
  id: 70614,
};

export const sampleWithPartialData: IDocument = {
  id: 50710,
  libelle: 'Soft Auto',
  docKey: 'Loan',
  path: 'a',
  typeDocument: 'connect',
};

export const sampleWithFullData: IDocument = {
  id: 71174,
  libelle: 'Aquitaine',
  docKey: 'Object-based',
  path: 'Stagiaire',
  typeDocument: 'de drive',
};

export const sampleWithNewData: NewDocument = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
