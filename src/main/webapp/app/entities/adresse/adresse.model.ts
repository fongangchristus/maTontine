export interface IAdresse {
  id: number;
  departmentName?: string | null;
  streetAddress?: string | null;
  postalCode?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  pays?: string | null;
}

export type NewAdresse = Omit<IAdresse, 'id'> & { id: null };
