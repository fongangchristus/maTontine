import dayjs from 'dayjs/esm';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { TypePersonne } from 'app/entities/enumerations/type-personne.model';

export interface IPersonne {
  id: number;
  idUser?: number | null;
  codeAssociation?: number | null;
  matricule?: string | null;
  nom?: string | null;
  prenom?: string | null;
  dateNaissance?: dayjs.Dayjs | null;
  lieuNaissance?: number | null;
  dateInscription?: dayjs.Dayjs | null;
  profession?: string | null;
  sexe?: Sexe | null;
  photoPath?: string | null;
  dateIntegration?: dayjs.Dayjs | null;
  isAdmin?: boolean | null;
  isDonateur?: boolean | null;
  isBenevole?: boolean | null;
  typePersonne?: TypePersonne | null;
  adresse?: Pick<IAdresse, 'id'> | null;
}

export type NewPersonne = Omit<IPersonne, 'id'> & { id: null };
