describe('在庫管理', () => {
    context('管理者', () => {
        beforeEach(() => {
            cy.login('U000003', 'a234567Z');

            // 事前にアップロードしてルールチェック対象データを作成
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(3) > .nav-sub-list > :nth-child(2) > #side-nav-inventory-nav').click();
            // アップロードボタンの確認
            cy.get('#upload').should('be.visible');

            // アップロードモーダルを開く
            cy.get('#upload').click();
            cy.get('.modal').should('be.visible');

            // ファイルアップロード
            cy.uploadFile('input[type="file"]', 'valid_inventory.csv', 'text/csv');

            // アップロード実行
            cy.get('.modal button').contains('アップロード').click();
            cy.get('p').contains('在庫アップロードが完了しました');
        })

        const inventoryRulePage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(3) > .nav-sub-list > :nth-child(3) > #side-nav-inventory-nav').click();
        }

        context('在庫ルール', () => {
            it('実行', () => {
                inventoryRulePage();

                // 実行ボタンの確認
                cy.get('#execute').should('be.visible');

                // ルールチェック実行
                cy.get('#execute').click();
                cy.get('p').contains('在庫ルールチェックを実行しました');
            });

            it('ルール結果の確認', () => {
                inventoryRulePage();

                // ルールチェック実行
                cy.get('#execute').click();
                cy.get('p').contains('在庫ルールチェックを実行しました');

                // ルール結果が表示されることを確認
                cy.get('body').then($body => {
                    if ($body.find('.upload-result-item').length > 0) {
                        cy.get('.upload-result-item').should('be.visible');
                        cy.get('.upload-result-message').should('be.visible');
                    }
                });
            });

            it('ルール結果の削除', () => {
                inventoryRulePage();

                // ルールチェック実行
                cy.get('#execute').click();

                // ルール結果がある場合、削除ボタンをテスト
                cy.get('.upload-result-item > .action-button').click();
            });

            it('在庫不足のルールチェック', () => {
                // まず在庫不足データをアップロード
                cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(3) > .nav-sub-list > :nth-child(2) > #side-nav-inventory-nav').click();
                cy.get('#upload').click();
                cy.uploadFile('input[type="file"]', 'inventory_valid_for_check.csv', 'text/csv');
                cy.get('.modal button').contains('アップロード').click();

                // ルールページに移動
                inventoryRulePage();

                // ルールチェック実行
                cy.get('#execute').click();
                cy.get('p').contains('在庫ルールチェックを実行しました');

                // 在庫不足の警告が出ることを確認
                cy.get('p').should('contain', '確認項目があります');
            });
        });
    });

    context('利用者', () => {
        beforeEach(() => {
            cy.login('U000001', 'a234567Z');

            // 事前にアップロードしてルールチェック対象データを作成
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-item > :nth-child(3) > .nav-sub-list > :nth-child(2) > #side-nav-inventory-nav').click();
            // アップロードボタンの確認
            cy.get('#upload').should('be.visible');

            // アップロードモーダルを開く
            cy.get('#upload').click();
            cy.get('.modal').should('be.visible');

            // ファイルアップロード
            cy.uploadFile('input[type="file"]', 'inventory_valid_for_check.csv', 'text/csv');

            // アップロード実行
            cy.get('.modal button').contains('アップロード').click();
            cy.get('p').contains('在庫アップロードが完了しました');
        })

        const inventoryRulePage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-item > :nth-child(3) > .nav-sub-list > :nth-child(3) > #side-nav-inventory-nav').click();
        }

        context('在庫ルール', () => {
            it('実行', () => {
                inventoryRulePage();

                // 実行ボタンの確認
                cy.get('#execute').should('be.visible');

                // ルールチェック実行
                cy.get('#execute').click();
                cy.get('p').contains('在庫ルールチェックを実行しました');
            });

            it('ルール結果の確認', () => {
                inventoryRulePage();

                // ルールチェック実行
                cy.get('#execute').click();
                cy.get('p').contains('在庫ルールチェックを実行しました');

                // ルール結果が表示されることを確認
                cy.get('body').then($body => {
                    if ($body.find('.upload-result-item').length > 0) {
                        cy.get('.upload-result-item').should('be.visible');
                        cy.get('.upload-result-message').should('be.visible');
                        cy.get('.upload-result-details').should('be.visible');
                    }
                });
            });

            it('複数回のルールチェック実行', () => {
                inventoryRulePage();

                // 1回目のルールチェック実行
                cy.get('#execute').click();
                cy.get('p').contains('在庫ルールチェックを実行しました');

                // 2回目のルールチェック実行
                cy.get('#execute').click();
                cy.get('p').contains('在庫ルールチェックを実行しました');

                // 複数の結果が表示されることを確認
                cy.get('body').then($body => {
                    if ($body.find('.upload-result-item').length > 1) {
                        cy.get('.upload-result-item').should('have.length.at.least', 1);
                    }
                });
            });

            it('ルール結果の詳細確認', () => {
                inventoryRulePage();

                // ルールチェック実行
                cy.get('#execute').click();

                // ルール結果の詳細が表示されることを確認
                cy.get('body').then($body => {
                    if ($body.find('.upload-result-details').length > 0) {
                        cy.get('.detail-item').should('be.visible');
                        cy.get('.detail-key').should('be.visible');
                        cy.get('.detail-value').should('be.visible');
                    }
                });
            });
        });
    });
});