import dayjs from 'dayjs/esm';
import { IAssociation } from 'app/entities/association/association.model';

export interface IDocumentAssociation {
  id: number;
  codeDocument?: string | null;
  libele?: string | null;
  description?: string | null;
  dateEnregistrement?: dayjs.Dayjs | null;
  dateArchivage?: dayjs.Dayjs | null;
  version?: string | null;
  association?: Pick<IAssociation, 'id'> | null;
}

export type NewDocumentAssociation = Omit<IDocumentAssociation, 'id'> & { id: null };
