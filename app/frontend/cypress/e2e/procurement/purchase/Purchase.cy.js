describe('仕入管理', () => {
    context('管理者', () => {
        beforeEach(() => {
            cy.login('U000003', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-purchase-nav').click();
        }

        context('仕入一覧', () => {
            it('仕入一覧の表示', () => {
                userPage();
                cy.get('.collection-view-container').should('be.visible');
            });
        });

        context('仕入新規登録', () => {
            it('新規登録', () => {
                userPage();

                // 仕入新規画面を開く
                cy.get('#new').click();

                // ヘッダ情報を入力
                cy.get('#purchaseNumber').type('PU00000009');
                cy.get('#purchaseDate').type('2024-01-01');
                cy.get('#supplierCode').click();
                cy.get('.close-modal-button').click();
                cy.get('#supplierCode').type('001');
                cy.get('#purchaseManagerCode').click();
                cy.get('.close-modal-button').click();
                cy.get('#purchaseManagerCode').type('EMP001');
                cy.get('#startDate').type('2024-01-01');
                cy.get('#purchaseOrderNumber').type('PO00000001');
                cy.get('#departmentCode').click();
                cy.get('.close-modal-button').click();
                cy.get('#departmentCode').type('D001');
                cy.get('#remarks').type('テスト仕入');

                // 明細行を追加
                cy.get('.add-line-button').click();

                // 明細行1のデータを入力
                cy.get('.purchase-order-line-row').eq(0).within(() => {
                    // 仕入行表示番号
                    cy.get(':nth-child(2) > .table-input').clear().type('1');
                    // 発注行番号
                    cy.get(':nth-child(3) > .table-input').type('1');
                    // 商品コード
                    cy.get(':nth-child(4) input[type="text"]').type('P0001');
                    // 商品名
                    cy.get(':nth-child(5) > .table-input').type('テスト商品');
                    // 倉庫コード
                    cy.get(':nth-child(6) input[type="text"]').type('W01');
                    // 仕入単価
                    cy.get(':nth-child(7) > .table-input').type('800');
                    // 仕入数量
                    cy.get(':nth-child(8) > .table-input').type('5');
                });

                // 仕入を保存
                cy.get('#save').click();

                // 作成完了メッセージの確認
                cy.get('#message').contains('仕入を作成しました。');
            });
        });

        context('仕入検索', () => {
            it('検索', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-purchase-number').type('PU00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get('.collection-object-item-content-name').contains('PU00000009');
            });
        });

        context('仕入登録情報編集', () => {
            it('登録情報編集', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-purchase-number').type('PU00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#edit').click();

                cy.get('#remarks').clear();
                cy.get('#remarks').type('テスト仕入更新');

                // Update purchase line
                cy.get('.purchase-order-line-row').eq(0).within(() => {
                    cy.get(':nth-child(8) > .table-input').clear().type('10');
                });

                cy.get('#save').click();
                cy.get('#message').contains('仕入を更新しました。');
            });
        });

        context('仕入削除', () => {
            it('削除', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-purchase-number').type('PU00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#delete').click();
                cy.get('#message').contains('仕入を削除しました。');
            });
        });
    });

    context('利用者', () => {
        beforeEach(() => {
            cy.login('U000001', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-purchase-nav').click();
        }

        context('仕入一覧', () => {
            it('仕入一覧の表示', () => {
                userPage();
                cy.get('.collection-view-container').should('be.visible');
            });
        });

        context('仕入新規登録', () => {
            it('新規登録', () => {
                userPage();

                // 仕入新規画面を開く
                cy.get('#new').click();

                // ヘッダ情報を入力
                cy.get('#purchaseNumber').type('PU00000009');
                cy.get('#purchaseDate').type('2024-01-01');
                cy.get('#supplierCode').click();
                cy.get('.close-modal-button').click();
                cy.get('#supplierCode').type('001');
                cy.get('#purchaseManagerCode').click();
                cy.get('.close-modal-button').click();
                cy.get('#purchaseManagerCode').type('EMP001');
                cy.get('#startDate').type('2024-01-01');
                cy.get('#purchaseOrderNumber').type('PO00000001');
                cy.get('#departmentCode').click();
                cy.get('.close-modal-button').click();
                cy.get('#departmentCode').type('D001');
                cy.get('#remarks').type('テスト仕入');

                // 明細行を追加
                cy.get('.add-line-button').click();

                // 明細行1のデータを入力
                cy.get('.purchase-order-line-row').eq(0).within(() => {
                    // 仕入行表示番号
                    cy.get(':nth-child(2) > .table-input').clear().type('1');
                    // 発注行番号
                    cy.get(':nth-child(3) > .table-input').type('1');
                    // 商品コード
                    cy.get(':nth-child(4) input[type="text"]').type('P0001');
                    // 商品名
                    cy.get(':nth-child(5) > .table-input').type('テスト商品');
                    // 倉庫コード
                    cy.get(':nth-child(6) input[type="text"]').type('W01');
                    // 仕入単価
                    cy.get(':nth-child(7) > .table-input').type('800');
                    // 仕入数量
                    cy.get(':nth-child(8) > .table-input').type('5');
                });

                // 仕入を保存
                cy.get('#save').click();

                // 作成完了メッセージの確認
                cy.get('#message').contains('仕入を作成しました。');
            });
        });

        context('仕入検索', () => {
            it('検索', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-purchase-number').type('PU00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get('.collection-object-item-content-name').contains('PU00000009');
            });
        });

        context('仕入登録情報編集', () => {
            it('登録情報編集', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-purchase-number').type('PU00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#edit').click();

                cy.get('#remarks').clear();
                cy.get('#remarks').type('テスト仕入更新');

                // Update purchase line
                cy.get('.purchase-order-line-row').eq(0).within(() => {
                    cy.get(':nth-child(8) > .table-input').clear().type('10');
                });

                cy.get('#save').click();
                cy.get('#message').contains('仕入を更新しました。');
            });
        });

        context('仕入削除', () => {
            it('削除', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-purchase-number').type('PU00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#delete').click();
                cy.get('#message').contains('仕入を削除しました。');
            });
        });
    });
});
