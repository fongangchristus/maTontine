export interface IDocument {
  id: number;
  libelle?: string | null;
  docKey?: string | null;
  path?: string | null;
  typeDocument?: string | null;
}

export type NewDocument = Omit<IDocument, 'id'> & { id: null };
