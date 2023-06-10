import dayjs from 'dayjs/esm';
import { TypePenalite } from 'app/entities/enumerations/type-penalite.model';
import { StatutTontine } from 'app/entities/enumerations/statut-tontine.model';

export interface ITontine {
  id: number;
  codeAssociation?: string | null;
  libele?: string | null;
  nombreTour?: number | null;
  nombrePersonne?: number | null;
  margeBeneficiaire?: number | null;
  montantPart?: number | null;
  montantCagnote?: number | null;
  penaliteRetardCotisation?: number | null;
  typePenalite?: TypePenalite | null;
  dateCreation?: dayjs.Dayjs | null;
  datePremierTour?: dayjs.Dayjs | null;
  dateDernierTour?: dayjs.Dayjs | null;
  statutTontine?: StatutTontine | null;
  description?: string | null;
}

export type NewTontine = Omit<ITontine, 'id'> & { id: null };
