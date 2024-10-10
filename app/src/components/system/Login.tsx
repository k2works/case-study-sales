import React, {useState} from "react";
import {useNavigate} from "react-router-dom";

export default function Login() {
    const [userId, setUserId] = useState("U000001");
    const [password, setPassword] = useState("a234567Z");
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/auth/signin', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({userId, password}),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.error || 'Network response was not ok');
            }

            const data = await response.json();
            if (data.accessToken) {
                setMessage("ログイン成功");
                navigate('/');
            } else {
                setMessage("ログイン失敗: " + data.error);
            }
        } catch (error) {
            if (error instanceof Error) {
                setMessage("ログインに失敗しました: " + error.message);
            } else {
                setMessage("ログインに失敗しました: 不明なエラー");
            }
        }
    };

    return (
        <div className="root-container w-container">
            <div className="view-container" id="contents">
                <div className="single-view-object-container">
                    <div className="view-message-box-container" id="message">{message}</div>
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
