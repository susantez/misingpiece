import { Moment } from 'moment';
import { ICategory } from 'app/shared/model//category.model';

export const enum Status {
  ACTIVE = 'ACTIVE',
  HISTORY = 'HISTORY'
}

export interface IProduct {
  id?: number;
  name?: string;
  description?: string;
  targetPrice?: number;
  creationDate?: Moment;
  updateDate?: Moment;
  status?: Status;
  category?: ICategory;
}

export const defaultValue: Readonly<IProduct> = {};
