// =============================================================================
// PM2 Ecosystem-Konfiguration für das Feuerwehr Management System
// Verwendung: pm2 start ecosystem.config.js
// =============================================================================

module.exports = {
  apps: [
    {
      name: 'feuerwehr-backend',
      script: '/var/www/feuerwehrmanagement/backend/dist/server.js',
      cwd: '/var/www/feuerwehrmanagement/backend',

      // Prozess-Einstellungen
      instances: 1,
      exec_mode: 'fork',
      autorestart: true,
      watch: false,
      max_memory_restart: '500M',

      // Umgebung
      env: {
        NODE_ENV: 'production',
        PORT: 3001,
      },

      // Log-Konfiguration
      error_file: '/var/log/feuerwehrmanagement/pm2-error.log',
      out_file: '/var/log/feuerwehrmanagement/pm2-out.log',
      log_date_format: 'YYYY-MM-DD HH:mm:ss',
      merge_logs: true,

      // Neustart-Konfiguration
      min_uptime: '10s',
      max_restarts: 10,
      restart_delay: 5000,

      // Graceful shutdown
      kill_timeout: 5000,
      wait_ready: true,
      listen_timeout: 10000,
    },
  ],
};
