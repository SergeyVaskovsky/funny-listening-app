import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './story.reducer';

export const StoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const storyEntity = useAppSelector(state => state.story.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="storyDetailsHeading">Story</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{storyEntity.id}</dd>
          <dt>
            <span id="storyName">Story Name</span>
          </dt>
          <dd>{storyEntity.storyName}</dd>
          <dt>Elements</dt>
          <dd>
            {storyEntity.elements
              ? storyEntity.elements.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {storyEntity.elements && i === storyEntity.elements.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/story" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/story/${storyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default StoryDetail;
