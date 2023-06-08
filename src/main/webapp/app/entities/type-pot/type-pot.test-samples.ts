import { ITypePot, NewTypePot } from './type-pot.model';

export const sampleWithRequiredData: ITypePot = {
  id: 67742,
  libele: 'redundant c Soap',
};

export const sampleWithPartialData: ITypePot = {
  id: 26416,
  libele: 'Bretagne',
  descrption: 'Sausages solutions',
};

export const sampleWithFullData: ITypePot = {
  id: 14548,
  libele: 'c mobile',
  descrption: 'orchid Card',
};

export const sampleWithNewData: NewTypePot = {
  libele: 'relationships Berkshire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
