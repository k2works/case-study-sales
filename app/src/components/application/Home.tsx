import React from "react";
import SiteLayout from "./SiteLayout";
import ErrorBoundary from "./ErrorBoundary";
import Navigation from "./Navigation";

export default function Home() {
    return (
        <SiteLayout menu={
            <Navigation/>
        }>
            <ErrorBoundary>
                <div className="single-view-container">
                    <div className="single-view-header">
                        <div className="single-view-header-item">
                            <h1 className="single-view-title">HOME</h1>
                            <p className="single-view-subtitle">お知らせ</p>
                        </div>

                    </div>

                    <div className="single-view-content">

                        <dl className="info">
                            <dt><span>新機能</span>
                                <time>2024.11.1</time>
                            </dt>
                            <dd>
                                <a href="#" id="auth-func">認証機能リリース</a>
                            </dd>
                        </dl>

                    </div>
                </div>
            </ErrorBoundary>
        </SiteLayout>
    )
}
