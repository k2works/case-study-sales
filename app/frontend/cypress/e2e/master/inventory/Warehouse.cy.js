describe('倉庫管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z');
    })

    const warehousePage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(4) > :nth-child(1) > :nth-child(9) > :nth-child(1) > #side-nav-warehouse-nav').click();
    }

    context('倉庫一覧', () => {
        it('倉庫一覧の表示', () => {
            warehousePage();
            cy.get('.collection-view-container').should('be.visible');
        });
    });

    context('倉庫新規登録', () => {
        it('新規登録', () => {
            warehousePage();
            cy.get('#new').click();
            cy.get('#warehouseCode').type('W99');
            cy.get('#warehouseName').type('テスト倉庫');
            cy.get('#save').click();
            cy.get('#message').contains('倉庫を作成しました。');
        });
    });

    context('倉庫検索', () => {
        it('検索', () => {
            warehousePage();
            cy.get('#search').click();
            cy.get('#search-warehouse-code').type('W99');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.get('.collection-object-item-content-name').contains('W99');
        });
    });

    context('倉庫登録情報編集', () => {
        it('登録情報編集', () => {
            warehousePage();
            cy.get('#search').click();
            cy.get('#search-warehouse-code').type('W99');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#edit').click();
            cy.get('#warehouseName').clear();
            cy.get('#warehouseName').type('テスト倉庫更新');
            cy.get('#save').click();
            cy.get('#message').contains('倉庫を更新しました。');
        });
    });

    context('倉庫削除', () => {
        it('削除', () => {
            warehousePage();
            cy.get('#search').click();
            cy.get('#search-warehouse-code').type('W99');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#delete').click();
            cy.get('#message').contains('倉庫を削除しました。');
        });
    });
});