import dayjs from 'dayjs/esm';

import { ICotisationBanque, NewCotisationBanque } from './cotisation-banque.model';

export const sampleWithRequiredData: ICotisationBanque = {
  id: 86651,
};

export const sampleWithPartialData: ICotisationBanque = {
  id: 5211,
  commentaire: 'architect',
};

export const sampleWithFullData: ICotisationBanque = {
  id: 7450,
  libelle: 'Small',
  montant: 69034,
  dateCotisation: dayjs('2023-06-07T16:56'),
  montantCotise: 33638,
  commentaire: 'c',
};

export const sampleWithNewData: NewCotisationBanque = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
