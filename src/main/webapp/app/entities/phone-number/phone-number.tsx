import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './phone-number.reducer';
import { IPhoneNumber } from 'app/shared/model/phone-number.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PhoneNumber = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const phoneNumberList = useAppSelector(state => state.phoneNumber.entities);
  const loading = useAppSelector(state => state.phoneNumber.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="phone-number-heading" data-cy="PhoneNumberHeading">
        <Translate contentKey="phonebookgeneratorApp.phoneNumber.home.title">Phone Numbers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="phonebookgeneratorApp.phoneNumber.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="phonebookgeneratorApp.phoneNumber.home.createLabel">Create new Phone Number</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {phoneNumberList && phoneNumberList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="phonebookgeneratorApp.phoneNumber.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="phonebookgeneratorApp.phoneNumber.number">Number</Translate>
                </th>
                <th>
                  <Translate contentKey="phonebookgeneratorApp.phoneNumber.phonebookEntry">Phonebook Entry</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {phoneNumberList.map((phoneNumber, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${phoneNumber.id}`} color="link" size="sm">
                      {phoneNumber.id}
                    </Button>
                  </td>
                  <td>{phoneNumber.number}</td>
                  <td>
                    {phoneNumber.phonebookEntry ? (
                      <Link to={`phonebook-entry/${phoneNumber.phonebookEntry.id}`}>{phoneNumber.phonebookEntry.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${phoneNumber.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${phoneNumber.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${phoneNumber.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="phonebookgeneratorApp.phoneNumber.home.notFound">No Phone Numbers found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PhoneNumber;
