import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IPhonebookEntry } from 'app/shared/model/phonebook-entry.model';
import { getEntities as getPhonebookEntries } from 'app/entities/phonebook-entry/phonebook-entry.reducer';
import { getEntity, updateEntity, createEntity, reset } from './phone-number.reducer';
import { IPhoneNumber } from 'app/shared/model/phone-number.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PhoneNumberUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const phonebookEntries = useAppSelector(state => state.phonebookEntry.entities);
  const phoneNumberEntity = useAppSelector(state => state.phoneNumber.entity);
  const loading = useAppSelector(state => state.phoneNumber.loading);
  const updating = useAppSelector(state => state.phoneNumber.updating);
  const updateSuccess = useAppSelector(state => state.phoneNumber.updateSuccess);
  const handleClose = () => {
    props.history.push('/phone-number');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPhonebookEntries({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...phoneNumberEntity,
      ...values,
      phonebookEntry: phonebookEntries.find(it => it.id.toString() === values.phonebookEntry.toString()),
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
          ...phoneNumberEntity,
          phonebookEntry: phoneNumberEntity?.phonebookEntry?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="phonebookgeneratorApp.phoneNumber.home.createOrEditLabel" data-cy="PhoneNumberCreateUpdateHeading">
            <Translate contentKey="phonebookgeneratorApp.phoneNumber.home.createOrEditLabel">Create or edit a PhoneNumber</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="phone-number-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('phonebookgeneratorApp.phoneNumber.number')}
                id="phone-number-number"
                name="number"
                data-cy="number"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="phone-number-phonebookEntry"
                name="phonebookEntry"
                data-cy="phonebookEntry"
                label={translate('phonebookgeneratorApp.phoneNumber.phonebookEntry')}
                type="select"
              >
                <option value="" key="0" />
                {phonebookEntries
                  ? phonebookEntries.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/phone-number" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PhoneNumberUpdate;
