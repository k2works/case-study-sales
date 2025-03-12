describe('取引先管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z'); // 必要に応じてログイン処理を実装
    });

    // 取引先管理画面を開く関数
    const openPartnerPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(4) > :nth-child(1) > :nth-child(6) > :nth-child(3) > #side-nav-partner-nav').click();
    };

    context('取引先一覧', () => {
        it('取引先一覧の表示', () => {
            openPartnerPage();
            cy.get('.collection-view-container').should('be.visible');
        });
    });

    context('取引先新規登録', () => {
        it('新規登録', () => {
            openPartnerPage();

            // 「新規登録」ボタンをクリック
            cy.get('#new').click();

            // 各フィールドにデータを入力
            cy.get('#partnerCode').type('999'); // 取引先コード
            cy.get('#partnerName').type('テスト取引先'); // 取引先名
            cy.get('#partnerNameKana').type('テストトリヒキサキ'); // 取引先名カナ
            cy.get('#vendorType').select('仕入先'); // 仕入先区分

            cy.get('#postalCode').type('1234567'); // 郵便番号
            cy.get('#prefecture').select('東京都'); // 都道府県
            cy.get('#address1').type('新宿区西新宿2-8-1'); // 住所1
            cy.get('#address2').type('第二オフィスビル'); // 住所2

            cy.get('#tradeProhibitedFlag').select('OFF'); // 取引禁止フラグ
            cy.get('#miscellaneousType').select('対象外'); // 雑区分

            cy.get('#partnerGroupCode').type('0001'); // 取引先グループコード
            cy.get('#creditLimit').type('5000000'); // 与信限度額
            cy.get('#temporaryCreditIncrease').type('1000000'); // 与信一時増加枠

            cy.get('#select-partner-group').click();
            cy.get('.collection-object-container-modal > .collection-object-list > :nth-child(1) > .collection-object-item-actions > .action-button').click();

            // 「保存」ボタンをクリック
            cy.get('#save').click();

            // 登録成功メッセージの確認
            cy.get('#message').contains('取引先を作成しました。');
        });
    });

    context('取引先検索', () => {
        it('検索', () => {
            openPartnerPage();
            cy.get('#search').click();
            cy.get('#search-partner-code').type('999');
            cy.get('#search-all').click();
            cy.get(':nth-child(3) > .collection-object-item-content-name').contains('テスト取引先');
        });
    });

    context('取引先登録情報編集', () => {
        it('登録情報編集', () => {
            openPartnerPage();
            cy.get('#search').click();
            cy.get('#search-partner-code').type('999');
            cy.get('#search-all').click();
            cy.get('#edit').click();
            cy.get('#partnerName').clear();
            cy.get('#partnerName').type('テスト取引先更新');
            cy.get('#save').click();
            cy.get('#message').contains('取引先を更新しました。');
        });
    });

    context('取引先削除', () => {
        it('削除', () => {
            openPartnerPage();
            cy.get('#search').click();
            cy.get('#search-partner-code').type('999');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.get('#delete').click();
            cy.get('#message').contains('取引先を削除しました。');
        });
    });
});
