import dayjs from 'dayjs/esm';

import { ICommentairePot, NewCommentairePot } from './commentaire-pot.model';

export const sampleWithRequiredData: ICommentairePot = {
  id: 75680,
  matriculeContributeur: 'deliverables',
  identifiantPot: 'RAM Music',
};

export const sampleWithPartialData: ICommentairePot = {
  id: 97331,
  matriculeContributeur: 'Metal Bike',
  identifiantPot: 'Guarani Home',
  dateComentaire: dayjs('2023-06-07T16:27'),
};

export const sampleWithFullData: ICommentairePot = {
  id: 27699,
  matriculeContributeur: 'Antigua-et-Barbuda Incredible',
  identifiantPot: 'sensor Île-de-France',
  contenu: 'Metal Investment',
  dateComentaire: dayjs('2023-06-08T03:26'),
};

export const sampleWithNewData: NewCommentairePot = {
  matriculeContributeur: "clear-thinking d'Azur Multi-lateral",
  identifiantPot: 'connecting Object-based',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
