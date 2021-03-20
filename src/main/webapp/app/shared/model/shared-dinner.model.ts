import { Moment } from 'moment';
import { IInfoInnkeeper } from 'app/shared/model/info-innkeeper.model';

export interface ISharedDinner {
  id?: number;
  createDate?: Moment;
  updateDate?: Moment;
  title?: string;
  slogan?: string;
  description?: string;
  homePage?: string;
  latitude?: string;
  longitude?: string;
  address?: string;
  costmin?: number;
  costmax?: number;
  infoInnkeeper?: IInfoInnkeeper;
}

export class SharedDinner implements ISharedDinner {
  constructor(
    public id?: number,
    public createDate?: Moment,
    public updateDate?: Moment,
    public title?: string,
    public slogan?: string,
    public description?: string,
    public homePage?: string,
    public latitude?: string,
    public longitude?: string,
    public address?: string,
    public costmin?: number,
    public costmax?: number,
    public infoInnkeeper?: IInfoInnkeeper
  ) {}
}
