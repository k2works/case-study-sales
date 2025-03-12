import 'cypress-file-upload';

// Custom command for login
Cypress.Commands.add('login', (username, password) => {
    cy.visit('/');
    cy.get('#username').type(username);
    cy.get('#password').type(password);
    cy.get('#login').click();
});

// Custom command for file upload
Cypress.Commands.add('uploadFile', (selector, fileName, fileType = '') => {
    cy.get(selector).attachFile({
        filePath: fileName,
        mimeType: fileType
    });
});
