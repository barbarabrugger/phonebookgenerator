import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PhoneNumber from './phone-number';
import PhoneNumberDetail from './phone-number-detail';
import PhoneNumberUpdate from './phone-number-update';
import PhoneNumberDeleteDialog from './phone-number-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PhoneNumberUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PhoneNumberUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PhoneNumberDetail} />
      <ErrorBoundaryRoute path={match.url} component={PhoneNumber} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PhoneNumberDeleteDialog} />
  </>
);

export default Routes;
