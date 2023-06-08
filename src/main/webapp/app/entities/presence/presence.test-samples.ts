import { StatutPresence } from 'app/entities/enumerations/statut-presence.model';

import { IPresence, NewPresence } from './presence.model';

export const sampleWithRequiredData: IPresence = {
  id: 75632,
  matriculeAdherant: 'SAS',
};

export const sampleWithPartialData: IPresence = {
  id: 4380,
  matriculeAdherant: 'bypassing b Handmade',
};

export const sampleWithFullData: IPresence = {
  id: 29315,
  matriculeAdherant: 'system invoice maroon',
  statutPresence: StatutPresence['PRESENT'],
};

export const sampleWithNewData: NewPresence = {
  matriculeAdherant: 'c',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
