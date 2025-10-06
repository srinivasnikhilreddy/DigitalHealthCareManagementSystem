import { defineConfig } from 'vite';

export default defineConfig({
  server: {
    host: true, // allow connections from outside localhost
    port: 4200, // make sure it matches your dev server port
    allowedHosts: 'all'//['tumultuous-proaction-libbie.ngrok-free.dev'] // add your ngrok URL here or 'all'
  }
});
