import { Moment } from 'moment';

export interface IEvent {
  id?: number;
  name?: string;
  description?: string;
  location?: string;
  startDate?: Moment;
  endDate?: Moment;
}

export const defaultValue: Readonly<IEvent> = {};
