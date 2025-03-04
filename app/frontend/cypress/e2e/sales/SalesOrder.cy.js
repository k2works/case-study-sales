describe('受注管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z');
    })

    const userPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > .nav-sub-list > .nav-sub-item > #side-nav-sales-order-nav').click();
    }

    context('受注一覧', () => {
        it('受注一覧の表示', () => {
            userPage();
            cy.get('.collection-view-container').should('be.visible');
        });
    });

    context('受注新規登録', () => {
        it('新規登録', () => {
            userPage();
            cy.get('#new').click();
            cy.get('#orderNumber').type('SO001');
            cy.get('#orderDate').type('2024-01-01');
            cy.get('#departmentCode').type('10000');
            cy.get('#customerCode').type('C0001');
            cy.get('#customerBranchNumber').type('0');
            cy.get('#employeeCode').type('E0001');
            cy.get('#desiredDeliveryDate').type('2024-01-31');
            cy.get('#customerOrderNumber').type('CO001');
            cy.get('#warehouseCode').type('WH001');
            cy.get('#remarks').type('テスト受注');

            // Add order line
            cy.get('.single-view-content-item-form-lines button').contains('明細追加').click();
            cy.get('#productCode-0').type('P0001');
            cy.get('#productName-0').type('テスト商品');
            cy.get('#quantity-0').type('2');
            cy.get('#unitPrice-0').type('1000');
            cy.get('#remarks-0').type('明細備考');

            cy.get('#save').click();
            cy.get('#message').contains('受注を作成しました。');
        });
    });

    context('受注検索', () => {
        it('検索', () => {
            userPage();
            cy.get('#search').click();
            cy.get('#search-order-number').type('SO001');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.get(':nth-child(3) > .collection-object-item-content-name').contains('SO001');
        });
    });

    context('受注登録情報編集', () => {
        it('登録情報編集', () => {
            userPage();
            cy.get('#search').click();
            cy.get('#search-order-number').type('SO001');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#edit').click();

            cy.get('#remarks').clear();
            cy.get('#remarks').type('テスト受注更新');

            // Update order line
            cy.get('#quantity-0').clear();
            cy.get('#quantity-0').type('3');

            cy.get('#save').click();
            cy.get('#message').contains('受注を更新しました。');
        });
    });

    context('受注削除', () => {
        it('削除', () => {
            userPage();
            cy.get('#search').click();
            cy.get('#search-order-number').type('SO001');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#delete').click();
            cy.get('#message').contains('受注を削除しました。');
        });
    });
});