import * as dayjs from 'dayjs';
import { IInnkeeper } from 'app/entities/innkeeper/innkeeper.model';

export interface ISharedDinner {
  id?: number;
  createDate?: dayjs.Dayjs | null;
  updateDate?: dayjs.Dayjs | null;
  title?: string;
  slogan?: string | null;
  description?: string;
  homePage?: string | null;
  latitude?: string | null;
  longitude?: string | null;
  address?: string;
  costmin?: number;
  costmax?: number;
  innkeeper?: IInnkeeper | null;
}

export class SharedDinner implements ISharedDinner {
  constructor(
    public id?: number,
    public createDate?: dayjs.Dayjs | null,
    public updateDate?: dayjs.Dayjs | null,
    public title?: string,
    public slogan?: string | null,
    public description?: string,
    public homePage?: string | null,
    public latitude?: string | null,
    public longitude?: string | null,
    public address?: string,
    public costmin?: number,
    public costmax?: number,
    public innkeeper?: IInnkeeper | null
  ) {}
}

export function getSharedDinnerIdentifier(sharedDinner: ISharedDinner): number | undefined {
  return sharedDinner.id;
}
