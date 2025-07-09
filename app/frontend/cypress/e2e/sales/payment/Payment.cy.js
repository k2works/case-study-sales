describe('入金管理', () => {
    context('管理者', () => {
        beforeEach(() => {
            cy.login('U000003', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(1) > :nth-child(10) > .nav-sub-item > #side-nav-payment-nav').click();
        }

        context('入金一覧', () => {
            it('入金一覧の表示', () => {
                userPage();
                cy.get('.collection-view-container').should('be.visible');
            });
        });

        context('入金新規登録', () => {
            it('新規登録', () => {
                userPage();

                // 入金新規画面を開く
                cy.get('#new').click();

                // ヘッダ情報を入力
                cy.get('#paymentNumber').type('PA00000009');
                cy.get('#paymentDate').type('2024-01-01');
                cy.get('#departmentCode').type('10000');
                cy.get('#departmentStartDate').type('2021-01-01')
                cy.get('#customerCode').type('001');
                cy.get('#paymentMethodType').select('現金');
                cy.get('#paymentAccountCode').type('ACC001');
                cy.get('#paymentAmount').type('10000');
                cy.get('#offsetAmount').type('10000');

                // 入金を保存
                cy.get('#save').click();

                // 作成完了メッセージの確認
                cy.get('#message').contains('入金を作成しました。');
            });
        });

        context('入金検索', () => {
            it('検索', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-payment-number').type('PA00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get(':nth-child(2) > .collection-object-item-content-name').contains('PA00000009');
            });
        });

        context('入金登録情報編集', () => {
            it('登録情報編集', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-payment-number').type('PA00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#edit').click();

                // 入金金額を更新
                cy.get('#paymentAmount').clear();
                cy.get('#paymentAmount').type('15000');

                cy.get('#save').click();
                cy.get('#message').contains('入金を更新しました。');
            });
        });

        context('入金削除', () => {
            it('削除', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-payment-number').type('PA00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#delete').click();
                cy.get('#message').contains('入金情報を削除しました。');
            });
        });
    });

    context('利用者', () => {
        beforeEach(() => {
            cy.login('U000001', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-item > :nth-child(1) > :nth-child(10) > .nav-sub-item > #side-nav-payment-nav').click();
        }

        context('入金一覧', () => {
            it('入金一覧の表示', () => {
                userPage();
                cy.get('.collection-view-container').should('be.visible');
            });
        });

        context('入金新規登録', () => {
            it('新規登録', () => {
                userPage();

                // 入金新規画面を開く
                cy.get('#new').click();

                // ヘッダ情報を入力
                cy.get('#paymentNumber').type('PA00000009');
                cy.get('#paymentDate').type('2024-01-01');
                cy.get('#departmentCode').type('10000');
                cy.get('#departmentStartDate').type('2021-01-01')
                cy.get('#customerCode').type('001');
                cy.get('#paymentMethodType').select('現金');
                cy.get('#paymentAccountCode').type('ACC001');
                cy.get('#paymentAmount').type('10000');
                cy.get('#offsetAmount').type('10000');

                // 入金を保存
                cy.get('#save').click();

                // 作成完了メッセージの確認
                cy.get('#message').contains('入金を作成しました。');
            });
        });

        context('入金検索', () => {
            it('検索', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-payment-number').type('PA00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.get(':nth-child(2) > .collection-object-item-content-name').contains('PA00000009');
            });
        });

        context('入金登録情報編集', () => {
            it('登録情報編集', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-payment-number').type('PA00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#edit').click();

                // 入金金額を更新
                cy.get('#paymentAmount').clear();
                cy.get('#paymentAmount').type('15000');

                cy.get('#save').click();
                cy.get('#message').contains('入金を更新しました。');
            });
        });

        context('入金削除', () => {
            it('削除', () => {
                userPage();
                cy.get('#search').click();
                cy.get('#search-payment-number').type('PA00000009');
                cy.wait(1000);
                cy.get('#search-all').click();
                cy.wait(1000);
                cy.get('#delete').click();
                cy.get('#message').contains('入金情報を削除しました。');
            });
        });
    });
});