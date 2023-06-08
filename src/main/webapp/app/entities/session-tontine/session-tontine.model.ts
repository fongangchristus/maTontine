import dayjs from 'dayjs/esm';
import { ITontine } from 'app/entities/tontine/tontine.model';

export interface ISessionTontine {
  id: number;
  libelle?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  tontine?: Pick<ITontine, 'id'> | null;
}

export type NewSessionTontine = Omit<ISessionTontine, 'id'> & { id: null };
