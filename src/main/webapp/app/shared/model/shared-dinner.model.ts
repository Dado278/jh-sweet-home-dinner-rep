import { Moment } from 'moment';
import { IInnkeeper } from 'app/shared/model/innkeeper.model';
import { ICustomer } from 'app/shared/model/customer.model';

export interface ISharedDinner {
  id?: number;
  createDate?: Moment;
  updateDate?: Moment;
  title?: string;
  slogan?: string;
  description?: string;
  dinnerDate?: Moment;
  homePage?: string;
  latitude?: string;
  longitude?: string;
  address?: string;
  costmin?: number;
  costmax?: number;
  innkeeper?: IInnkeeper;
  customers?: ICustomer[];
}

export class SharedDinner implements ISharedDinner {
  constructor(
    public id?: number,
    public createDate?: Moment,
    public updateDate?: Moment,
    public title?: string,
    public slogan?: string,
    public description?: string,
    public dinnerDate?: Moment,
    public homePage?: string,
    public latitude?: string,
    public longitude?: string,
    public address?: string,
    public costmin?: number,
    public costmax?: number,
    public innkeeper?: IInnkeeper,
    public customers?: ICustomer[]
  ) {}
}
