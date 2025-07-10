describe('口座管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z');
    })

    const userPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(4) > :nth-child(1) > :nth-child(7) > #side-nav-account-nav').click();
    }

    context('口座一覧', () => {
        it('口座一覧の表示', () => {
            userPage();
            cy.get('.collection-view-container').should('be.visible');
        });
    });

    context('口座新規登録', () => {
        it('新規登録', () => {
            userPage();
            cy.get('#new').click();
            cy.get('#accountCode').type('99999');
            cy.get('#accountName').type('テスト口座');
            cy.get('#startDate').type('2021-01-01');
            cy.get('#endDate').type('9999-12-31');
            cy.get('#accountNameAfterStart').type('テスト口座（適用後）');
            cy.get('#accountType').select('銀行');
            cy.get('#accountNumber').type('1234567890');
            cy.get('#bankAccountType').select('普通');
            cy.get('#accountHolder').type('テスト名義人');
            cy.get('#departmentCode').type('10000');
            cy.get('#departmentStartDate').type('9999-12-31');
            cy.get('#bankCode').type('0001');
            cy.get('#branchCode').type('001');
            cy.get('#save').click();

            cy.get('#message').contains('口座を作成しました。');
        });
    });

    context('口座検索', () => {
        it('検索', () => {
            userPage();
            cy.get('#search').click();
            cy.get('#search-account-code').type('99999');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.get(':nth-child(3) > .collection-object-item-content-name').contains('テスト口座');
        });
    })

    context('口座登録情報編集', () => {
        it('登録情報編集', () => {
            userPage();
            cy.get('#search').click();
            cy.get('#search-account-code').type('99999');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#edit').click();
            cy.get('#accountName').clear();
            cy.get('#accountName').type('テスト口座更新');
            cy.get('#accountNameAfterStart').clear();
            cy.get('#accountNameAfterStart').type('テスト口座更新（適用後）');
            cy.get('#accountNumber').clear();
            cy.get('#accountNumber').type('9876543210');
            cy.get('#bankAccountType').select('当座');
            cy.get('#accountHolder').clear();
            cy.get('#accountHolder').type('テスト名義人更新');
            cy.get('#bankCode').clear();
            cy.get('#bankCode').type('0002');
            cy.get('#branchCode').clear();
            cy.get('#branchCode').type('002');
            cy.get('#save').click();

            cy.get('#message').contains('口座を更新しました。');
        });
    });

    context('口座削除', () => {
        it('削除', () => {
            userPage();
            cy.get('#search').click();
            cy.get('#search-account-code').type('99999');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.wait(1000);
            cy.get('#delete').click();
            cy.get('#message').contains('口座を削除しました。');
        })
    });
});