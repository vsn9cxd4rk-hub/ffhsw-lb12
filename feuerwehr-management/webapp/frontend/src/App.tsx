import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Layout } from './components/layout/Layout';
import { LoginPage } from './pages/auth/LoginPage';
import { DashboardPage } from './pages/dashboard/DashboardPage';
import { MembersPage } from './pages/members/MembersPage';
import { MemberDetailPage } from './pages/members/MemberDetailPage';
import { MemberFormPage } from './pages/members/MemberFormPage';
import { VehiclesPage } from './pages/vehicles/VehiclesPage';
import { VehicleDetailPage } from './pages/vehicles/VehicleDetailPage';
import { InventoryPage } from './pages/inventory/InventoryPage';
import { InspectionBookPage } from './pages/inspections/InspectionBookPage';
import { OperationsPage } from './pages/operations/OperationsPage';
import { OperationDetailPage } from './pages/operations/OperationDetailPage';
import { EventsPage } from './pages/events/EventsPage';
import { EventDetailPage } from './pages/events/EventDetailPage';
import { AttendancePage } from './pages/events/AttendancePage';
import { TrainingPage } from './pages/training/TrainingPage';
import { SettingsPage } from './pages/settings/SettingsPage';
import { ChangePasswordPage } from './pages/auth/ChangePasswordPage';
import { UsersPage } from './pages/users/UsersPage';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public routes */}
        <Route path="/login" element={<LoginPage />} />

        {/* Protected routes */}
        <Route element={<Layout />}>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="/dashboard" element={<DashboardPage />} />

          {/* Members */}
          <Route path="/members" element={<MembersPage />} />
          <Route path="/members/new" element={<MemberFormPage />} />
          <Route path="/members/:id" element={<MemberDetailPage />} />

          {/* Vehicles */}
          <Route path="/vehicles" element={<VehiclesPage />} />
          <Route path="/vehicles/:id" element={<VehicleDetailPage />} />

          {/* Inventory */}
          <Route path="/inventory" element={<InventoryPage />} />

          {/* Inspection Book */}
          <Route path="/inspections" element={<InspectionBookPage />} />

          {/* Operations */}
          <Route path="/operations" element={<OperationsPage />} />
          <Route path="/operations/:id" element={<OperationDetailPage />} />

          {/* Events */}
          <Route path="/events" element={<EventsPage />} />
          <Route path="/events/:id" element={<EventDetailPage />} />
          <Route path="/events/:id/attendance" element={<AttendancePage />} />

          {/* Training */}
          <Route path="/training" element={<TrainingPage />} />

          {/* Settings */}
          <Route path="/settings" element={<SettingsPage />} />

          {/* Password change - accessible for all authenticated users */}
          <Route path="/password" element={<ChangePasswordPage />} />

          {/* Admin */}
          <Route path="/admin/users" element={<UsersPage />} />
        </Route>

        {/* Catch all */}
        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
