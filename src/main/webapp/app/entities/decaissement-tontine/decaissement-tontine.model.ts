import dayjs from 'dayjs/esm';
import { ISessionTontine } from 'app/entities/session-tontine/session-tontine.model';
import { ICompteTontine } from 'app/entities/compte-tontine/compte-tontine.model';

export interface IDecaissementTontine {
  id: number;
  libele?: string | null;
  dateDecaissement?: dayjs.Dayjs | null;
  montantDecaisse?: number | null;
  commentaire?: string | null;
  tontine?: Pick<ISessionTontine, 'id'> | null;
  compteTontine?: Pick<ICompteTontine, 'id'> | null;
}

export type NewDecaissementTontine = Omit<IDecaissementTontine, 'id'> & { id: null };
