// Since 1.4.0 there is no fixed default password: set the admin password of the
// test environment via E2E_ADMIN_PASSWORD (and E2E_ADMIN_USER if not "admin").
export const E2E_ADMIN_USER = process.env.E2E_ADMIN_USER || 'admin';
export const E2E_ADMIN_PASSWORD = process.env.E2E_ADMIN_PASSWORD || '';
