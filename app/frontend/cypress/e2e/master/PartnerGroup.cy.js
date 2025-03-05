describe('取引先グループ管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z'); // 必要に応じてログイン処理を実装
    });

    // 取引先グループ管理画面を開く関数
    const openPartnerGroupPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(4) > :nth-child(1) > :nth-child(6) > :nth-child(2) > #side-nav-partner-nav').click();
    };

    context('取引先グループ一覧', () => {
        it('取引先グループ一覧の表示', () => {
            openPartnerGroupPage();
            cy.get('.collection-view-container').should('be.visible');
        });
    });

    context('取引先グループ新規登録', () => {
        it('新規登録', () => {
            openPartnerGroupPage();
            cy.get('#new').click();
            cy.get('#partnerGroupCode').type('9999');
            cy.get('#partnerGroupName').type('テスト取引先グループ');
            cy.get('#save').click();
            cy.get('#message').contains('取引先グループを作成しました。');
        });
    });

    context('取引先グループ検索', () => {
        it('検索', () => {
            openPartnerGroupPage();
            cy.get('#search').click();
            cy.get('#search-partner-group-code').type('9999');
            cy.get('#search-all').click();
            cy.get(':nth-child(3) > .collection-object-item-content-name').contains('テスト取引先グループ');
        });
    });

    context('取引先グループ登録情報編集', () => {
        it('登録情報編集', () => {
            openPartnerGroupPage();
            cy.get('#search').click();
            cy.get('#search-partner-group-code').type('9999');
            cy.get('#search-all').click();
            cy.get('#edit').click();
            cy.get('#partnerGroupName').clear();
            cy.get('#partnerGroupName').type('テスト取引先グループ更新');
            cy.get('#save').click();
            cy.get('#message').contains('取引先グループを更新しました。');
        });
    });

    context('取引先グループ削除', () => {
        it('削除', () => {
            openPartnerGroupPage();
            cy.get('#search').click();
            cy.get('#search-partner-group-code').type('9999');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.get('#delete').click();
            cy.get('#message').contains('取引先グループを削除しました。');
        });
    });
});