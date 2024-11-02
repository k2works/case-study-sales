describe('ユーザー管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z');
    })

    const userPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-sub-list > #side-nav-user-nav > a').click();
    }

    context('ユーザー一覧', () => {
        it('ユーザー一覧の表示', () => {
            userPage();
            cy.get('.collection-view-container').should('be.visible');
        });
    });

    context('ユーザー新規登録', () => {
        it('一般ユーザーの新規登録', () => {
            userPage();
            cy.get('#new').click();
            cy.get('#userId').type('U000004');
            cy.get('#firstName').type('テスト');
            cy.get('#lastName').type('太郎');
            cy.get('#roleName').select('ユーザー')
            cy.get('#password').type('a234567Z');
            cy.get('#save').click();

            cy.get('#message').contains('ユーザーを作成しました。');
        });

        it('管理ユーザーの新規登録', () => {
            userPage();
            cy.get('#new').click();
            cy.get('#userId').type('U000005');
            cy.get('#firstName').type('管理');
            cy.get('#lastName').type('太郎');
            cy.get('#roleName').select('管理者')
            cy.get('#password').type('a234567Z');
            cy.get('#save').click();

            cy.get('#message').contains('ユーザーを作成しました。');
        });

        it('未入力の項目がある状態で新規登録', () => {
            userPage();
            cy.get('#new').click();
            cy.get('#userId').type('U000004');
            cy.get('#save').click();

            cy.get('#message').contains('ユーザーID、姓、名、役割は必須項目です。');
        });

        it('登録済みのユーザーを新規登録', () => {
            userPage();
            cy.get('#new').click();
            cy.get('#userId').type('U000001');
            cy.get('#firstName').type('管理');
            cy.get('#lastName').type('太郎');
            cy.get('#roleName').select('ユーザー')
            cy.get('#password').type('a234567Z');
            cy.get('#save').click();

            cy.get('#message').contains('登録済みのユーザーです');
        });


        it('ユーザーIDの先頭をU以外で新規登録', () => {
            userPage();
            cy.get('#new').click();
            cy.get('#userId').type('X000004');
            cy.get('#firstName').type('テスト');
            cy.get('#lastName').type('太郎');
            cy.get('#roleName').select('ユーザー')
            cy.get('#password').type('a234567Z');
            cy.get('#save').click();

            cy.get('#message').contains('ユーザーIDの先頭はUから始まります');
        });

        it('ユーザーID７文字以上で新規登録', () => {
            userPage();
            cy.get('#new').click();
            cy.get('#userId').type('U1234567');
            cy.get('#firstName').type('テスト');
            cy.get('#lastName').type('太郎');
            cy.get('#roleName').select('ユーザー')
            cy.get('#password').type('a234567Z');
            cy.get('#save').click();

            cy.get('#message').contains('ユーザーIDの長さは7文字です');
        });

        it('ユーザーID７文字以下で新規登録', () => {
            userPage();
            cy.get('#new').click();
            cy.get('#userId').type('U12345');
            cy.get('#firstName').type('テスト');
            cy.get('#lastName').type('太郎');
            cy.get('#roleName').select('ユーザー')
            cy.get('#password').type('a234567Z');
            cy.get('#save').click();

            cy.get('#message').contains('ユーザーIDの長さは7文字です');
        });
    });

    context('ユーザー登録情報編集', () => {
        it('一般ユーザー登録情報編集', () => {
            userPage();
            cy.get(':nth-child(4) > :nth-child(5) > #edit').click();
            cy.get('#firstName').clear();
            cy.get('#firstName').type('テスト');
            cy.get('#lastName').clear();
            cy.get('#lastName').type('花子');
            cy.get('#save').click();

            cy.get('#message').contains('ユーザーを更新しました。');
        });

        it('管理ユーザー登録情報編集', () => {
            userPage();
            cy.get(':nth-child(5) > :nth-child(5) > #edit').click();
            cy.get('#firstName').clear();
            cy.get('#firstName').type('管理');
            cy.get('#lastName').clear();
            cy.get('#lastName').type('花子');
            cy.get('#save').click();

            cy.get('#message').contains('ユーザーを更新しました。');
        });
    });

    context('ユーザー検索', () => {
        it('一般ユーザー検索', () => {
            userPage();
            cy.get('#search-input').type('U000004');
            cy.get('#search-all').click();
            cy.get(':nth-child(2) > .collection-object-item-content-name').contains('テスト');
        });

        it('管理ユーザー検索', () => {
            userPage();
            cy.get('#search-input').type('U000005');
            cy.get('#search-all').click();
            cy.get(':nth-child(2) > .collection-object-item-content-name').contains('管理');
        });
    })

    context('ユーザー削除', () => {
        it('一般ユーザー削除', () => {
            userPage();
            cy.get(':nth-child(4) > .collection-object-item-actions > #delete').click();
            //TODO:メッセージが取得できない
            cy.get('#message').contains('ユーザーを削除しました。');
        })

        it('管理ユーザー削除', () => {
            userPage();
            cy.get(':nth-child(4) > .collection-object-item-actions > #delete').click();
            //TODO:メッセージが取得できない
            cy.get('#message').contains('ユーザーを削除しました。');
        });
    });
});
