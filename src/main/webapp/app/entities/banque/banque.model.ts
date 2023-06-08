import dayjs from 'dayjs/esm';

export interface IBanque {
  id: number;
  codeAssociation?: string | null;
  libelle?: string | null;
  description?: string | null;
  dateOuverture?: dayjs.Dayjs | null;
  dateCloture?: dayjs.Dayjs | null;
}

export type NewBanque = Omit<IBanque, 'id'> & { id: null };
