import React from 'react';

const HomeSingleView: React.FC = () => {
    return (
        <div className="single-view-container">
            <div className="single-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">HOME</h1>
                    <p className="single-view-subtitle">お知らせ</p>
                </div>
            </div>
            <div className="single-view-content">
                <dl className="info">
                    <dt>
                        <span>新機能</span>
                        <time>2025.1.1</time>
                    </dt>
                    <dd>
                        <a href="#" id="auth-func">実行履歴機能リリース</a>
                    </dd>
                    <dd>
                        <a href="#" id="auth-func">ダウンロード機能リリース</a>
                    </dd>
                    <dd>
                        <a href="#" id="auth-func">マスタ検索機能追加</a>
                    </dd>
                    <dt>
                        <span>新機能</span>
                        <time>2024.11.1</time>
                    </dt>
                    <dd>
                        <a href="#" id="auth-func">部門マスタ機能リリース</a>
                    </dd>
                    <dd>
                        <a href="#" id="auth-func">社員マスタ機能リリース</a>
                    </dd>
                    <dd>
                        <a href="#" id="auth-func">商品マスタ機能リリース</a>
                    </dd>
                    <dt>
                        <span>新機能</span>
                        <time>2024.10.1</time>
                    </dt>
                    <dd>
                        <a href="#" id="auth-func">認証機能リリース</a>
                    </dd>
                </dl>
            </div>
        </div>
    );
};

export default HomeSingleView;
