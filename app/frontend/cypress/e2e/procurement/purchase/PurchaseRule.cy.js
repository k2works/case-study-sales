describe('仕入管理', () => {
    context('管理者', () => {
        beforeEach(() => {
            cy.login('U000003', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(2) > :nth-child(4) > :nth-child(2) > #side-nav-purchase-nav').click();
        }

        context('仕入ルール', () => {
            it('実行', () => {
                userPage();

                // 実行ボタンの確認
                cy.get('#execute').should('be.visible');

                // ルールチェック実行
                cy.get('#execute').click();
                cy.get('p').contains('仕入ルールチェックを実行しました');
            });
        });
    });

    context('利用者', () => {
        beforeEach(() => {
            cy.login('U000001', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-item > :nth-child(2) > :nth-child(4) > :nth-child(2) > #side-nav-purchase-nav').click();
        }

        context('仕入ルール', () => {
            it('実行', () => {
                userPage();

                // 実行ボタンの確認
                cy.get('#execute').should('be.visible');

                // ルールチェック実行
                cy.get('#execute').click();
                cy.get('p').contains('仕入ルールチェックを実行しました');
            });
        });
    });
});
