const { allureCypress } = require("allure-cypress/reporter");

module.exports = {
  e2e: {
    setupNodeEvents(on, config) {
      allureCypress(on, config, {
        resultsDir: "allure-results",
      });
      return config;
    },
  },
  viewportWidth: 1920,
  viewportHeight: 1080,
};