import element from 'app/entities/element/element.reducer';
import story from 'app/entities/story/story.reducer';
import link from 'app/entities/link/link.reducer';
import referalLink from 'app/entities/referal-link/referal-link.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  element,
  story,
  link,
  referalLink,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
