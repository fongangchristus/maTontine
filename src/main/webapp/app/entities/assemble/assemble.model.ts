import { NatureAssemble } from 'app/entities/enumerations/nature-assemble.model';

export interface IAssemble {
  id: number;
  codeAssociation?: string | null;
  libele?: string | null;
  enLigne?: boolean | null;
  dateSeance?: string | null;
  lieuSeance?: string | null;
  matriculeMembreRecoit?: string | null;
  nature?: NatureAssemble | null;
  compteRendu?: string | null;
  resumeAssemble?: string | null;
  documentCRPath?: string | null;
}

export type NewAssemble = Omit<IAssemble, 'id'> & { id: null };
