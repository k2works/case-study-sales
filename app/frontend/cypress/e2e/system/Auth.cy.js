describe('認証', () => {
    context('ユーザー認証', () => {
        it('一般ユーザー認証', () => {
            cy.loginWithSession('U000001', 'a234567Z');
        });

        it('管理ユーザー認証', () => {
            cy.loginWithSession('U000003', 'a234567Z');
        });
    });
});