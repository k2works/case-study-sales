import React from 'react';

const ErrorMessage = ({message}: { message: string }) => (
        <div className="view-message-content">
            <div className="view-message-content-icon">
                <i className="fas fa-exclamation-circle"></i>
            </div>
            <div className="view-message-content-error-text">
                <p className="view-message-content-text-title">ログインに失敗しました</p>
                <p className="view-message-content-text-subtitle">{message}</p>
            </div>
        </div>
    )
;

interface HeaderProps {
    title: string;
    subtitle: string;
    onSignIn: () => void;
}

const Header = ({title, subtitle, onSignIn}: HeaderProps) => (
    <div className="single-view-header">
        <div className="single-view-header-item">
            <h1 className="single-view-title logo">{title}</h1>
            <p className="single-view-subtitle">{subtitle}</p>
        </div>
        <div className="single-view-header-item">
            <button className="action-button" id="login" onClick={onSignIn}>ログイン</button>
        </div>
    </div>
);

interface LoginFormProps {
    userId: string;
    setUserId: (userId: string) => void;
    password: string;
    setPassword: (password: string) => void;
}

const LoginForm = ({userId, setUserId, password, setPassword}: LoginFormProps) => (
    <div className="single-view-content-item-form">
        <div className="single-view-content-item-form-item">
            <label className="single-view-content-item-form-item-label">ユーザー名</label>
            <input
                className="single-view-content-item-form-item-input"
                type="text"
                value={userId}
                onChange={(e) => setUserId(e.target.value)}
                id="userId"
            />
        </div>
        <div className="single-view-content-item-form-item">
            <label className="single-view-content-item-form-item-label">パスワード</label>
            <input
                className="single-view-content-item-form-item-input"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                id="password"
            />
        </div>
    </div>
);

interface LoginSingleViewProps {
    message: string;
    handleSignIn: () => void;
    userId: string;
    setUserId: (userId: string) => void;
    password: string;
    setPassword: (password: string) => void;
}

export const LoginSingleView = ({
                                    message,
                                    handleSignIn,
                                    userId,
                                    setUserId,
                                    password,
                                    setPassword
                                }: LoginSingleViewProps) => {
    return (
        <div className="root-container w-container">
            <div className="view-container" id="contents">
                <div className="single-view-object-container">
                    <div className="view-message-box-container" id="message">
                        {message && <ErrorMessage message={message}/>}
                    </div>
                    <div className="single-view-box-container">
                        <Header
                            title="SMS"
                            subtitle="Sales Management System"
                            onSignIn={handleSignIn}
                        />
                        <div className="single-view-content">
                            <div className="single-view-content-item">
                                <LoginForm
                                    userId={userId}
                                    setUserId={setUserId}
                                    password={password}
                                    setPassword={setPassword}
                                />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};
