describe('売上管理', () => {
    context('管理者', () => {
        beforeEach(() => {
            cy.login('U000003', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(1) > :nth-child(6) > :nth-child(1) > #side-nav-sales-nav').click();
        }

        context('売上一覧', () => {
            it('売上一覧の表示', () => {
                userPage();
                cy.get('.collection-view-container').should('be.visible');
            });
        });

        context('売上新規登録', () => {
            it('新規登録', () => {
                userPage();

                // 売上新規画面を開く
                cy.get('#new').click();

                // ヘッダ情報を入力
                cy.get('#salesNumber').type('SA00000009');
                cy.get('#orderNumber').type('OD00000009');
                cy.get('#salesDate').type('2024-01-01');
                cy.get('#salesType').select('現金');
                cy.get('#departmentCode').type('10000');
                cy.get('.collection-object-container-modal > .collection-object-list > :nth-child(1) > .collection-object-item-actions > .action-button').click();
                cy.get('#customerCode').type('001');
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-customer').click();
                cy.get('#employeeCode').type('EMP001');
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-employee').click();
                cy.get('#remarks').type('テスト売上');
                cy.get('#voucherNumber').type('12345');
                cy.get('#originalVoucherNumber').type('ORIG12345');

                // 明細行を追加
                cy.get('.add-line-button').click();

                // 明細行1のデータを入力
                cy.get('.sales-line-row').eq(0).within(() => {
                    // 商品コード
                    cy.get('input[type="text"]').eq(0).type('P0001');
                    // 商品名
                    cy.get('input[type="text"]').eq(1).type('テスト商品');
                    // 販売単価
                    cy.get('input[type="number"]').eq(0).type('1000');
                    // 消費税率 (ドロップダウン選択)
                    cy.get('select').select('標準');
                    // 売上数量
                    cy.get('input[type="number"]').eq(1).type('2');
                    // 出荷済数量
                    cy.get('input[type="number"]').eq(2).type('2');
                    // 値引金額
                    cy.get('input[type="number"]').eq(3).type('0');
                    // 請求日
                    cy.get('input[type="date"]').eq(0).type('2024-01-31');
                    // 請求番号
                    cy.get('input[type="text"]').eq(2).type('BL001');
                    // 請求遅延区分
                    cy.get('input[type="number"]').eq(4).type('0');
                    // 自動仕訳日
                    cy.get('input[type="date"]').eq(1).type('2024-01-31');
                });

                // 売上を保存
                cy.get('#save').click();

                // 作成完了メッセージの確認
                cy.get('#message').contains('売上を作成しました。');
            });
        });

        context('売上検索', () => {
            it('検索', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-sales-number').type('SA00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get(':nth-child(2) > .collection-object-item-content-name').contains('SA00000009');
            });
        });

        context('売上登録情報編集', () => {
            it('登録情報編集', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-sales-number').type('SA00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#edit').click();

                cy.get('#remarks').clear();
                cy.get('#remarks').type('テスト売上更新');

                // Update sales line
                cy.get('.sales-line-row').eq(0).within(() => {
                    // 売上数量を更新
                    cy.get('input[type="number"]').eq(1).clear();
                    cy.get('input[type="number"]').eq(1).type('3');
                });

                cy.get('#save').click();
                cy.get('#message').contains('売上を更新しました。');
            });
        });

        context('売上削除', () => {
            it('削除', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-sales-number').type('SA00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#delete').click();
                cy.get('#message').contains('売上を削除しました。');
            });
        });
    });

    context('利用者', () => {
        beforeEach(() => {
            cy.login('U000001', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-item > :nth-child(1) > :nth-child(6) > :nth-child(1) > #side-nav-sales-nav').click();
        }

        context('売上一覧', () => {
            it('売上一覧の表示', () => {
                userPage();
                cy.get('.collection-view-container').should('be.visible');
            });
        });

        context('売上新規登録', () => {
            it('新規登録', () => {
                userPage();

                // 売上新規画面を開く
                cy.get('#new').click();

                // ヘッダ情報を入力
                cy.get('#salesNumber').type('SA00000009');
                cy.get('#orderNumber').type('OD00000009');
                cy.get('#salesDate').type('2024-01-01');
                cy.get('#salesType').select('現金');
                cy.get('#departmentCode').type('10000');
                cy.get('.collection-object-container-modal > .collection-object-list > :nth-child(1) > .collection-object-item-actions > .action-button').click();
                cy.get('#customerCode').type('001');
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-customer').click();
                cy.get('#employeeCode').type('EMP001');
                cy.get(':nth-child(1) > .collection-object-item-actions > #select-employee').click();
                cy.get('#remarks').type('テスト売上');
                cy.get('#voucherNumber').type('12345');
                cy.get('#originalVoucherNumber').type('ORIG12345');

                // 明細行を追加
                cy.get('.add-line-button').click();

                // 明細行1のデータを入力
                cy.get('.sales-line-row').eq(0).within(() => {
                    // 商品コード
                    cy.get('input[type="text"]').eq(0).type('P0001');
                    // 商品名
                    cy.get('input[type="text"]').eq(1).type('テスト商品');
                    // 販売単価
                    cy.get('input[type="number"]').eq(0).type('1000');
                    // 消費税率 (ドロップダウン選択)
                    cy.get('select').select('標準');
                    // 売上数量
                    cy.get('input[type="number"]').eq(1).type('2');
                    // 出荷済数量
                    cy.get('input[type="number"]').eq(2).type('2');
                    // 値引金額
                    cy.get('input[type="number"]').eq(3).type('0');
                    // 請求日
                    cy.get('input[type="date"]').eq(0).type('2024-01-31');
                    // 請求番号
                    cy.get('input[type="text"]').eq(2).type('BL001');
                    // 請求遅延区分
                    cy.get('input[type="number"]').eq(4).type('0');
                    // 自動仕訳日
                    cy.get('input[type="date"]').eq(1).type('2024-01-31');
                });

                // 売上を保存
                cy.get('#save').click();

                // 作成完了メッセージの確認
                cy.get('#message').contains('売上を作成しました。');
            });
        });
        context('売上検索', () => {
            it('検索', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-sales-number').type('SA00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get(':nth-child(2) > .collection-object-item-content-name').contains('SA00000009');
            });
        });

        context('売上登録情報編集', () => {
            it('登録情報編集', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-sales-number').type('SA00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#edit').click();

                cy.get('#remarks').clear();
                cy.get('#remarks').type('テスト売上更新');

                // Update sales line
                cy.get('.sales-line-row').eq(0).within(() => {
                    // 売上数量を更新
                    cy.get('input[type="number"]').eq(1).clear();
                    cy.get('input[type="number"]').eq(1).type('3');
                });

                cy.get('#save').click();
                cy.get('#message').contains('売上を更新しました。');
            });
        });

        context('売上削除', () => {
            it('削除', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-sales-number').type('SA00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#delete').click();
                cy.get('#message').contains('売上を削除しました。');
            });
        });
    });

});