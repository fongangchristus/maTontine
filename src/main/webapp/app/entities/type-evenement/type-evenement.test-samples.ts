import { ITypeEvenement, NewTypeEvenement } from './type-evenement.model';

export const sampleWithRequiredData: ITypeEvenement = {
  id: 35478,
};

export const sampleWithPartialData: ITypeEvenement = {
  id: 36614,
  libele: 'ability',
  observation: 'SCSI Account',
};

export const sampleWithFullData: ITypeEvenement = {
  id: 39998,
  libele: 'Sleek c leading-edge',
  observation: 'synergize solutions',
};

export const sampleWithNewData: NewTypeEvenement = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
