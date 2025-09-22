// uncaught exceptionの処理をグローバルに設定
Cypress.on('uncaught:exception', (err, runnable) => {
    // 特定のエラーメッセージを無視
    if (err.message.includes('倉庫コードは必須です') ||
        err.message.includes('商品コードは必須です') ||
        err.message.includes('ロット番号は必須です') ||
        err.message.includes('在庫区分は必須です') ||
        err.message.includes('良品区分は必須です')) {
        return false; // Cypressテストを落とさない
    }
    return true;
});

describe('在庫管理', () => {
    context('管理者', () => {
        beforeEach(() => {
            cy.login('U000003', 'a234567Z');
        })

        const inventoryUploadPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(3) > .nav-sub-list > :nth-child(2) > #side-nav-inventory-nav').click();
        }

        context('在庫一括登録', () => {
            it('アップロード', () => {
                inventoryUploadPage();

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
            });

            it('不正なファイルのアップロード', () => {
                inventoryUploadPage();

                cy.get('#upload').click();

                // 不正なファイルをアップロード
                cy.uploadFile('input[type="file"]', 'invalid_inventory.csv', 'text/csv');

                cy.get('.modal button').contains('アップロード').click();

                // エラーメッセージの確認
                cy.get('p').should('contain', '在庫アップロードでエラーが発生しました');
            });

            it('アップロード結果の削除', () => {
                inventoryUploadPage();

                // アップロード結果がある場合、削除ボタンをテスト
                cy.get('body').then($body => {
                    if ($body.find('.upload-result-item').length > 0) {
                        cy.get('.upload-result-item').first().find('button').contains('削除').click();
                        // 削除後、該当の結果アイテムが削除されることを確認
                        cy.get('.upload-result-item').should('have.length.lessThan', 1);
                    }
                });
            });
        });
    });

    context('利用者', () => {
        beforeEach(() => {
            cy.login('U000001', 'a234567Z');
        })

        const inventoryUploadPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-item > :nth-child(3) > .nav-sub-list > :nth-child(2) > #side-nav-inventory-nav').click();
        }

        context('在庫一括登録', () => {
            it('アップロード', () => {
                inventoryUploadPage();

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
            });

            it('不正なファイルのアップロード', () => {
                inventoryUploadPage();

                cy.get('#upload').click();

                // 不正なファイルをアップロード
                cy.uploadFile('input[type="file"]', 'invalid_inventory.csv', 'text/csv');

                cy.get('.modal button').contains('アップロード').click();

                // エラーメッセージの確認
                cy.get('p').should('contain', '在庫アップロードでエラーが発生しました');
            });

            it('アップロード結果の確認', () => {
                inventoryUploadPage();

                // アップロード結果が表示されることを確認
                cy.get('body').then($body => {
                    if ($body.find('.upload-result-item').length > 0) {
                        cy.get('.upload-result-item').should('be.visible');
                        cy.get('.upload-result-message').should('be.visible');
                    }
                });
            });
        });
    });
});