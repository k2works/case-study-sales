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
                        <time>2024.12.1</time>
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