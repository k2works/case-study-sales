describe('仕入先管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z'); // 必要に応じてログイン処理を実装
    });
    // 仕入先管理画面を開く関数
    const openVendorPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(1) > :nth-child(6) > :nth-child(5) > #side-nav-partner-nav').click();
    };
    context('仕入先一覧', () => {
        it('仕入先一覧の表示', () => {
            openVendorPage();
            cy.get('.collection-view-container').should('be.visible');
        });
    });

    context('仕入先新規登録', () => {
        it('新規登録', () => {
            openVendorPage();
            // 新規ボタンのクリック
            cy.get('#new').click();
            // フォーム入力
            cy.get('#vendorCode').type('002'); // 仕入先コード
            cy.get('#branchNumber').type('9'); // 仕入先コード枝番
            cy.get('#vendorName').type('テスト仕入先'); // 仕入先名
            cy.get('#vendorContactName').type('山田花子'); // 担当者名
            cy.get('#vendorDepartmentName').type('購買部'); // 部門名
            cy.get('#postalCode').type('9876543'); // 郵便番号
            cy.get('#address1').type('大阪府大阪市中央区'); // 住所
            cy.get('#address2').type('1-2-3');
            cy.get('#phoneNumber').type('06-1234-5678'); // 電話番号
            cy.get('#faxNumber').type('06-1234-5679'); // FAX番号
            cy.get('#emailAddress').type('vendor@example.com'); // メールアドレス
            // 保存ボタンのクリック
            cy.get('#save').click();
            // 確認: 登録成功メッセージ
            cy.get('#message').contains('仕入先を作成しました。');
        });
    });

    context('仕入先検索', () => {
        it('検索', () => {
            openVendorPage();
            cy.get('#search').click();
            cy.get('#search-vendor-code').type('002');
            cy.get('#search-vendor-name').type('テスト仕入先');
            cy.get('#search-all').click();
            cy.get(':nth-child(4) > .collection-object-item-content-name').contains('テスト仕入先');
        });
    });

    context('仕入先登録情報編集', () => {
        it('登録情報編集', () => {
            openVendorPage();
            cy.get('#search').click();
            cy.get('#search-vendor-code').type('002');
            cy.get('#search-all').click();
            cy.get('#edit').click();
            cy.get('#vendorName').clear();
            cy.get('#vendorName').type('テスト仕入先更新');
            cy.get('#save').click();
            cy.get('#message').contains('仕入先を更新しました。');
        });
    });

    context('仕入先削除', () => {
        it('削除', () => {
            openVendorPage();
            cy.get('#search').click();
            cy.get('#search-vendor-code').type('002');
            cy.wait(1000);
            cy.get('#search-all').click();
            cy.get('#delete').click();
            cy.get('#message').contains('仕入先を削除しました。');
        });
    });
});