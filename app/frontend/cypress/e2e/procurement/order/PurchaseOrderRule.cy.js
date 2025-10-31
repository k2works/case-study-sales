describe('発注管理', () => {
    context('管理者', () => {
        beforeEach(() => {
            cy.login('U000003', 'a234567Z');

            // 事前にアップロードしてルールチェック対象データを作成
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(2) > .nav-sub-list > :nth-child(2) > #side-nav-order-order-nav').click();
            // アップロードボタンの確認
            cy.get('#upload').should('be.visible');

            // アップロードモーダルを開く
            cy.get('#upload').click();
            cy.get('.modal').should('be.visible');

            // ファイルアップロード
            cy.uploadFile('input[type="file"]', 'purchase_order_multiple.csv', 'text/csv');

            // アップロード実行
            cy.get('.modal button').contains('アップロード').click();
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(2) > .nav-sub-list > :nth-child(3) > #side-nav-order-order-nav').click();
        }

        context('発注ルール', () => {
            it('実行', () => {
                userPage();

                // 実行ボタンの確認
                cy.get('#execute').should('be.visible');

                // ルールチェック実行
                cy.get('#execute').click();
                cy.get('p').contains('発注ルールチェックを実行しました');
            });
        });
    });

    context('利用者', () => {
        beforeEach(() => {
            cy.login('U000001', 'a234567Z');

            // 事前にアップロードしてルールチェック対象データを作成
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-item > :nth-child(2) > .nav-sub-list > :nth-child(2) > #side-nav-order-order-nav').click();
            // アップロードボタンの確認
            cy.get('#upload').should('be.visible');

            // アップロードモーダルを開く
            cy.get('#upload').click();
            cy.get('.modal').should('be.visible');

            // ファイルアップロード
            cy.uploadFile('input[type="file"]', 'purchase_order_multiple.csv', 'text/csv');

            // アップロード実行
            cy.get('.modal button').contains('アップロード').click();
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-item > :nth-child(2) > .nav-sub-list > :nth-child(3) > #side-nav-order-order-nav').click();
        }

        context('発注ルール', () => {
            it('実行', () => {
                userPage();

                // 実行ボタンの確認
                cy.get('#execute').should('be.visible');

                // ルールチェック実行
                cy.get('#execute').click();
                cy.get('p').contains('発注ルールチェックを実行しました');
            });
        });
    });
});