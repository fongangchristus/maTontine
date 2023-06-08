import dayjs from 'dayjs/esm';
import { StatutTontine } from 'app/entities/enumerations/statut-tontine.model';

export interface ITontine {
  id: number;
  codeAssociation?: string | null;
  libele?: string | null;
  nombreTour?: number | null;
  nombreMaxPersonne?: number | null;
  margeBeneficiaire?: number | null;
  montantPart?: number | null;
  amandeEchec?: number | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  statutTontine?: StatutTontine | null;
  description?: string | null;
}

export type NewTontine = Omit<ITontine, 'id'> & { id: null };
