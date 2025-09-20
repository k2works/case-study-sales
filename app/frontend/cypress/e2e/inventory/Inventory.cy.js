describe('在庫管理', () => {
    context('管理者', () => {
        beforeEach(() => {
            cy.login('U000003', 'a234567Z');
        })

        const inventoryPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(3) > .nav-sub-list > :nth-child(1) > #side-nav-inventory-nav').click();
        }

        context('在庫一覧', () => {
            it('在庫一覧の表示', () => {
                inventoryPage();
                cy.get('.collection-view-container').should('be.visible');
            });
        });

        context('在庫新規登録', () => {
            it('新規登録', () => {
                inventoryPage();

                // 在庫新規画面を開く
                cy.get('#new').click();

                // 倉庫情報を入力
                cy.get('#warehouseCode').click();
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-warehouse').click();

                // 商品情報を入力
                cy.get('#productCode').click()
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-product').click();

                // ロット番号を入力
                cy.get('#lotNumber').type('LOT001');

                // 在庫区分を選択
                cy.get('#stockCategory').select('通常在庫');

                // 良品区分を選択
                cy.get('#qualityCategory').select('良品');

                // 在庫数量を入力
                cy.get('#actualStockQuantity').type('100');
                cy.get('#availableStockQuantity').type('95');

                // 最終出荷日を入力
                cy.get('#lastShipmentDate').type('2024-01-15');

                // 在庫を保存
                cy.get('#save').click();

                // 作成完了メッセージの確認
                cy.get('#message').contains('在庫データを登録しました。');
            });
        });

        context('在庫検索', () => {
            it('検索', () => {
                inventoryPage();
                cy.get('#search').click();
                cy.get('#search-warehouse-code').click();
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-warehouse').click();
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get('.collection-object-item-content-name').contains('WH1');
            });

            it('在庫区分での検索', () => {
                inventoryPage();
                cy.get('#search').click();
                cy.get('#search-stock-category').select('通常在庫');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get('.collection-view-container').should('be.visible');
            });

            it('良品区分での検索', () => {
                inventoryPage();
                cy.get('#search').click();
                cy.get('#search-quality-category').select('良品');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get('.collection-view-container').should('be.visible');
            });
        });

        context('在庫登録情報編集', () => {
            it('登録情報編集', () => {
                inventoryPage();
                cy.get('#search').click();
                cy.get('#search-warehouse-code').click();
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-warehouse').click();
                cy.get('#search-product-code').click();
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-product').click();
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#edit').click();

                // 在庫数量を更新
                cy.get('#actualStockQuantity').clear().type('120');
                cy.get('#availableStockQuantity').clear().type('115');

                // 最終出荷日を更新
                cy.get('#lastShipmentDate').clear().type('2024-01-20');

                cy.get('#save').click();
                cy.get('#message').contains('在庫データを更新しました。');
            });
        });

        context('在庫削除', () => {
            it('削除', () => {
                inventoryPage();
                cy.get('#search').click();
                cy.get('#search-warehouse-code').click();
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-warehouse').click();
                cy.get('#search-product-code').click();
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-product').click();
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#delete').click();
                cy.get('#message').contains('在庫データを削除しました。');
            });
        });
    });

    context('利用者', () => {
        beforeEach(() => {
            cy.login('U000001', 'a234567Z');
        })

        const inventoryPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-item > :nth-child(3) > .nav-sub-list > :nth-child(1) > #side-nav-inventory-nav').click();
        }

        context('在庫一覧', () => {
            it('在庫一覧の表示', () => {
                inventoryPage();
                cy.get('.collection-view-container').should('be.visible');
            });
        });

        context('在庫新規登録', () => {
            it('新規登録', () => {
                inventoryPage();

                // 在庫新規画面を開く
                cy.get('#new').click();

                // 倉庫情報を入力
                cy.get('#warehouseCode').click();
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-warehouse').click();

                // 商品情報を入力
                cy.get('#productCode').click()
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-product').click();

                // ロット番号を入力
                cy.get('#lotNumber').type('LOT002');

                // 在庫区分を選択
                cy.get('#stockCategory').select('安全在庫');

                // 良品区分を選択
                cy.get('#qualityCategory').select('良品');

                // 在庫数量を入力
                cy.get('#actualStockQuantity').type('50');
                cy.get('#availableStockQuantity').type('45');

                // 最終出荷日を入力
                cy.get('#lastShipmentDate').type('2024-01-10');

                // 在庫を保存
                cy.get('#save').click();

                // 作成完了メッセージの確認
                cy.get('#message').contains('在庫データを登録しました。');
            });
        });

        context('在庫検索', () => {
            it('検索', () => {
                inventoryPage();
                cy.get('#search').click();
                cy.get('#search-warehouse-code').click();
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-warehouse').click();
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get('.collection-object-item-content-name').contains('WH1');
            });
        });

        context('在庫登録情報編集', () => {
            it('登録情報編集', () => {
                inventoryPage();
                cy.get('#search').click();
                cy.get('#search-warehouse-code').click();
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-warehouse').click();
                cy.get('#search-product-code').click();
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-product').click();
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#edit').click();

                // 在庫数量を更新
                cy.get('#actualStockQuantity').clear().type('60');
                cy.get('#availableStockQuantity').clear().type('55');

                cy.get('#save').click();
                cy.get('#message').contains('在庫データを更新しました。');
            });
        });

        context('在庫削除', () => {
            it('削除', () => {
                inventoryPage();
                cy.get('#search').click();
                cy.get('#search-warehouse-code').click();
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-warehouse').click();
                cy.get('#search-product-code').click();
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-product').click();
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#delete').click();
                cy.get('#message').contains('在庫データを削除しました。');
            });
        });
    });
});