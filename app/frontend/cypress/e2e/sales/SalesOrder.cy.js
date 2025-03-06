describe('受注管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z');
    })

    const userPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(1) > .nav-sub-list > .nav-sub-item > #side-nav-product-nav').click();
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

            // 受注新規画面を開く
            cy.get('#new').click();

            // ヘッダ情報を入力
            cy.get('#orderNumber').type('1000000009');
            cy.get('#orderDate').type('2024-01-01');
            cy.get('#departmentCode').type('10000');
            cy.get('.collection-object-container-modal > .collection-object-list > :nth-child(1) > .collection-object-item-actions > .action-button').click();
            cy.get('#customerCode').type('001');
            cy.get(':nth-child(1) > .collection-object-item-actions > #select-customer').click();
            cy.get('#employeeCode').type('EMP001');
            cy.get(':nth-child(1) > .collection-object-item-actions > #select-employee').click();
            cy.get('#desiredDeliveryDate').type('2024-01-31');
            cy.get('#customerOrderNumber').type('CO001');
            cy.get('#warehouseCode').type('001');
            cy.get('#remarks').type('テスト受注');

            // 明細行を追加
            cy.get('.add-line-button').click();

            // 明細行1のデータを入力
            cy.get('.order-line-row').eq(0).within(() => {
                // 商品コード
                cy.get('input[type="text"]').eq(0).type('P0001');
                // 商品名
                cy.get('input[type="text"]').eq(1).type('テスト商品');
                // 販売単価
                cy.get('input[type="number"]').eq(0).type('1000');
                // 受注数量
                cy.get('input[type="number"]').eq(1).type('2');
                // 消費税率 (ドロップダウン選択)
                cy.get(':nth-child(5) > .table-input').select('標準');
                // 引当数量
                cy.get('input[type="number"]').eq(2).type('1');
                // 出荷指示数量
                cy.get('input[type="number"]').eq(3).type('1');
                // 出荷済数量
                cy.get('input[type="number"]').eq(4).type('0');
                // 完了フラグ (ドロップダウン選択)
                cy.get(':nth-child(9) > .table-input').select('未完了');
                // 値引金額
                cy.get('input[type="number"]').eq(5).type('0');
                // 納期
                cy.get('input[type="datetime-local"]').type('2024-02-01T12:00');
            });

            // 受注を保存
            cy.get('#save').click();

            // 作成完了メッセージの確認
            cy.get('#message').contains('受注を作成しました。');
        });
    });

    context('受注検索', () => {
        it('検索', () => {
            userPage();
            cy.get('#search').click();
            cy.get('#search-order-number').type('1000000009');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.get(':nth-child(2) > .collection-object-item-content-name').contains('1000000009');
        });
    });

    context('受注登録情報編集', () => {
        it('登録情報編集', () => {
            userPage();
            cy.get('#search').click();
            cy.get('#search-order-number').type('1000000009');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#edit').click();

            cy.get('#remarks').clear();
            cy.get('#remarks').type('テスト受注更新');

            // Update order line
            cy.get(':nth-child(4) > .table-input').clear();
            cy.get(':nth-child(4) > .table-input').type('3');
            cy.get(':nth-child(9) > .table-input').select('完了');

            cy.get('#save').click();
            cy.get('#message').contains('受注を更新しました。');
        });
    });

    context('受注一括登録', () => {
        it('タブ切り替えとアップロード', () => {
            userPage();

            // 一括登録タブに切り替え
            cy.contains('一括登録').click();
            cy.get('.upload-container').should('be.visible');

            // アップロードボタンの確認
            cy.get('#upload').should('be.visible');

            // アップロードモーダルを開く
            cy.get('#upload').click();
            cy.get('.modal').should('be.visible');

            // ファイルアップロード
            cy.uploadFile('input[type="file"]', 'sales_order_multiple.csv', 'text/csv');

            // アップロード実行
            cy.get('.modal button').contains('アップロード').click();
            cy.get('#message').contains('アップロードが完了しました');

            // アップロード結果の確認
            cy.get('.upload-result-container').should('be.visible');
            cy.get('.upload-result-item').should('have.length.at.least', 1);

            // メッセージの確認
            cy.get('.upload-result-message').should('be.visible');
            cy.get('.upload-result-message .message').should('contain', 'アップロードが完了しました');

            // 詳細情報の確認
            cy.get('.upload-result-details').should('be.visible');
            cy.get('.upload-result-detail').should('have.length.at.least', 1);
            cy.get('.detail-key').should('be.visible');
            cy.get('.detail-value').should('be.visible');

            // 結果の削除
            cy.get('.upload-result-item button').first().click();
            cy.get('.upload-result-item').should('have.length', 0);
        });

        it('不正なファイルのアップロード', () => {
            userPage();
            cy.contains('一括登録').click();
            cy.get('#upload').click();

            // 不正なファイルをアップロード
            cy.uploadFile('input[type="file"]', 'invalid.csv', 'text/csv');

            cy.get('.modal button').contains('アップロード').click();
            cy.get('.upload-result-message .error').should('be.visible');
            cy.get('.upload-result-details').should('be.visible');
            cy.get('.detail-key').contains('エラー');
        });
    });

    context('受注削除', () => {
        it('削除', () => {
            userPage();
            cy.get('#search').click();
            cy.get('#search-order-number').type('1000000009');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#delete').click();
            cy.get('#message').contains('受注を削除しました。');
        });
    });
});
