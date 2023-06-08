import dayjs from 'dayjs/esm';

export interface IFichePresence {
  id: number;
  libelle?: string | null;
  dateJour?: dayjs.Dayjs | null;
  description?: string | null;
  codeAssemble?: string | null;
  codeEvenement?: string | null;
}

export type NewFichePresence = Omit<IFichePresence, 'id'> & { id: null };
