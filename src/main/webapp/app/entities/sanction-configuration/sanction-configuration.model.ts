import { TypeSanction } from 'app/entities/enumerations/type-sanction.model';

export interface ISanctionConfiguration {
  id: number;
  codeAssociation?: string | null;
  codeTontine?: string | null;
  type?: TypeSanction | null;
}

export type NewSanctionConfiguration = Omit<ISanctionConfiguration, 'id'> & { id: null };
