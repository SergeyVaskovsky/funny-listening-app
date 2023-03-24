import { IStory } from 'app/shared/model/story.model';

export interface IElement {
  id?: number;
  elementText?: string;
  elementAudioPath?: string | null;
  entireStory?: IStory | null;
  story?: IStory | null;
}

export const defaultValue: Readonly<IElement> = {};
