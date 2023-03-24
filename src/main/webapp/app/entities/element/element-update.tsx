import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStory } from 'app/shared/model/story.model';
import { getEntities as getStories } from 'app/entities/story/story.reducer';
import { IElement } from 'app/shared/model/element.model';
import { getEntity, updateEntity, createEntity, reset } from './element.reducer';

export const ElementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const stories = useAppSelector(state => state.story.entities);
  const elementEntity = useAppSelector(state => state.element.entity);
  const loading = useAppSelector(state => state.element.loading);
  const updating = useAppSelector(state => state.element.updating);
  const updateSuccess = useAppSelector(state => state.element.updateSuccess);

  const handleClose = () => {
    navigate('/element');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getStories({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...elementEntity,
      ...values,
      story: stories.find(it => it.id.toString() === values.story.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...elementEntity,
          story: elementEntity?.story?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="funnyListeningApp.element.home.createOrEditLabel" data-cy="ElementCreateUpdateHeading">
            Create or edit a Element
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="element-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Element Text"
                id="element-elementText"
                name="elementText"
                data-cy="elementText"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Element Audio Path"
                id="element-elementAudioPath"
                name="elementAudioPath"
                data-cy="elementAudioPath"
                type="text"
              />
              <ValidatedField id="element-story" name="story" data-cy="story" label="Story" type="select">
                <option value="" key="0" />
                {stories
                  ? stories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/element" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ElementUpdate;
