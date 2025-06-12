describe('出荷確認管理', () => {
    context('管理者', () => {
        beforeEach(() => {
            cy.login('U000003', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(1) > :nth-child(4) > :nth-child(4) > #side-nav-shipping-nav').click();
        }

        context('出荷確認一覧', () => {
            it('出荷確認一覧の表示', () => {
                userPage();
                cy.get('.collection-view-container').should('be.visible');
                cy.get('.single-view-title').contains('出荷確認');
            });
        });

        context('出荷確認実行', () => {
            it('出荷確認実行', () => {
                userPage();

                // 出荷確認ボタンをクリック
                cy.get('#confirmShipping').click();

                // 出荷確認完了メッセージの確認
                cy.get('#message').contains('出荷を確認しました。');
            });
        });

        context('出荷確認検索', () => {
            it('検索', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-order-number').type('OD00000001');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get('.collection-object-item-content-name').contains('OD00000001');
            });
        });

        context('出荷確認情報編集', () => {
            it('情報編集', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-order-number').type('OD00000001');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);

                // 編集ボタンクリック
                cy.get('#edit').click();

                // 出荷済数量を変更する
                cy.get('#shippedQuantity').type('{selectall}3');

                // 保存ボタンクリック
                cy.get('#save').click();

                // 更新完了メッセージの確認
                cy.get('#message').contains('出荷を更新しました。');
            });
        });
    });

    context('利用者', () => {
        beforeEach(() => {
            cy.login('U000001', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-item > :nth-child(1) > :nth-child(4) > :nth-child(4) > #side-nav-shipping-nav').click();
        }

        context('出荷確認一覧', () => {
            it('出荷確認一覧の表示', () => {
                userPage();
                cy.get('.collection-view-container').should('be.visible');
                cy.get('.single-view-title').contains('出荷確認');
            });
        });

        context('出荷確認実行', () => {
            it('出荷確認実行', () => {
                userPage();

                // 出荷確認ボタンをクリック
                cy.get('#confirmShipping').click();

                // 出荷確認完了メッセージの確認
                cy.get('#message').contains('出荷を確認しました。');
            });
        });

        context('出荷確認検索', () => {
            it('検索', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-order-number').type('OD00000001');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get('.collection-object-item-content-name').contains('OD00000001');
            });
        });

        context('出荷確認情報編集', () => {
            it('情報編集', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-order-number').type('OD00000001');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);

                // 編集ボタンクリック
                cy.get('#edit').click();

                // 出荷済数量を変更する
                cy.get('#shippedQuantity').type('{selectall}3');

                // 保存ボタンクリック
                cy.get('#save').click();

                // 更新完了メッセージの確認
                cy.get('#message').contains('出荷を更新しました。');
            });
        });
    });
});