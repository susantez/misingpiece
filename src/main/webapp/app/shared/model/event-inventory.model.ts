import { Moment } from 'moment';
import { IProduct } from 'app/shared/model//product.model';
import { IEvent } from 'app/shared/model//event.model';

export interface IEventInventory {
  id?: number;
  itemCount?: number;
  creationDate?: Moment;
  product?: IProduct;
  event?: IEvent;
}

export const defaultValue: Readonly<IEventInventory> = {};
