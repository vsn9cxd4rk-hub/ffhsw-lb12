import { defineConfig } from 'vitest/config';

export default defineConfig({
  test: {
    globals: true,
    root: '.',
    include: ['tests/**/*.test.ts'],
    setupFiles: ['tests/setup.ts'],
    testTimeout: 15000,
    hookTimeout: 15000,
    sequence: { concurrent: false },
    // Tests sign their own tokens; a fixed secret keeps them independent of the local .env
    env: { JWT_ACCESS_SECRET: 'vitest-only-access-secret-0123456789abcdef' },
  },
});
