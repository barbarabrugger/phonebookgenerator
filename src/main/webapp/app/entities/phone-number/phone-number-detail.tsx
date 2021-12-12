import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './phone-number.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PhoneNumberDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const phoneNumberEntity = useAppSelector(state => state.phoneNumber.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="phoneNumberDetailsHeading">
          <Translate contentKey="phonebookgeneratorApp.phoneNumber.detail.title">PhoneNumber</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{phoneNumberEntity.id}</dd>
          <dt>
            <span id="number">
              <Translate contentKey="phonebookgeneratorApp.phoneNumber.number">Number</Translate>
            </span>
          </dt>
          <dd>{phoneNumberEntity.number}</dd>
          <dt>
            <Translate contentKey="phonebookgeneratorApp.phoneNumber.phonebookEntry">Phonebook Entry</Translate>
          </dt>
          <dd>{phoneNumberEntity.phonebookEntry ? phoneNumberEntity.phonebookEntry.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/phone-number" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/phone-number/${phoneNumberEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PhoneNumberDetail;
