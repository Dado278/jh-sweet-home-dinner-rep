import { ISharedDinner } from 'app/shared/model/shared-dinner.model';

export interface IInfoInnkeeper {
  id?: number;
  nickname?: string;
  slogan?: string;
  description?: string;
  homePage?: string;
  latitude?: string;
  longitude?: string;
  address?: string;
  services?: string;
  sharedDinners?: ISharedDinner[];
}

export class InfoInnkeeper implements IInfoInnkeeper {
  constructor(
    public id?: number,
    public nickname?: string,
    public slogan?: string,
    public description?: string,
    public homePage?: string,
    public latitude?: string,
    public longitude?: string,
    public address?: string,
    public services?: string,
    public sharedDinners?: ISharedDinner[]
  ) {}
}
