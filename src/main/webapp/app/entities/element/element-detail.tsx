import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './element.reducer';

export const ElementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const elementEntity = useAppSelector(state => state.element.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="elementDetailsHeading">Element</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{elementEntity.id}</dd>
          <dt>
            <span id="elementText">Element Text</span>
          </dt>
          <dd>{elementEntity.elementText}</dd>
          <dt>
            <span id="elementAudioPath">Element Audio Path</span>
          </dt>
          <dd>{elementEntity.elementAudioPath}</dd>
          <dt>Entire Story</dt>
          <dd>{elementEntity.entireStory ? elementEntity.entireStory.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/element" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/element/${elementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ElementDetail;
