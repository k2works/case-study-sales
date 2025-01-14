describe('商品管理', () => {
    beforeEach(() => {
        cy.login('U000003', 'a234567Z');
    });

    const openProductPage = () => {
        cy.get('#side-nav-menu > :nth-child(1) > :nth-child(3) > :nth-child(1) > .nav-sub-list > :nth-child(2) > #side-nav-product-nav').click();
    };

    context('商品一覧', () => {
        it('商品一覧の表示', () => {
            openProductPage();
            cy.get('.collection-view-container').should('be.visible');
        });
    });

    context('商品新規登録', () => {
        it('新規登録', () => {
            openProductPage();
            cy.get('#new').click();

            // 各フォーム項目に対して入力設定
            cy.get('#productCode').type('99999001');
            cy.get('#productFormalName').type('テスト商品正式名');
            cy.get('#productAbbreviation').type('テスト商品略称');
            cy.get('#productNameKana').type('テストショウヒン');
            cy.get('#productType').select('商品');
            cy.get('#sellingPrice').type('1000');
            cy.get('#costOfSales').type('500');
            cy.get('#taxType').select('外税');
            cy.get('#productCategoryCode').type('C99999');
            cy.get('#miscellaneousType').select('適用');
            cy.get('#stockManagementTargetType').select('対象');
            cy.get('#stockAllocationType').select('未引当');
            cy.get('#supplierCode').type('999');

            // 登録ボタンをクリック
            cy.get('#save').click();

            // メッセージを確認
            cy.get('#message').contains('商品を作成しました。');
        });
    });

    context('商品検索', () => {
        it('検索', () => {
            openProductPage();
            cy.get('#search').click();
            cy.get('#productCode').type('99999001');
            cy.get('#search-all').click();
            cy.get('.collection-object-item-content-name').contains('テスト商品正式名');
        });
    });

    context('商品登録情報編集', () => {
        it('登録情報編集', () => {
            openProductPage();
            cy.get('#search').click();
            cy.get('#productCode').type('99999001');
            cy.get('#search-all').click();
            cy.get('#edit').click();

            // 各フォーム項目を更新
            cy.get('#productFormalName').clear().type('テスト商品正式名更新');
            cy.get('#productAbbreviation').clear().type('テスト商品略称更新');
            cy.get('#productNameKana').clear().type('テストショウヒン更新');
            cy.get('#productType').select('その他');
            cy.get('#sellingPrice').clear().type('1200');
            cy.get('#costOfSales').clear().type('600');
            cy.get('#taxType').select('その他');
            cy.get('#miscellaneousType').select('適用外');
            cy.get('#stockManagementTargetType').select('対象外');
            cy.get('#stockAllocationType').select('引当済');
            cy.get('#supplierCode').clear().type('998');

            // 保存ボタンをクリック
            cy.get('#save').click();

            // メッセージを確認
            cy.get('#message').contains('商品を更新しました。');
        });
    });

    context('商品削除', () => {
        it('削除', () => {
            openProductPage();
            cy.get('#search').click();
            cy.get('#productCode').type('99999001');
            cy.get('#search-all').click();
            cy.get('#delete').click();
            cy.get('#message').contains('商品を削除しました。');
        });
    });
});