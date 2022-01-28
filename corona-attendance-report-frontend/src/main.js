import Vue from 'vue';
import Keycloak from 'keycloak-js';
import axios from 'axios';
import App from './App.vue';

Vue.config.productionTip = false;

axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('vue-token');
    // eslint-disable-next-line no-param-reassign
    config.headers.Authorization = `Bearer ${token}`;
    return config;
  },
);
Vue.prototype.$axios = axios;

const initOptions = {
  url: 'http://localhost:8088/auth/',
  realm: 'corona-attendance-report',
  clientId: 'corona-attendance-report-frontend',
  onLoad: 'login-required',
};

const keycloak = Keycloak(initOptions);

keycloak.init({ onLoad: initOptions.onLoad })
  .then((auth) => {
    if (!auth) {
      window.location.reload();
    } else {
      console.log('Authenticated');
    }

    new Vue({
      render: (h) => h(App),
    }).$mount('#app');

    // Move to VUEX
    localStorage.setItem('vue-token', keycloak.token);

    setInterval(() => {
      keycloak.updateToken(70)
        .then((refreshed) => {
          if (refreshed) {
            console.log(`Token refreshed${refreshed}`);
          } else {
            console.log(`Token not refreshed, valid for ${
              Math.round(keycloak.tokenParsed.exp + keycloak.timeSkew - new Date().getTime() / 1000)} seconds`);
          }
        }).catch(() => {
          console.error('Failed to refresh token');
        });
    }, 60000);
  }).catch(() => {
    console.error('Authenticated Failed');
  });
