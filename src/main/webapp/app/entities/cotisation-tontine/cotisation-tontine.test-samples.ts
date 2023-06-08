import dayjs from 'dayjs/esm';

import { EtatCotisation } from 'app/entities/enumerations/etat-cotisation.model';

import { ICotisationTontine, NewCotisationTontine } from './cotisation-tontine.model';

export const sampleWithRequiredData: ICotisationTontine = {
  id: 42172,
};

export const sampleWithPartialData: ICotisationTontine = {
  id: 4950,
  montantCotise: 50999,
  dateCotisation: dayjs('2023-06-08'),
  dateValidation: dayjs('2023-06-07'),
  commentaire: 'invoice e-markets',
};

export const sampleWithFullData: ICotisationTontine = {
  id: 91083,
  montantCotise: 16729,
  pieceJustifPath: 'matrix Towels Honduras',
  dateCotisation: dayjs('2023-06-08'),
  dateValidation: dayjs('2023-06-07'),
  commentaire: 'cohesive program applications',
  etat: EtatCotisation['ENCOURS'],
};

export const sampleWithNewData: NewCotisationTontine = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
