import { Moment } from 'moment';
import { IProduct } from 'app/shared/model//product.model';
import { IEvent } from 'app/shared/model//event.model';

export const enum SalesType {
  ONLINE = 'ONLINE',
  EVENT = 'EVENT'
}

export const enum Status {
  ACTIVE = 'ACTIVE',
  HISTORY = 'HISTORY'
}

export interface ISales {
  id?: number;
  type?: SalesType;
  unitPrice?: number;
  itemCount?: number;
  totalPrice?: number;
  creationDate?: Moment;
  updateDate?: Moment;
  status?: Status;
  product?: IProduct;
  events?: IEvent[];
}

export const defaultValue: Readonly<ISales> = {};
