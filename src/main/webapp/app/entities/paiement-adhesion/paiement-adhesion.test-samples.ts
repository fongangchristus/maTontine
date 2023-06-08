import { IPaiementAdhesion, NewPaiementAdhesion } from './paiement-adhesion.model';

export const sampleWithRequiredData: IPaiementAdhesion = {
  id: 2514,
  referencePaiement: 'Bonaparte',
};

export const sampleWithPartialData: IPaiementAdhesion = {
  id: 25955,
  referencePaiement: 'microchip maroon la',
};

export const sampleWithFullData: IPaiementAdhesion = {
  id: 81452,
  referencePaiement: 'Pants compressing Rustic',
};

export const sampleWithNewData: NewPaiementAdhesion = {
  referencePaiement: 'driver Saint-Séverin best-of-breed',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
