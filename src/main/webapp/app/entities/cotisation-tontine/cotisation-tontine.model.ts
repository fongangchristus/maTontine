import dayjs from 'dayjs/esm';
import { ISessionTontine } from 'app/entities/session-tontine/session-tontine.model';
import { ICompteTontine } from 'app/entities/compte-tontine/compte-tontine.model';
import { EtatCotisation } from 'app/entities/enumerations/etat-cotisation.model';

export interface ICotisationTontine {
  id: number;
  montantCotise?: number | null;
  pieceJustifPath?: string | null;
  dateCotisation?: dayjs.Dayjs | null;
  dateValidation?: dayjs.Dayjs | null;
  commentaire?: string | null;
  etat?: EtatCotisation | null;
  sessionTontine?: Pick<ISessionTontine, 'id'> | null;
  compteTontine?: Pick<ICompteTontine, 'id'> | null;
}

export type NewCotisationTontine = Omit<ICotisationTontine, 'id'> & { id: null };
