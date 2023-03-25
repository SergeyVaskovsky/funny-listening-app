import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Link from './link';
import LinkDetail from './link-detail';
import LinkUpdate from './link-update';
import LinkDeleteDialog from './link-delete-dialog';

const LinkRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Link />} />
    <Route path="new" element={<LinkUpdate />} />
    <Route path=":id">
      <Route index element={<LinkDetail />} />
      <Route path="edit" element={<LinkUpdate />} />
      <Route path="delete" element={<LinkDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LinkRoutes;
