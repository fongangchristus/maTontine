import dayjs from 'dayjs/esm';
import { ISanctionConfiguration } from 'app/entities/sanction-configuration/sanction-configuration.model';

export interface ISanction {
  id: number;
  libelle?: string | null;
  matriculeAdherent?: string | null;
  dateSanction?: dayjs.Dayjs | null;
  motifSanction?: string | null;
  description?: string | null;
  codeActivite?: string | null;
  sanctionConfig?: Pick<ISanctionConfiguration, 'id'> | null;
}

export type NewSanction = Omit<ISanction, 'id'> & { id: null };
