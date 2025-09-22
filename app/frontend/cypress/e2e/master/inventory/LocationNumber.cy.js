describe('棚番管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z');
    })

    const warehousePage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(4) > :nth-child(1) > :nth-child(9) > :nth-child(1) > #side-nav-warehouse-nav').click();
    }
    const locationNumberPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(4) > :nth-child(1) > :nth-child(9) > :nth-child(2) > #side-nav-locationnumber-nav').click();
    }

    context('棚番一覧', () => {
        it('棚番一覧の表示', () => {
            locationNumberPage();
            cy.get('.collection-view-container').should('be.visible');
        });
    });

    context('棚番新規登録', () => {
        it('新規登録', () => {
            warehousePage();
            cy.get('#new').click();
            cy.get('#warehouseCode').type('W90');
            cy.get('#warehouseName').type('テスト倉庫');
            cy.get('#save').click();

            locationNumberPage();
            cy.get('#new').click();
            cy.get('#warehouseCode').type('W90');
            cy.get('#locationNumberCode').type('L99');
            cy.get('#productCode').type('123');
            cy.get('#save').click();
            cy.get('#message').contains('棚番を作成しました。');
        });
    });

    context('棚番検索', () => {
        it('検索', () => {
            locationNumberPage();
            cy.get('#search').click();
            cy.get('#search-location-number-warehouse-code').type('W90');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.get('.collection-object-item-content-name').contains('W90');
        });
    });

    context('棚番登録情報編集', () => {
        it('登録情報編集', () => {
            locationNumberPage();
            cy.get('#search').click();
            cy.get('#search-location-number-warehouse-code').type('W90');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#edit').click();
            cy.get('#save').click();
            cy.get('#message').contains('棚番を更新しました。');
        });
    });

    context('棚番削除', () => {
        it('削除', () => {
            warehousePage();
            cy.get('#search').click();
            cy.get('#search-warehouse-code').type('W90');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#delete').click();

            locationNumberPage();
            cy.get('#search').click();
            cy.get('#search-location-number-warehouse-code').type('W90');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#delete').click();
            cy.get('#message').contains('棚番を削除しました。');
        });
    });
});