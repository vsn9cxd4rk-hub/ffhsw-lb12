import client from './client';
import { Vehicle, VehicleInspection, LogbookEntry, EquipmentInspection } from '../types';

export const vehiclesApi = {
  getAll: (params?: { isRetired?: boolean }) => client.get<{ data: Vehicle[] }>('/vehicles', { params }),
  getById: (id: number) => client.get<{ data: Vehicle }>(`/vehicles/${id}`),
  create: (data: Partial<Vehicle>) => client.post<{ data: Vehicle }>('/vehicles', data),
  update: (id: number, data: Partial<Vehicle>) => client.put<{ data: Vehicle }>(`/vehicles/${id}`, data),
  delete: (id: number) => client.delete(`/vehicles/${id}`),

  updateInspection: (id: number, data: Partial<VehicleInspection>) =>
    client.put(`/vehicles/${id}/inspection`, data),

  getLogbook: (id: number, page?: number) =>
    client.get(`/vehicles/${id}/logbook`, { params: { page } }),
  createLogbookEntry: (id: number, data: Partial<LogbookEntry>) =>
    client.post(`/vehicles/${id}/logbook`, data),
  updateLogbookEntry: (vehicleId: number, entryId: number, data: Partial<LogbookEntry>) =>
    client.put(`/vehicles/${vehicleId}/logbook/${entryId}`, data),

  getEquipmentInspections: (id: number) =>
    client.get<{ data: EquipmentInspection[] }>(`/vehicles/${id}/equipment-inspections`),
  createEquipmentInspection: (id: number, data: Partial<EquipmentInspection>) =>
    client.post(`/vehicles/${id}/equipment-inspections`, data),
  updateEquipmentInspection: (vehicleId: number, inspId: number, data: Partial<EquipmentInspection>) =>
    client.put(`/vehicles/${vehicleId}/equipment-inspections/${inspId}`, data),
};
