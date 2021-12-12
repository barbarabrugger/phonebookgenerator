import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './phonebook-entry.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PhonebookEntryDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const phonebookEntryEntity = useAppSelector(state => state.phonebookEntry.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="phonebookEntryDetailsHeading">
          <Translate contentKey="phonebookgeneratorApp.phonebookEntry.detail.title">PhonebookEntry</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{phonebookEntryEntity.id}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="phonebookgeneratorApp.phonebookEntry.description">Description</Translate>
            </span>
          </dt>
          <dd>{phonebookEntryEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/phonebook-entry" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/phonebook-entry/${phonebookEntryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PhonebookEntryDetail;
