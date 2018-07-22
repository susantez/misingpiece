import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import EventInventory from './event-inventory';
import EventInventoryDetail from './event-inventory-detail';
import EventInventoryUpdate from './event-inventory-update';
import EventInventoryDeleteDialog from './event-inventory-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EventInventoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EventInventoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EventInventoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={EventInventory} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={EventInventoryDeleteDialog} />
  </>
);

export default Routes;
