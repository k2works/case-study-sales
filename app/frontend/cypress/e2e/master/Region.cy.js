describe('地域管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z'); // ログインの共通処理を追加
    });
    const navigateToRegionPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(1) > :nth-child(8) > .nav-sub-item > #side-nav-code-nav').click();
    };

    context('地域一覧', () => {
        it('地域一覧の表示', () => {
            navigateToRegionPage();
            cy.get('.collection-view-container').should('be.visible');
        });
    });

    context('地域新規登録', () => {
        it('新規登録', () => {
            navigateToRegionPage();
            cy.get('#new').click();
            cy.get('#regionCode').type('R999');
            cy.get('#regionName').type('テスト地域');
            cy.get('#save').click();
            cy.get('#message').contains('地域を作成しました。');
        });
    });

    context('地域検索', () => {
        it('検索', () => {
            navigateToRegionPage();
            cy.get('#search').click();
            cy.get('#search-region-code').type('R999');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.get(':nth-child(3) > .collection-object-item-content-name').contains('テスト地域');
        });
    });

    context('地域登録情報編集', () => {
        it('登録情報編集', () => {
            navigateToRegionPage();
            cy.get('#search').click();
            cy.get('#search-region-code').type('R999');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#edit').click();
            cy.get('#regionName').clear();
            cy.get('#regionName').type('テスト地域更新');
            cy.get('#save').click();
            cy.get('#message').contains('地域を更新しました。');
        });
    });

    context('地域削除', () => {
        it('削除', () => {
            navigateToRegionPage();
            cy.get('#search').click();
            cy.get('#search-region-code').type('R999');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#delete').click();
            cy.get('#message').contains('地域を削除しました。');
        });
    });
});