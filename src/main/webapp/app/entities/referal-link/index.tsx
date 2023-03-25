import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ReferalLink from './referal-link';
import ReferalLinkDetail from './referal-link-detail';
import ReferalLinkUpdate from './referal-link-update';
import ReferalLinkDeleteDialog from './referal-link-delete-dialog';

const ReferalLinkRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ReferalLink />} />
    <Route path="new" element={<ReferalLinkUpdate />} />
    <Route path=":id">
      <Route index element={<ReferalLinkDetail />} />
      <Route path="edit" element={<ReferalLinkUpdate />} />
      <Route path="delete" element={<ReferalLinkDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReferalLinkRoutes;
