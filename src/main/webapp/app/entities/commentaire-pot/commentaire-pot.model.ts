import dayjs from 'dayjs/esm';
import { IPot } from 'app/entities/pot/pot.model';

export interface ICommentairePot {
  id: number;
  matriculeContributeur?: string | null;
  identifiantPot?: string | null;
  contenu?: string | null;
  dateComentaire?: dayjs.Dayjs | null;
  pot?: Pick<IPot, 'id'> | null;
}

export type NewCommentairePot = Omit<ICommentairePot, 'id'> & { id: null };
