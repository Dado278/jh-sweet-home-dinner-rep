import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { ISharedDinner } from 'app/shared/model/shared-dinner.model';
import { ITakeAway } from 'app/shared/model/take-away.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';

export interface IInnkeeper {
  id?: number;
  nickname?: string;
  freshman?: number;
  email?: string;
  phoneNumber?: string;
  gender?: Gender;
  slogan?: string;
  description?: string;
  homePage?: string;
  latitude?: string;
  longitude?: string;
  address?: string;
  services?: string;
  createDate?: Moment;
  updateDate?: Moment;
  internalUser?: IUser;
  sharedDinners?: ISharedDinner[];
  takeAways?: ITakeAway[];
}

export class Innkeeper implements IInnkeeper {
  constructor(
    public id?: number,
    public nickname?: string,
    public freshman?: number,
    public email?: string,
    public phoneNumber?: string,
    public gender?: Gender,
    public slogan?: string,
    public description?: string,
    public homePage?: string,
    public latitude?: string,
    public longitude?: string,
    public address?: string,
    public services?: string,
    public createDate?: Moment,
    public updateDate?: Moment,
    public internalUser?: IUser,
    public sharedDinners?: ISharedDinner[],
    public takeAways?: ITakeAway[]
  ) {}
}
