describe('社員管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z');
    })

    const userPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > .nav-sub-list > :nth-child(2) > a').click();
    }

    context('社員一覧', () => {
        it('社員一覧の表示', () => {
            userPage();
            cy.get('.collection-view-container').should('be.visible');
        });
    });

    context('社員新規登録', () => {
        it('新規登録', () => {
            userPage();
            cy.get('#new').click();
            cy.get('#empCode').type('EMP999');
            cy.get('#firstName').type('山田');
            cy.get('#lastName').type('太郎');
            cy.get('#firstNameKana').type('ヤマダ');
            cy.get('#lastNameKana').type('タロウ');
            cy.get('#tel').type('090-1234-5678');
            cy.get('#fax').type('090-1234-5678');
            cy.get('#select-department').click();
            cy.get('.collection-object-container-modal > .collection-object-list > :nth-child(1) > .collection-object-item-actions > .action-button').click();
            cy.get('#save').click();

            cy.get('#message').contains('社員を作成しました。');
        });
    });

    context('社員検索', () => {
        it('検索', () => {
            userPage();
            cy.get('#search-input').type('EMP999');
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get(':nth-child(3) > .collection-object-item-content-name').contains('山田');
        });
    })

    context('社員登録情報編集', () => {
        it('登録情報編集', () => {
            userPage();
            cy.get('#search-input').type('EMP999');
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#edit').click();
            cy.get('#firstName').clear();
            cy.get('#firstName').type('鈴木');
            cy.get('#lastName').clear();
            cy.get('#lastName').type('次郎');
            cy.get('#firstNameKana').clear();
            cy.get('#firstNameKana').type('スズキ');
            cy.get('#lastNameKana').clear();
            cy.get('#lastNameKana').type('ジロウ');
            cy.get('#tel').clear();
            cy.get('#tel').type('09-9999-9999');
            cy.get('#fax').clear();
            cy.get('#fax').type('00-0000-0000');
            cy.get('#select-department').click();
            cy.get('.collection-object-container-modal > .collection-object-list > :nth-child(2) > .collection-object-item-actions > .action-button').click();
            cy.get('#save').click();

            cy.get('#message').contains('社員を更新しました。');
        });
    });

    context('社員削除', () => {
        it('削除', () => {
            userPage();
            cy.get('#search-input').type('EMP999');
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#delete').click();
            cy.get('#message').contains('社員を削除しました。');
        })
    });
});
