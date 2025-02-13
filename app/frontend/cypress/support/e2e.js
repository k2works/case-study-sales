// ***********************************************************
// This example support/e2e.js is processed and
// loaded automatically before your test files.
//
// This is a great place to put global configuration and
// behavior that modifies Cypress.
//
// You can change the location of this file or turn off
// automatically serving support files with the
// 'supportFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/configuration
// ***********************************************************

// Import commands.js using ES2015 syntax:
import './commands.js'

// Alternatively you can use CommonJS syntax:
// require('./commands')
import "allure-cypress";

Cypress.Commands.add('login', (username, password) => {
    cy.visit('http://localhost:5173/login');
    cy.get('#userId').clear();
    cy.get('#userId').type(username);
    cy.get('#password').clear();
    cy.get('#password').type(password);
    cy.get('#login').click();
});

Cypress.Commands.add('loginWithSession', (username, password) => {
    cy.session(
        username,
        () => {
            cy.visit('http://localhost:5173/login');
            cy.get('#userId').clear();
            cy.get('#userId').type(username);
            cy.get('#password').clear();
            cy.get('#password').type(password);
            cy.get('#login').click();

            cy.contains('HOME').should('be.visible');
        }
    )
});
