describe('アプリケーションデータダウンロード', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z');
    })

    const userPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-sub-list > .nav-sub-item > #side-nav-download-nav').click();
    }

    it('ダウンロード画面の表示', () => {
        userPage();
        cy.get('.single-view-container').should('be.visible');
    });

    it('部門データダウンロード', () => {
        userPage();
        cy.get('#downloadTarget').select('部門');
        cy.get('#download').click();
        cy.get('#message').contains('部門 データをダウンロードしました。');
    });

    it('社員データダウンロード', () => {
        userPage();
        cy.get('#downloadTarget').select('社員');
        cy.get('#download').click();
        cy.get('#message').contains('社員 データをダウンロードしました。');
    });

    it('商品分類データダウンロード', () => {
        userPage();
        cy.get('#downloadTarget').select('商品分類');
        cy.get('#download').click();
        cy.get('#message').contains('商品分類 データをダウンロードしました。');
    });

    it('商品データダウンロード', () => {
        userPage();
        cy.get('#downloadTarget').select('商品');
        cy.get('#download').click();
        cy.get('#message').contains('商品 データをダウンロードしました。');
    });
});
