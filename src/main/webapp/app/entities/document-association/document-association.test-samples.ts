import dayjs from 'dayjs/esm';

import { IDocumentAssociation, NewDocumentAssociation } from './document-association.model';

export const sampleWithRequiredData: IDocumentAssociation = {
  id: 76672,
};

export const sampleWithPartialData: IDocumentAssociation = {
  id: 76228,
  codeDocument: 'c Cotton',
  version: 'Cambridgeshire CSS',
};

export const sampleWithFullData: IDocumentAssociation = {
  id: 77458,
  codeDocument: 'c Soft tertiary',
  libele: 'Card',
  description: 'c enhance',
  dateEnregistrement: dayjs('2023-06-07'),
  dateArchivage: dayjs('2023-06-08'),
  version: 'payment',
};

export const sampleWithNewData: NewDocumentAssociation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
