describe('アプリケーションデータダウンロード', () => {
    describe('管理者', () => {
        beforeEach(() => {
            cy.login('U000003', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-sub-list > .nav-sub-item > #side-nav-download-nav').click();
        }

        it('ダウンロード画面の表示', () => {
            userPage();
            cy.get('.single-view-container').should('be.visible');
        });

        it('部門データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('部門');
            cy.get('#download').click();
            cy.get('#message').contains('部門 データをダウンロードしました。');
        });

        it('社員データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('社員');
            cy.get('#download').click();
            cy.get('#message').contains('社員 データをダウンロードしました。');
        });

        it('商品分類データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('商品分類');
            cy.get('#download').click();
            cy.get('#message').contains('商品分類 データをダウンロードしました。');
        });

        it('商品データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('商品');
            cy.get('#download').click();
            cy.get('#message').contains('商品 データをダウンロードしました。');
        });

        it('取引先グループダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('取引先グループ');
            cy.get('#download').click();
            cy.get('#message').contains('取引先グループ データをダウンロードしました。');
        });

        it('取引先データダウンロード',  () => {
            userPage();
            cy.get('#downloadTarget').select('取引先');
            cy.get('#download').click();
            cy.get('#message').contains('取引先 データをダウンロードしました。');
        });

        it('顧客データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('顧客');
            cy.get('#download').click();
            cy.get('#message').contains('顧客 データをダウンロードしました。');
        });

        it('仕入先データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('仕入先');
            cy.get('#download').click();
            cy.get('#message').contains('仕入先 データをダウンロードしました。');
        });

        it('受注データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('受注');
            cy.get('#download').click();
            cy.get('#message').contains('受注 データをダウンロードしました。');
        });

        it('出荷データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('出荷');
            cy.get('#download').click();
            cy.get('#message').contains('出荷 データをダウンロードしました。');
        });

        it('売上データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('売上');
            cy.get('#download').click();
            cy.get('#message').contains('売上 データをダウンロードしました。');
        });
    });

    describe('利用者', () => {
        beforeEach(() => {
            cy.login('U000001', 'a234567Z');
        })

        const userPage = () => {
            cy.get('#side-nav-menu > :nth-child(1) > :nth-child(2) > .nav-sub-list > .nav-sub-item > #side-nav-download-nav').click();
        }

        it('ダウンロード画面の表示', () => {
            userPage();
            cy.get('.single-view-container').should('be.visible');
        });

        it('部門データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('部門');
            cy.get('#download').click();
            cy.get('#message').contains('ダウンロードに失敗しました: 権限がありません');
        });

        it('社員データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('社員');
            cy.get('#download').click();
            cy.get('#message').contains('ダウンロードに失敗しました: 権限がありません');
        });

        it('商品分類データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('商品分類');
            cy.get('#download').click();
            cy.get('#message').contains('ダウンロードに失敗しました: 権限がありません');
        });

        it('商品データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('商品');
            cy.get('#download').click();
            cy.get('#message').contains('ダウンロードに失敗しました: 権限がありません');
        });

        it('取引先グループダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('取引先グループ');
            cy.get('#download').click();
            cy.get('#message').contains('ダウンロードに失敗しました: 権限がありません');
        });

        it('取引先データダウンロード',  () => {
            userPage();
            cy.get('#downloadTarget').select('取引先');
            cy.get('#download').click();
            cy.get('#message').contains('ダウンロードに失敗しました: 権限がありません');
        });

        it('顧客データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('顧客');
            cy.get('#download').click();
            cy.get('#message').contains('ダウンロードに失敗しました: 権限がありません');
        });

        it('仕入先データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('仕入先');
            cy.get('#download').click();
            cy.get('#message').contains('ダウンロードに失敗しました: 権限がありません');
        });

        it('受注データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('受注');
            cy.get('#download').click();
            cy.get('#message').contains('受注 データをダウンロードしました。');
        });

        it('出荷データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('出荷');
            cy.get('#download').click();
            cy.get('#message').contains('出荷 データをダウンロードしました。');
        });

        it('売上データダウンロード', () => {
            userPage();
            cy.get('#downloadTarget').select('売上');
            cy.get('#download').click();
            cy.get('#message').contains('売上 データをダウンロードしました。');
        });
    });
});
