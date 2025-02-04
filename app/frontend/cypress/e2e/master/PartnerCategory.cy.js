describe('取引先分類管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z'); // 必要に応じてログイン処理を実装
    });

    const openPartnerCategoryPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(1) > :nth-child(6) > :nth-child(1) > #side-nav-partner-nav').click();
    };

    context('取引先分類一覧', () => {
        it('取引先分類一覧の表示', () => {
            openPartnerCategoryPage();
            cy.get('.collection-view-container').should('be.visible');
        });
    });

    context('取引先分類新規登録', () => {
        it('新規登録', () => {
            openPartnerCategoryPage();
            cy.get('#new').click();
            cy.get('#partnerCategoryTypeCode').type('99');
            cy.get('#partnerCategoryTypeName').type('テスト取引先分類');
            cy.get('#save').click();
            cy.get('#message').contains('取引先分類を作成しました。');
        });
    });

    context('取引先分類検索', () => {
        it('検索', () => {
            openPartnerCategoryPage();
            cy.get('#search').click();
            cy.get('#search-partner-category-type-code').type('99')
            cy.get('#search-all').click();
            cy.get(':nth-child(3) > .collection-object-item-content-name').contains('テスト取引先分類');
        });
    });

    context('取引先分類登録情報編集', () => {
        it('登録情報編集', () => {
            openPartnerCategoryPage();
            cy.get('#search').click();
            cy.get('#search-partner-category-type-code').type('99')
            cy.get('#search-all').click();
            cy.get('#edit').click();
            cy.get('#partnerCategoryTypeName').clear();
            cy.get('#partnerCategoryTypeName').type('テスト取引先分類更新');
            cy.get('#add').click();
            cy.get(':nth-child(4) > #add').click();
            cy.get('#add-partner').click();
            cy.get(':nth-child(1) > .collection-object-item-actions > #select-partner').click();
            cy.get('.close-modal-button').click();
            cy.get('#save').click();
            cy.get('#message').contains('取引先分類を更新しました。');
        });
    });

    context('取引先分類削除', () => {
        it('削除', () => {
            openPartnerCategoryPage();
            cy.get('#search').click();
            cy.get('#search-partner-category-type-code').type('99')
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.get('#delete').click();
            cy.get('#message').contains('取引先分類を削除しました。');
        });
    });
});