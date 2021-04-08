import * as dayjs from 'dayjs';
import { IInnkeeper } from 'app/entities/innkeeper/innkeeper.model';

export interface ITakeAway {
  id?: number;
  createDate?: dayjs.Dayjs | null;
  updateDate?: dayjs.Dayjs | null;
  dish?: string;
  description?: string;
  ingredients?: string;
  allergens?: string;
  latitude?: string | null;
  longitude?: string | null;
  address?: string;
  costmin?: number;
  costmax?: number;
  tags?: string | null;
  innkeeper?: IInnkeeper | null;
}

export class TakeAway implements ITakeAway {
  constructor(
    public id?: number,
    public createDate?: dayjs.Dayjs | null,
    public updateDate?: dayjs.Dayjs | null,
    public dish?: string,
    public description?: string,
    public ingredients?: string,
    public allergens?: string,
    public latitude?: string | null,
    public longitude?: string | null,
    public address?: string,
    public costmin?: number,
    public costmax?: number,
    public tags?: string | null,
    public innkeeper?: IInnkeeper | null
  ) {}
}

export function getTakeAwayIdentifier(takeAway: ITakeAway): number | undefined {
  return takeAway.id;
}
