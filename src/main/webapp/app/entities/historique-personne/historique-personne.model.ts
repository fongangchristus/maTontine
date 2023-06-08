import dayjs from 'dayjs/esm';
import { IPersonne } from 'app/entities/personne/personne.model';

export interface IHistoriquePersonne {
  id: number;
  dateAction?: dayjs.Dayjs | null;
  matriculePersonne?: string | null;
  action?: string | null;
  result?: string | null;
  description?: string | null;
  personne?: Pick<IPersonne, 'id'> | null;
}

export type NewHistoriquePersonne = Omit<IHistoriquePersonne, 'id'> & { id: null };
