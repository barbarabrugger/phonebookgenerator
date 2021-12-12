import { IPhoneNumber } from 'app/shared/model/phone-number.model';

export interface IPhonebookEntry {
  id?: number;
  description?: string;
  phoneNumbers?: IPhoneNumber[] | null;
}

export const defaultValue: Readonly<IPhonebookEntry> = {};
