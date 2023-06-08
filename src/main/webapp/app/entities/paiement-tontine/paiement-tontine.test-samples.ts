import { IPaiementTontine, NewPaiementTontine } from './paiement-tontine.model';

export const sampleWithRequiredData: IPaiementTontine = {
  id: 47524,
};

export const sampleWithPartialData: IPaiementTontine = {
  id: 98221,
};

export const sampleWithFullData: IPaiementTontine = {
  id: 25474,
  referencePaiement: 'THX parsing c',
};

export const sampleWithNewData: NewPaiementTontine = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
