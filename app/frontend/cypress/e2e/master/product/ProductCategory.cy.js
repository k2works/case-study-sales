describe('商品分類管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z');
    })
    const openProductCategoryPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(4) > :nth-child(1) > .nav-sub-list > :nth-child(1) > #side-nav-product-nav').click();
    }
    context('商品分類一覧', () => {
        it('商品分類一覧の表示', () => {
            openProductCategoryPage();
            cy.get('.collection-view-container').should('be.visible');
        });
    });
    context('商品分類新規登録', () => {
        it('新規登録', () => {
            openProductCategoryPage();
            cy.get('#new').click();
            cy.get('#productCategoryCode').type('99999');
            cy.get('#productCategoryName').type('テストカテゴリ');
            cy.get('#productCategoryHierarchy').type('1');
            cy.get('#productCategoryPath').type('99999~');
            cy.get('#lowestLevelDivision').type('0');
            cy.get('#save').click();
            cy.get('#message').contains('商品分類を作成しました。');
        });
    });
    context('商品分類検索', () => {
        it('検索', () => {
            openProductCategoryPage();
            cy.get('#search').click();
            cy.get('#productCategoryCode').type('99999');
            cy.get('#search-all').click();
            cy.get(':nth-child(3) > .collection-object-item-content-name').contains('テストカテゴリ');
        });
    })
    context('商品分類登録情報編集', () => {
        it('登録情報編集', () => {
            openProductCategoryPage();
            cy.get('#search').click();
            cy.get('#productCategoryCode').type('99999');
            cy.get('#search-all').click();
            cy.get('#edit').click();
            cy.get('#productCategoryName').clear();
            cy.get('#productCategoryName').type('テストカテゴリ更新');
            cy.get('#productCategoryHierarchy').clear();
            cy.get('#productCategoryHierarchy').type('2');
            cy.get('#productCategoryPath').type('99999~99999~');
            cy.get('#lowestLevelDivision').clear();
            cy.get('#lowestLevelDivision').type('1');
            cy.get('#save').click();
            cy.get('#message').contains('商品分類を更新しました。');
        });
    });
    context('商品分類削除', () => {
        it('削除', () => {
            openProductCategoryPage();
            cy.get('#search').click();
            cy.get('#productCategoryCode').type('99999');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.get('#delete').click();
            cy.get('#message').contains('商品分類を削除しました。');
        })
    });
});