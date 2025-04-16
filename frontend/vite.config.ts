import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { fileURLToPath } from 'url';
import { dirname, resolve } from 'path';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@components': resolve(__dirname, 'src/components'),
      '@pages': resolve(__dirname, 'src/pages'),
      '@context': resolve(__dirname, 'src/context'),
      '@service': resolve(__dirname, 'src/service'),
      '@hook': resolve(__dirname, 'src/hook'),
      '@util': resolve(__dirname, 'src/util'),
    },
  },
  server: {
    port: 5173,
  },
});
