import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './phonebook-entry.reducer';
import { IPhonebookEntry } from 'app/shared/model/phonebook-entry.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PhonebookEntry = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const phonebookEntryList = useAppSelector(state => state.phonebookEntry.entities);
  const loading = useAppSelector(state => state.phonebookEntry.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="phonebook-entry-heading" data-cy="PhonebookEntryHeading">
        <Translate contentKey="phonebookgeneratorApp.phonebookEntry.home.title">Phonebook Entries</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="phonebookgeneratorApp.phonebookEntry.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="phonebookgeneratorApp.phonebookEntry.home.createLabel">Create new Phonebook Entry</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {phonebookEntryList && phonebookEntryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="phonebookgeneratorApp.phonebookEntry.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="phonebookgeneratorApp.phonebookEntry.description">Description</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {phonebookEntryList.map((phonebookEntry, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${phonebookEntry.id}`} color="link" size="sm">
                      {phonebookEntry.id}
                    </Button>
                  </td>
                  <td>{phonebookEntry.description}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${phonebookEntry.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${phonebookEntry.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${phonebookEntry.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="phonebookgeneratorApp.phonebookEntry.home.notFound">No Phonebook Entries found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PhonebookEntry;
