export interface IFonction {
  id: number;
  title?: string | null;
  description?: string | null;
}

export type NewFonction = Omit<IFonction, 'id'> & { id: null };
