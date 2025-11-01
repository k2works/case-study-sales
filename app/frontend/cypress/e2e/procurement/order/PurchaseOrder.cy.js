describe('発注管理', () => {
    context('管理者', () => {
        beforeEach(() => {
            cy.login('U000003', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(2) > .nav-sub-list > :nth-child(1) > #side-nav-purchase-order-nav').click();
        }

        context('発注一覧', () => {
            it('発注一覧の表示', () => {
                userPage();
                cy.get('.collection-view-container').should('be.visible');
            });
        });

        context('発注新規登録', () => {
            it('新規登録', () => {
                userPage();

                // 発注新規画面を開く
                cy.get('#new').click();

                // ヘッダ情報を入力
                cy.get('#purchaseOrderNumber').type('PO00000009');
                cy.get('#purchaseOrderDate').type('2024-01-01');
                cy.get('#salesOrderNumber').type('OD00000001');
                cy.get('#supplierCode').click();
                cy.get('.close-modal-button').click();
                cy.get('#supplierCode').type('001');
                cy.get('#purchaseManagerCode').click();
                cy.get('.close-modal-button').click();
                cy.get('#purchaseManagerCode').type('EMP001');
                cy.get('#designatedDeliveryDate').type('2024-01-31');
                cy.get('#warehouseCode').type('W01');
                cy.get('#remarks').type('テスト発注');

                // 明細行を追加
                cy.get('.add-line-button').click();

                // 明細行1のデータを入力
                cy.get('.purchase-order-line-row').eq(0).within(() => {
                    // 発注行表示番号
                    cy.get(':nth-child(2) > .table-input').clear().type('1');
                    // 受注番号
                    cy.get(':nth-child(3) > .table-input').type('OD00000001');
                    // 受注行番号
                    cy.get(':nth-child(4) > .table-input').type('1');
                    // 商品コード
                    cy.get(':nth-child(5) input[type="text"]').type('P0001');
                    // 商品名
                    cy.get(':nth-child(6) > .table-input').type('テスト商品');
                    // 発注単価
                    cy.get(':nth-child(7) > .table-input').type('800');
                    // 発注数量
                    cy.get(':nth-child(8) > .table-input').type('5');
                    // 入荷数量
                    cy.get(':nth-child(9) > .table-input').type('0');
                    // 完了フラグ (ドロップダウン選択)
                    cy.get(':nth-child(10) > .table-input').select('未完了');
                });

                // 発注を保存
                cy.get('#save').click();

                // 作成完了メッセージの確認
                cy.get('#message').contains('発注を作成しました。');
            });
        });

        context('発注検索', () => {
            it('検索', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-purchase-order-number').type('PO00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get('.collection-object-item-content-name').contains('PO00000009');
            });
        });

        context('発注登録情報編集', () => {
            it('登録情報編集', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-purchase-order-number').type('PO00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#edit').click();

                cy.get('#remarks').clear();
                cy.get('#remarks').type('テスト発注更新');

                // Update purchase order line
                cy.get('.purchase-order-line-row').eq(0).within(() => {
                    cy.get(':nth-child(8) > .table-input').clear().type('10');
                    cy.get(':nth-child(10) > .table-input').select('完了');
                });

                cy.get('#save').click();
                cy.get('#message').contains('発注を更新しました。');
            });
        });

        context('発注削除', () => {
            it('削除', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-purchase-order-number').type('PO00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#delete').click();
                cy.get('#message').contains('発注を削除しました。');
            });
        });
    });

    context('利用者', () => {
        beforeEach(() => {
            cy.login('U000001', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-item > :nth-child(2) > .nav-sub-list > :nth-child(1) > #side-nav-purchase-order-nav').click();
        }

        context('発注一覧', () => {
            it('発注一覧の表示', () => {
                userPage();
                cy.get('.collection-view-container').should('be.visible');
            });
        });

        context('発注新規登録', () => {
            it('新規登録', () => {
                userPage();

                // 発注新規画面を開く
                cy.get('#new').click();

                // ヘッダ情報を入力
                cy.get('#purchaseOrderNumber').type('PO00000009');
                cy.get('#purchaseOrderDate').type('2024-01-01');
                cy.get('#salesOrderNumber').type('OD00000001');
                cy.get('#supplierCode').click();
                cy.get('.close-modal-button').click();
                cy.get('#supplierCode').type('001');
                cy.get('#purchaseManagerCode').click();
                cy.get('.close-modal-button').click();
                cy.get('#purchaseManagerCode').type('EMP001');
                cy.get('#designatedDeliveryDate').type('2024-01-31');
                cy.get('#warehouseCode').type('W01');
                cy.get('#remarks').type('テスト発注');

                // 明細行を追加
                cy.get('.add-line-button').click();

                // 明細行1のデータを入力
                cy.get('.purchase-order-line-row').eq(0).within(() => {
                    // 発注行表示番号
                    cy.get(':nth-child(2) > .table-input').clear().type('1');
                    // 受注番号
                    cy.get(':nth-child(3) > .table-input').type('OD00000001');
                    // 受注行番号
                    cy.get(':nth-child(4) > .table-input').type('1');
                    // 商品コード
                    cy.get(':nth-child(5) input[type="text"]').type('P0001');
                    // 商品名
                    cy.get(':nth-child(6) > .table-input').type('テスト商品');
                    // 発注単価
                    cy.get(':nth-child(7) > .table-input').type('800');
                    // 発注数量
                    cy.get(':nth-child(8) > .table-input').type('5');
                    // 入荷数量
                    cy.get(':nth-child(9) > .table-input').type('0');
                    // 完了フラグ (ドロップダウン選択)
                    cy.get(':nth-child(10) > .table-input').select('未完了');
                });

                // 発注を保存
                cy.get('#save').click();

                // 作成完了メッセージの確認
                cy.get('#message').contains('発注を作成しました。');
            });
        });

        context('発注検索', () => {
            it('検索', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-purchase-order-number').type('PO00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get('.collection-object-item-content-name').contains('PO00000009');
            });
        });

        context('発注登録情報編集', () => {
            it('登録情報編集', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-purchase-order-number').type('PO00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#edit').click();

                cy.get('#remarks').clear();
                cy.get('#remarks').type('テスト発注更新');

                // Update purchase order line
                cy.get('.purchase-order-line-row').eq(0).within(() => {
                    cy.get(':nth-child(8) > .table-input').clear().type('10');
                    cy.get(':nth-child(10) > .table-input').select('完了');
                });

                cy.get('#save').click();
                cy.get('#message').contains('発注を更新しました。');
            });
        });

        context('発注削除', () => {
            it('削除', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-purchase-order-number').type('PO00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#delete').click();
                cy.get('#message').contains('発注を削除しました。');
            });
        });
    });
});