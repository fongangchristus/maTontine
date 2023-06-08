import dayjs from 'dayjs/esm';
import { ModePaiement } from 'app/entities/enumerations/mode-paiement.model';
import { StatutPaiement } from 'app/entities/enumerations/statut-paiement.model';

export interface IPaiement {
  id: number;
  codeAssociation?: string | null;
  referencePaiement?: string | null;
  matriculecmptEmet?: string | null;
  matriculecmptDest?: string | null;
  montantPaiement?: number | null;
  datePaiement?: dayjs.Dayjs | null;
  modePaiement?: ModePaiement | null;
  statutPaiement?: StatutPaiement | null;
}

export type NewPaiement = Omit<IPaiement, 'id'> & { id: null };
