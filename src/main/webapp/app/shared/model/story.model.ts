import { IElement } from 'app/shared/model/element.model';

export interface IStory {
  id?: number;
  storyName?: string;
  entireStoryElement?: IElement | null;
  elements?: IElement[] | null;
}

export const defaultValue: Readonly<IStory> = {};
