import dayjs from 'dayjs/esm';

import { StatutTontine } from 'app/entities/enumerations/statut-tontine.model';

import { ITontine, NewTontine } from './tontine.model';

export const sampleWithRequiredData: ITontine = {
  id: 76527,
  codeAssociation: 'EXE Outdoors',
};

export const sampleWithPartialData: ITontine = {
  id: 47424,
  codeAssociation: 'Shirt middleware',
  nombreTour: 20278,
  amandeEchec: 8229,
  dateDebut: dayjs('2023-06-08'),
  statutTontine: StatutTontine['CLOTUREE'],
  description: 'a Plastic Riel',
};

export const sampleWithFullData: ITontine = {
  id: 14488,
  codeAssociation: 'Wooden',
  libele: 'drive Balboa Haute-Normandie',
  nombreTour: 81871,
  nombreMaxPersonne: 57622,
  margeBeneficiaire: 51023,
  montantPart: 67282,
  amandeEchec: 27664,
  dateDebut: dayjs('2023-06-08'),
  dateFin: dayjs('2023-06-07'),
  statutTontine: StatutTontine['CLOTUREE'],
  description: 'c Mille',
};

export const sampleWithNewData: NewTontine = {
  codeAssociation: 'digital',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
