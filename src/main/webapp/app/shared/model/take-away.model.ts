import { Moment } from 'moment';
import { IInnkeeper } from 'app/shared/model/innkeeper.model';

export interface ITakeAway {
  id?: number;
  createDate?: Moment;
  updateDate?: Moment;
  dish?: string;
  description?: string;
  ingredients?: string;
  allergens?: string;
  latitude?: string;
  longitude?: string;
  address?: string;
  costmin?: number;
  costmax?: number;
  tags?: string;
  innkeeper?: IInnkeeper;
}

export class TakeAway implements ITakeAway {
  constructor(
    public id?: number,
    public createDate?: Moment,
    public updateDate?: Moment,
    public dish?: string,
    public description?: string,
    public ingredients?: string,
    public allergens?: string,
    public latitude?: string,
    public longitude?: string,
    public address?: string,
    public costmin?: number,
    public costmax?: number,
    public tags?: string,
    public innkeeper?: IInnkeeper
  ) {}
}
