import dayjs from 'dayjs/esm';

import { ISanction, NewSanction } from './sanction.model';

export const sampleWithRequiredData: ISanction = {
  id: 68757,
  matriculeAdherent: 'orange disintermediate transmit',
};

export const sampleWithPartialData: ISanction = {
  id: 33777,
  matriculeAdherent: 'Fresh',
  motifSanction: 'Jamaican',
  description: 'Handmade de vortals',
};

export const sampleWithFullData: ISanction = {
  id: 10123,
  libelle: 'interactive',
  matriculeAdherent: 'Malagasy Self-enabling turquoise',
  dateSanction: dayjs('2023-06-08'),
  motifSanction: 'Chilean transmitter array',
  description: 'Bike',
  codeActivite: 'a Fish program',
};

export const sampleWithNewData: NewSanction = {
  matriculeAdherent: 'Phased',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
