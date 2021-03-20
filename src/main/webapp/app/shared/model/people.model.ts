import { Moment } from 'moment';
import { IInfoInnkeeper } from 'app/shared/model/info-innkeeper.model';
import { PersonType } from 'app/shared/model/enumerations/person-type.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';

export interface IPeople {
  id?: number;
  username?: string;
  password?: string;
  freshman?: number;
  personType?: PersonType;
  email?: string;
  phoneNumber?: string;
  gender?: Gender;
  token?: string;
  createDate?: Moment;
  updateDate?: Moment;
  infoInnkeeper?: IInfoInnkeeper;
}

export class People implements IPeople {
  constructor(
    public id?: number,
    public username?: string,
    public password?: string,
    public freshman?: number,
    public personType?: PersonType,
    public email?: string,
    public phoneNumber?: string,
    public gender?: Gender,
    public token?: string,
    public createDate?: Moment,
    public updateDate?: Moment,
    public infoInnkeeper?: IInfoInnkeeper
  ) {}
}
