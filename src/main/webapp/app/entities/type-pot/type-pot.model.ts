export interface ITypePot {
  id: number;
  libele?: string | null;
  descrption?: string | null;
}

export type NewTypePot = Omit<ITypePot, 'id'> & { id: null };
