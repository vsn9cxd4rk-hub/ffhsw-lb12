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
import { ArticleDetailPage } from './pages/inventory/ArticleDetailPage';
import { InspectionBookPage } from './pages/inspections/InspectionBookPage';
import { DefectsPage } from './pages/defects/DefectsPage';
import { OperationsPage } from './pages/operations/OperationsPage';
import { OperationDetailPage } from './pages/operations/OperationDetailPage';
import { EventsPage } from './pages/events/EventsPage';
import { EventDetailPage } from './pages/events/EventDetailPage';
import { AttendancePage } from './pages/events/AttendancePage';
import { TrainingPage } from './pages/training/TrainingPage';
import { SettingsPage } from './pages/settings/SettingsPage';
import { ChangePasswordPage } from './pages/auth/ChangePasswordPage';
import { HelpPage } from './pages/help/HelpPage';
import { UsersPage } from './pages/users/UsersPage';
import { ScanPage } from './pages/scan/ScanPage';
import { StatisticsPage } from './pages/statistics/StatisticsPage';
import { ThirdPartyLicensesPage } from './pages/about/ThirdPartyLicensesPage';
import { RequireGroup } from './components/layout/RequireGroup';
import { RequirePermission } from './components/layout/RequirePermission';
import { BIT_VEHICLES, BIT_OPERATIONS, BIT_EQUIPMENT } from './config/permissionBits';

const GROUP_ADMIN = 1;

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public routes */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/scan/:id" element={<ScanPage />} />

        {/* Protected routes */}
        <Route element={<Layout />}>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="/dashboard" element={<DashboardPage />} />

          {/* Members - Admin only. Gruppenführer get member names via the Operations personnel picker (GET /members), not this page. */}
          <Route path="/members" element={<RequireGroup allowedGroups={[GROUP_ADMIN]}><MembersPage /></RequireGroup>} />
          <Route path="/members/new" element={<RequireGroup allowedGroups={[GROUP_ADMIN]}><MemberFormPage /></RequireGroup>} />
          <Route path="/members/:id" element={<RequireGroup allowedGroups={[GROUP_ADMIN]}><MemberDetailPage /></RequireGroup>} />

          {/* Vehicles - capability: Fahrzeuge */}
          <Route path="/vehicles" element={<RequirePermission bit={BIT_VEHICLES}><VehiclesPage /></RequirePermission>} />
          <Route path="/vehicles/:id" element={<RequirePermission bit={BIT_VEHICLES}><VehicleDetailPage /></RequirePermission>} />

          {/* Inventory - capability: Gerätewart-Bereich */}
          <Route path="/inventory" element={<RequirePermission bit={BIT_EQUIPMENT}><InventoryPage /></RequirePermission>} />
          <Route path="/inventory/:id" element={<RequirePermission bit={BIT_EQUIPMENT}><ArticleDetailPage /></RequirePermission>} />

          {/* Inspection Book - capability: Gerätewart-Bereich */}
          <Route path="/inspections" element={<RequirePermission bit={BIT_EQUIPMENT}><InspectionBookPage /></RequirePermission>} />

          {/* Defects & Repairs - capability: Gerätewart-Bereich */}
          <Route path="/defects" element={<RequirePermission bit={BIT_EQUIPMENT}><DefectsPage /></RequirePermission>} />

          {/* Operations - capability: Einsätze */}
          <Route path="/operations" element={<RequirePermission bit={BIT_OPERATIONS}><OperationsPage /></RequirePermission>} />
          <Route path="/operations/:id" element={<RequirePermission bit={BIT_OPERATIONS}><OperationDetailPage /></RequirePermission>} />

          {/* Statistics - capability: Einsätze */}
          <Route path="/statistics" element={<RequirePermission bit={BIT_OPERATIONS}><StatisticsPage /></RequirePermission>} />

          {/* Events */}
          <Route path="/events" element={<EventsPage />} />
          <Route path="/events/:id" element={<EventDetailPage />} />
          <Route path="/events/:id/attendance" element={<AttendancePage />} />

          {/* Training */}
          <Route path="/training" element={<TrainingPage />} />

          {/* Settings */}
          <Route path="/settings" element={<SettingsPage />} />

          {/* Help */}
          <Route path="/help" element={<HelpPage />} />

          {/* Password change - accessible for all authenticated users */}
          <Route path="/password" element={<ChangePasswordPage />} />

          {/* Admin */}
          <Route path="/admin/users" element={<UsersPage />} />

          {/* Third-party licenses */}
          <Route path="/third-party-licenses" element={<ThirdPartyLicensesPage />} />
        </Route>

        {/* Catch all */}
        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
