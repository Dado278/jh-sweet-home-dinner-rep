import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { ISharedDinner } from 'app/entities/shared-dinner/shared-dinner.model';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface ICustomer {
  id?: number;
  nickname?: string;
  avatarImageBlobContentType?: string | null;
  avatarImageBlob?: string | null;
  avatarTextBlob?: string | null;
  freshman?: number | null;
  email?: string | null;
  phoneNumber?: string;
  gender?: Gender | null;
  createDate?: dayjs.Dayjs | null;
  updateDate?: dayjs.Dayjs | null;
  internalUser?: IUser | null;
  sharedDinners?: ISharedDinner[] | null;
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public nickname?: string,
    public avatarImageBlobContentType?: string | null,
    public avatarImageBlob?: string | null,
    public avatarTextBlob?: string | null,
    public freshman?: number | null,
    public email?: string | null,
    public phoneNumber?: string,
    public gender?: Gender | null,
    public createDate?: dayjs.Dayjs | null,
    public updateDate?: dayjs.Dayjs | null,
    public internalUser?: IUser | null,
    public sharedDinners?: ISharedDinner[] | null
  ) {}
}

export function getCustomerIdentifier(customer: ICustomer): number | undefined {
  return customer.id;
}
