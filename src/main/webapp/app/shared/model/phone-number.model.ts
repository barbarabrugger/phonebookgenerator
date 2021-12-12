import { IPhonebookEntry } from 'app/shared/model/phonebook-entry.model';

export interface IPhoneNumber {
  id?: number;
  number?: string;
  phonebookEntry?: IPhonebookEntry | null;
}

export const defaultValue: Readonly<IPhoneNumber> = {};
