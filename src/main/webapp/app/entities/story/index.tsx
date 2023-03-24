import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Story from './story';
import StoryDetail from './story-detail';
import StoryUpdate from './story-update';
import StoryDeleteDialog from './story-delete-dialog';

const StoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Story />} />
    <Route path="new" element={<StoryUpdate />} />
    <Route path=":id">
      <Route index element={<StoryDetail />} />
      <Route path="edit" element={<StoryUpdate />} />
      <Route path="delete" element={<StoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StoryRoutes;
