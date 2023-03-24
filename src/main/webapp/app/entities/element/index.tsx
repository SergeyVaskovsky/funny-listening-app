import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Element from './element';
import ElementDetail from './element-detail';
import ElementUpdate from './element-update';
import ElementDeleteDialog from './element-delete-dialog';

const ElementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Element />} />
    <Route path="new" element={<ElementUpdate />} />
    <Route path=":id">
      <Route index element={<ElementDetail />} />
      <Route path="edit" element={<ElementUpdate />} />
      <Route path="delete" element={<ElementDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ElementRoutes;
