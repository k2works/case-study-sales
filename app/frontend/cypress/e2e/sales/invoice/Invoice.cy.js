describe('請求管理', () => {
    context('管理者', () => {
        beforeEach(() => {
            cy.login('U000003', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(1) > :nth-child(8) > :nth-child(1) > #side-nav-invoice-nav').click();
        }

        context('請求一覧', () => {
            it('請求一覧の表示', () => {
                userPage();
                cy.get('.collection-view-container').should('be.visible');
            });
        });


        context('請求検索', () => {
            it('検索', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-invoice-number').type('IV00000001');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get(':nth-child(2) > .collection-object-item-content-name').contains('IV00000001');
            });
        });

        context('請求登録情報編集', () => {
            it('登録情報編集', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-invoice-number').type('IV00000001');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#edit').click();

                // 金額を更新
                cy.get('#currentMonthInvoiceAmount').clear();
                cy.get('#currentMonthInvoiceAmount').type('9000');
                cy.get('#consumptionTaxAmount').clear();
                cy.get('#consumptionTaxAmount').type('900');

                // Update invoice line
                cy.get('.sales-line-row').eq(0).within(() => {
                    // 売上番号を更新
                    cy.get('input[type="text"]').eq(0).clear();
                    cy.get('input[type="text"]').eq(0).type('SA00000002');
                });

                cy.get('#save').click();
                cy.get('#message').contains('請求を更新しました。');
            });
        });
    });

    context('利用者', () => {
        beforeEach(() => {
            cy.login('U000001', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-item > :nth-child(1) > :nth-child(8) > :nth-child(1) > #side-nav-invoice-nav').click();
        }

        context('請求一覧', () => {
            it('請求一覧の表示', () => {
                userPage();
                cy.get('.collection-view-container').should('be.visible');
            });
        });

        context('請求検索', () => {
            it('検索', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-invoice-number').type('IV00000001');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get(':nth-child(2) > .collection-object-item-content-name').contains('IV00000001');
            });
        });

        context('請求登録情報編集', () => {
            it('登録情報編集', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-invoice-number').type('IV00000001');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#edit').click();

                // 金額を更新
                cy.get('#currentMonthInvoiceAmount').clear();
                cy.get('#currentMonthInvoiceAmount').type('9000');
                cy.get('#consumptionTaxAmount').clear();
                cy.get('#consumptionTaxAmount').type('900');

                // Update invoice line
                cy.get('.sales-line-row').eq(0).within(() => {
                    // 売上番号を更新
                    cy.get('input[type="text"]').eq(0).clear();
                    cy.get('input[type="text"]').eq(0).type('SA00000002');
                });

                cy.get('#save').click();
                cy.get('#message').contains('請求を更新しました。');
            });
        });
    });
});