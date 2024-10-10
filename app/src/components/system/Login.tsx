import React from "react";
import {useNavigate} from "react-router-dom";

export default function Login() {
    const navigate = useNavigate();
    const handleLogin = () => {
        console.log("Login");
        navigate("/");
    }

    return (
        <div className="root-container w-container">
            <div className="view-container" id="contents">
                <div className="single-view-object-container">
                    <div className="view-message-box-container" id="message"></div>
                    <div className="single-view-box-container">
                        <div className="single-view-header">
                            <div className="single-view-header-item">
                                <h1 className="single-view-title logo">SMS</h1>
                                <p className="single-view-subtitle">Sales Management System</p>
                            </div>
                            <div className="single-view-header-item">
                                <button className="action-button" id="login" onClick={handleLogin}>ログイン</button>
                            </div>
                        </div>

                        <div className="single-view-content">
                            <div className="single-view-content-item">
                                <div className="single-view-content-item-form">
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">ユーザー名</label>
                                        <input className="single-view-content-item-form-item-input" type="text"
                                               value="U000001" id="userId"/>
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">パスワード</label>
                                        <input className="single-view-content-item-form-item-input" type="password"
                                               value="a234567Z" id="password"/>
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
