import dayjs from 'dayjs/esm';
import { ITontine } from 'app/entities/tontine/tontine.model';

export interface IGestionnaireTontine {
  id: number;
  matriculeAdherent?: string | null;
  codeTontine?: string | null;
  datePriseFonction?: dayjs.Dayjs | null;
  dateFinFonction?: dayjs.Dayjs | null;
  tontine?: Pick<ITontine, 'id'> | null;
}

export type NewGestionnaireTontine = Omit<IGestionnaireTontine, 'id'> & { id: null };
