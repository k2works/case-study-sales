describe('顧客管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z'); // 必要に応じてログイン処理を実装
    });

    // 顧客管理画面を開く関数
    const openCustomerPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(1) > :nth-child(6) > :nth-child(4) > #side-nav-partner-nav').click();
    };

    context('顧客一覧', () => {
        it('顧客一覧の表示', () => {
            openCustomerPage();
            cy.get('.collection-view-container').should('be.visible');
        });
    });

    context('顧客新規登録', () => {
        it('新規登録', () => {
            openCustomerPage();

            // 新規ボタンのクリック
            cy.get('#new').click();

            // フォーム入力
            cy.get('#customerCode').type('001'); // 顧客コード
            cy.get('#customerBranchNumber').type('9'); // 顧客コード枝番

            cy.get('#customerType').select('顧客'); // 顧客区分
            cy.get('#customerName').type('テスト顧客'); // 顧客名
            cy.get('#customerRepresentativeName').type('山田太郎'); // 顧客担当者名
            cy.get('#customerDepartmentName').type('営業部'); // 顧客部門名
            cy.get('#billingCode').type('100'); // 請求先コード
            cy.get('#billingBranchNumber').type('2'); // 請求先コード枝番
            cy.get('#collectionCode').type('200'); // 回収先コード
            cy.get('#collectionBranchNumber').type('3'); // 回収先コード枝番
            cy.get('#postalCode').type('1234567'); // 郵便番号
            cy.get('#prefecture').select('東京都'); // 都道府県
            cy.get('#address1').type('新宿区西新宿'); // 住所1
            cy.get('#address2').type('2-8-1'); // 住所2
            cy.get('#customerPhoneNumber').type('03-1234-5678'); // 顧客電話番号
            cy.get('#customerFaxNumber').type('03-1234-5678'); // FAX番号
            cy.get('#customerEmailAddress').type('test@example.com'); // メールアドレス
            cy.get('#customerBillingCategory').select('都度請求'); // 顧客請求区分
            // 保存ボタンのクリック
            cy.get('#save').click();

            // 確認: 登録成功メッセージ
            cy.get('#message').contains('顧客を作成しました。');
        });
    });

    context('顧客検索', () => {
        it('検索', () => {
            openCustomerPage();
            cy.get('#search').click();
            cy.get('#search-customer-code').type('001');
            cy.get('#search-customer-name').type('テスト顧客')
            cy.get('#search-all').click();
            cy.get(':nth-child(4) > .collection-object-item-content-name').contains('テスト顧客');
        });
    });

    context('顧客登録情報編集', () => {
        it('登録情報編集', () => {
            openCustomerPage();
            cy.get('#search').click();
            cy.get('#search-customer-code').type('001');
            cy.get('#search-all').click();
            cy.get('#edit').click();
            cy.get('#customerName').clear();
            cy.get('#customerName').type('テスト顧客更新');
            cy.get('#save').click();
            cy.get('#message').contains('顧客を更新しました。');
        });
    });

    context('顧客削除', () => {
        it('削除', () => {
            openCustomerPage();
            cy.get('#search').click();
            cy.get('#search-customer-code').type('001');
            cy.get('#search-all').click();
            cy.get('#delete').click();
            cy.get('#message').contains('顧客を削除しました。');
        });
    });
});
