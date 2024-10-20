import React, {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {CustomLocation, DataType, RoleType, UserType} from "../../types";
import {AuthUserContextType, useAuthUserContext} from "../../providers/AuthUser";
import AuthService from "../../services/auth";

const DEFAULT_USER_ID = "U000001";
const DEFAULT_PASSWORD = "a234567Z";

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

export const Login: React.FC = () => {
    const [userId, setUserId] = useState(DEFAULT_USER_ID);
    const [password, setPassword] = useState(DEFAULT_PASSWORD);
    const [message, setMessage] = useState("");
    const navigate = useNavigate();
    const location: CustomLocation = useLocation() as CustomLocation;
    const fromPathName: string = location.state?.from?.pathname || "/";
    const authUser: AuthUserContextType = useAuthUserContext();

    useEffect(() => {
        if (authUser.isLogin()) {
            navigate("/", {replace: true});
        }
    }, [authUser, navigate]);

    const handleSignIn = async () => {
        const authService = AuthService();
        try {
            const result = await authService.signIn(userId, password);
            if (result.message) {
                setMessage(result.message);
                return;
            }
            const data: DataType = result as DataType;
            const user: UserType = {
                userId: data.userId,
                token: data.accessToken,
                roles: data.roles as RoleType[],
            };
            authUser.signIn(user, () => {
                navigate(fromPathName, {replace: true});
            });
        } catch (e: any) {
            setMessage(e.message);
        }
    };

    return (
        <div className="root-container w-container">
            <div className="view-container" id="contents">
                <div className="single-view-object-container">
                    <div className="view-message-box-container" id="message">
                        {message && <ErrorMessage message={message}/>}
                    </div>
                    <div className="single-view-box-container">
                        <div className="single-view-header">
                            <div className="single-view-header-item">
                                <h1 className="single-view-title logo">SMS</h1>
                                <p className="single-view-subtitle">Sales Management System</p>
                            </div>
                            <div className="single-view-header-item">
                                <button className="action-button" id="login" onClick={handleSignIn}>ログイン</button>
                            </div>
                        </div>
                        <div className="single-view-content">
                            <div className="single-view-content-item">
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
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
