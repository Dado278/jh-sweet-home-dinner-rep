import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { ISharedDinner } from 'app/shared/model/shared-dinner.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';

export interface ICustomer {
  id?: number;
  nickname?: string;
  freshman?: number;
  email?: string;
  phoneNumber?: string;
  gender?: Gender;
  createDate?: Moment;
  updateDate?: Moment;
  internalUser?: IUser;
  sharedDinners?: ISharedDinner[];
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public nickname?: string,
    public freshman?: number,
    public email?: string,
    public phoneNumber?: string,
    public gender?: Gender,
    public createDate?: Moment,
    public updateDate?: Moment,
    public internalUser?: IUser,
    public sharedDinners?: ISharedDinner[]
  ) {}
}
