describe('売上集計管理', () => {
    context('管理者', () => {
        beforeEach(() => {
            cy.login('U000003', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(1) > :nth-child(6) > :nth-child(2) > #side-nav-sales-nav').click();
        }

        context('売上集計画面', () => {
            it('売上集計画面の表示', () => {
                userPage();
                cy.get('.collection-view-container').should('be.visible');
                cy.get('.single-view-title').contains('売上集計');
                cy.get('#execute').should('be.visible');
            });
        });

        context('売上集計実行', () => {
            it('売上集計を実行する', () => {
                userPage();

                // 集計実行ボタンをクリック
                cy.get('#execute').click();

                // 集計結果が表示されることを確認
                cy.get('.upload-result-item').should('be.visible');
                cy.get('.check-result-message').should('be.visible');
                
                // 詳細情報が表示されていることを確認
                // cy.get('.upload-result-details').should('exist');
            });
        });

        context('売上集計結果の削除', () => {
            it('売上集計結果を削除する', () => {
                userPage();

                // 集計実行ボタンをクリック
                cy.get('#execute').click();

                // 集計結果が表示されることを確認
                cy.get('.upload-result-item').should('be.visible');

                // 最初の集計結果の削除ボタンをクリック
                cy.get('.upload-result-item').first().find('.action-button').click();

                // 該当の集計結果が削除されたことを確認
                // または結果リストが空になったことを確認
                cy.get('.upload-result-item').should('not.exist');
            });
        });

        context('複数回の集計実行と結果表示', () => {
            it('複数回集計を実行して結果が追加されることを確認', () => {
                userPage();

                // 1回目の集計実行
                cy.get('#execute').click();

                // 集計結果が表示されることを確認
                cy.get('.upload-result-item').should('be.visible');
                cy.get('.upload-result-item').should('have.length', 1);

                // 2回目の集計実行
                cy.get('#execute').click();

                // 集計結果が2件表示されることを確認
                cy.get('.upload-result-item').should('have.length', 2);
                
                // 詳細データの各項目を確認
                // cy.get('.detail-item').should('be.visible');
                // cy.get('.detail-key').should('be.visible');
                // cy.get('.detail-value').should('be.visible');
            });
        });
    });

    context('利用者', () => {
        beforeEach(() => {
            cy.login('U000001', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-item > :nth-child(1) > :nth-child(6) > :nth-child(2) > #side-nav-sales-nav').click();
        }

        context('売上集計画面', () => {
            it('売上集計画面の表示', () => {
                userPage();
                cy.get('.collection-view-container').should('be.visible');
                cy.get('.single-view-title').contains('売上集計');
                cy.get('#execute').should('be.visible');
            });
        });

        context('売上集計実行', () => {
            it('売上集計を実行する', () => {
                userPage();

                // 集計実行ボタンをクリック
                cy.get('#execute').click();

                // 集計結果が表示されることを確認
                cy.get('.upload-result-item').should('be.visible');
                cy.get('.check-result-message').should('be.visible');
                
                // 詳細情報が表示されていることを確認
                // cy.get('.upload-result-details').should('exist');
            });
        });

        context('売上集計結果の削除', () => {
            it('売上集計結果を削除する', () => {
                userPage();

                // 集計実行ボタンをクリック
                cy.get('#execute').click();

                // 集計結果が表示されることを確認
                cy.get('.upload-result-item').should('be.visible');

                // 最初の集計結果の削除ボタンをクリック
                cy.get('.upload-result-item').first().find('.action-button').click();

                // 該当の集計結果が削除されたことを確認
                // または結果リストが空になったことを確認
                cy.get('.upload-result-item').should('not.exist');
            });
        });
    });
});