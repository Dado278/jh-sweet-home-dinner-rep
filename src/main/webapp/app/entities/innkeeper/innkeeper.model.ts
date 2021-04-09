import * as dayjs from 'dayjs';
import { ISharedDinner } from 'app/entities/shared-dinner/shared-dinner.model';
import { ITakeAway } from 'app/entities/take-away/take-away.model';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface IInnkeeper {
  id?: number;
  nickname?: string;
  avatarImageBlobContentType?: string | null;
  avatarImageBlob?: string | null;
  avatarTextBlob?: string | null;
  freshman?: number | null;
  email?: string;
  phoneNumber?: string;
  gender?: Gender | null;
  slogan?: string | null;
  description?: string;
  homePage?: string | null;
  latitude?: string | null;
  longitude?: string | null;
  address?: string;
  services?: string | null;
  createDate?: dayjs.Dayjs | null;
  updateDate?: dayjs.Dayjs | null;
  sharedDinners?: ISharedDinner[] | null;
  takeAways?: ITakeAway[] | null;
}

export class Innkeeper implements IInnkeeper {
  constructor(
    public id?: number,
    public nickname?: string,
    public avatarImageBlobContentType?: string | null,
    public avatarImageBlob?: string | null,
    public avatarTextBlob?: string | null,
    public freshman?: number | null,
    public email?: string,
    public phoneNumber?: string,
    public gender?: Gender | null,
    public slogan?: string | null,
    public description?: string,
    public homePage?: string | null,
    public latitude?: string | null,
    public longitude?: string | null,
    public address?: string,
    public services?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public updateDate?: dayjs.Dayjs | null,
    public sharedDinners?: ISharedDinner[] | null,
    public takeAways?: ITakeAway[] | null
  ) {}
}

export function getInnkeeperIdentifier(innkeeper: IInnkeeper): number | undefined {
  return innkeeper.id;
}
