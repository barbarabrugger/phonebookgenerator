import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PhonebookEntry from './phonebook-entry';
import PhonebookEntryDetail from './phonebook-entry-detail';
import PhonebookEntryUpdate from './phonebook-entry-update';
import PhonebookEntryDeleteDialog from './phonebook-entry-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PhonebookEntryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PhonebookEntryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PhonebookEntryDetail} />
      <ErrorBoundaryRoute path={match.url} component={PhonebookEntry} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PhonebookEntryDeleteDialog} />
  </>
);

export default Routes;
