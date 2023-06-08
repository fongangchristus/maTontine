import { ITontine } from 'app/entities/tontine/tontine.model';

export interface ICompteTontine {
  id: number;
  etatDeCompte?: boolean | null;
  libele?: string | null;
  odreBeneficiere?: number | null;
  matriculeCompte?: string | null;
  matriculeMenbre?: string | null;
  tontine?: Pick<ITontine, 'id'> | null;
}

export type NewCompteTontine = Omit<ICompteTontine, 'id'> & { id: null };
