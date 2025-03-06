describe('受注管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z');

        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(1) > .nav-sub-list > :nth-child(2) > #side-nav-product-nav').click();
        // アップロードボタンの確認
        cy.get('#upload').should('be.visible');

        // アップロードモーダルを開く
        cy.get('#upload').click();
        cy.get('.modal').should('be.visible');

        // ファイルアップロード
        cy.uploadFile('input[type="file"]', 'sales_order_multiple.csv', 'text/csv');

        // アップロード実行
        cy.get('.modal button').contains('アップロード').click();
    })

    const userPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(1) > .nav-sub-list > :nth-child(3) > #side-nav-product-nav').click();
    }

    context('受注ルール', () => {
        it('実行', () => {
            userPage();

            // アップロードボタンの確認
            cy.get('#execute').should('be.visible');

            // ルールチェック実行
            cy.get('#execute').click();
            cy.get('p').contains('受注ルールチェックを実行しました(確認項目があります)');
        });
    });
});
