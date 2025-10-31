// uncaught exceptionの処理をグローバルに設定
Cypress.on('uncaught:exception', (err, runnable) => {
    // 特定のエラーメッセージを無視
    if (err.message.includes('社員コードは必須です') || 
        err.message.includes('仕入先コードは必須です')) {
        return false; // Cypressテストを落とさない
    }
    return true;
});

describe('発注管理', () => {
    context('管理者', () => {
        beforeEach(() => {
            cy.login('U000003', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(2) > .nav-sub-list > :nth-child(2) > #side-nav-order-order-nav').click();
        }

        context('発注一括登録', () => {
            it('アップロード', () => {
                userPage();

                // アップロードボタンの確認
                cy.get('#upload').should('be.visible');

                // アップロードモーダルを開く
                cy.get('#upload').click();
                cy.get('.modal').should('be.visible');

                // ファイルアップロード
                cy.uploadFile('input[type="file"]', 'purchase_order_multiple.csv', 'text/csv');

                // アップロード実行
                cy.get('.modal button').contains('アップロード').click();
                cy.get('p').contains('発注をアップロードしました');
            });

            it('不正なファイルのアップロード', () => {
                userPage();

                cy.get('#upload').click();

                // 不正なファイルをアップロード
                cy.uploadFile('input[type="file"]', 'invalid.csv', 'text/csv');

                cy.get('.modal button').contains('アップロード').click();

                // エラーメッセージの確認
                cy.get('.view-message-content-error-text > p').should('contain', '社員コードは必須です');
            });
        });
    });

    context('利用者', () => {
        beforeEach(() => {
            cy.login('U000001', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-item > :nth-child(2) > .nav-sub-list > :nth-child(2) > #side-nav-order-order-nav').click();
        }

        context('発注一括登録', () => {
            it('アップロード', () => {
                userPage();

                // アップロードボタンの確認
                cy.get('#upload').should('be.visible');

                // アップロードモーダルを開く
                cy.get('#upload').click();
                cy.get('.modal').should('be.visible');

                // ファイルアップロード
                cy.uploadFile('input[type="file"]', 'purchase_order_multiple.csv', 'text/csv');

                // アップロード実行
                cy.get('.modal button').contains('アップロード').click();
                cy.get('p').contains('発注をアップロードしました');
            });

            it('不正なファイルのアップロード', () => {
                userPage();

                cy.get('#upload').click();

                // 不正なファイルをアップロード
                cy.uploadFile('input[type="file"]', 'invalid.csv', 'text/csv');

                cy.get('.modal button').contains('アップロード').click();

                // エラーメッセージの確認
                cy.get('.view-message-content-error-text > p').should('contain', '社員コードは必須です');
            });
        });
    });
});