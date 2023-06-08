import { IPaiementBanque, NewPaiementBanque } from './paiement-banque.model';

export const sampleWithRequiredData: IPaiementBanque = {
  id: 13957,
  referencePaiement: 'Mouse Cloned deploy',
};

export const sampleWithPartialData: IPaiementBanque = {
  id: 90317,
  referencePaiement: 'real-time c',
};

export const sampleWithFullData: IPaiementBanque = {
  id: 61954,
  referencePaiement: 'mission-critical Designer Tunisian',
};

export const sampleWithNewData: NewPaiementBanque = {
  referencePaiement: '1080p Loan',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
