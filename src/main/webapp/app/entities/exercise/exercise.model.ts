import dayjs from 'dayjs/esm';
import { IAssociation } from 'app/entities/association/association.model';
import { StatutExercice } from 'app/entities/enumerations/statut-exercice.model';

export interface IExercise {
  id: number;
  libele?: string | null;
  observation?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  statut?: StatutExercice | null;
  association?: Pick<IAssociation, 'id'> | null;
}

export type NewExercise = Omit<IExercise, 'id'> & { id: null };
