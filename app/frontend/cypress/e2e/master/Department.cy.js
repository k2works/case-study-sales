describe('部門管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z');
    })

    const userPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(4) > .nav-sub-list > :nth-child(1) > #side-nav-department-nav').click();
    }

    context('部門一覧', () => {
        it('部門一覧の表示', () => {
            userPage();
            cy.get('.collection-view-container').should('be.visible');
        });
    });

    context('部門新規登録', () => {
        it('新規登録', () => {
            userPage();
            cy.get('#new').click();
            cy.get('#deptCode').type('99999');
            cy.get('#deptName').type('テスト');
            cy.get('#startDate').type('2021-01-01');
            cy.get('#endDate').type('9999-12-31');
            cy.get('#layer').type('0');
            cy.get('#path').type('99999~');
            cy.get(':nth-child(7) > .single-view-content-item-form-radios > :nth-child(1)').click();
            cy.get(':nth-child(8) > .single-view-content-item-form-radios > :nth-child(1)').click();
            cy.get('#save').click();

            cy.get('#message').contains('部門を作成しました。');
        });
    });

    context('部門検索', () => {
        it('検索', () => {
            userPage();
            cy.get('#search').click();
            cy.get('#search-department-code').type('99999');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.get(':nth-child(3) > .collection-object-item-content-name').contains('テスト');
        });
    })

    context('部門登録情報編集', () => {
        it('登録情報編集', () => {
            userPage();
            cy.get('#search').click();
            cy.get('#search-department-code').type('99999');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#edit').click();
            cy.get('#deptName').clear();
            cy.get('#deptName').type('テスト更新');
            cy.get('#layer').type('9');
            cy.get('#path').type('99999~99999~');
            cy.get(':nth-child(7) > .single-view-content-item-form-radios > :nth-child(2)').click();
            cy.get(':nth-child(8) > .single-view-content-item-form-radios > :nth-child(2)').click();
            cy.get('#add').click();
            cy.get(':nth-child(1) > .collection-object-item-actions > #select-employee').click();
            cy.get('.close-modal-button').click();
            cy.get('#save').click();

            cy.get('#message').contains('部門を更新しました。');
        });
    });

    context('部門削除', () => {
        it('削除', () => {
            userPage();
            cy.get('#search').click();
            cy.get('#search-department-code').type('99999');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#delete').click();
            cy.get('#message').contains('部門を削除しました。');
        })
    });
});
