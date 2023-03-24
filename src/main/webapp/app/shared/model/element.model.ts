import { IStory } from 'app/shared/model/story.model';

export interface IElement {
  id?: number;
  elementText?: string;
  elementAudioPath?: string;
  entireStoryElement?: IStory | null;
  element?: IStory | null;
}

export const defaultValue: Readonly<IElement> = {};
