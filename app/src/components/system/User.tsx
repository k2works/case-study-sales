import React, {useEffect, useState} from "react";
import {SiteLayout} from "../application/SiteLayout";
import ErrorBoundary from "../application/ErrorBoundary";
import {UserAccountType} from "../../types";
import {UserService} from "../../services/user";

export const User: React.FC = () => {
    const [users, setUsers] = useState<UserAccountType[]>([]);
    const [newUser, setNewUser] = useState<UserAccountType>({
        userId: {value: ""},
        name: {firstName: "", lastName: ""},
        password: {value: ""},
        roleName: ""
    });

    const userService = UserService();

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const fetchedUsers = await userService.select();
            setUsers(fetchedUsers.list);
        } catch (error) {
            console.error("ユーザー情報の取得に失敗しました:", error);
        }
    };

    const handleCreateUser = async () => {
        try {
            await userService.create(newUser);
            setNewUser({
                userId: {value: ""},
                name: {firstName: "", lastName: ""},
                password: {value: ""},
                roleName: ""
            });
            await fetchUsers();
        } catch (error) {
            console.error("ユーザーの作成に失敗しました:", error);
        }
    };

    const handleDeleteUser = async (userId: string) => {
        try {
            await userService.destroy(userId);
            await fetchUsers();
        } catch (error) {
            console.error("ユーザーの削除に失敗しました:", error);
        }
    };

    return (
        <SiteLayout>
            <ErrorBoundary>
                <div className="collection-view-object-container">
                    <div className="view-message-container" id="message"></div>
                    <div className="collection-view-container">
                        <div className="collection-view-header">
                            <div className="single-view-header-item">
                                <h1 className="single-view-title">ユーザー</h1>
                            </div>
                        </div>
                        <div className="collection-view-content">
                            <div className="search-container">
                                <input type="text" id="search-input" placeholder="絞り込み"/>
                                <button className="action-button" id="search-all">検索</button>
                            </div>
                            <div className="button-container">
                                <input
                                    type="text"
                                    placeholder="名"
                                    value={newUser.name.firstName}
                                    onChange={(e) => setNewUser({
                                        ...newUser,
                                        name: {...newUser.name, firstName: e.target.value}
                                    })}
                                />
                                <input
                                    type="text"
                                    placeholder="姓"
                                    value={newUser.name.lastName}
                                    onChange={(e) => setNewUser({
                                        ...newUser,
                                        name: {...newUser.name, lastName: e.target.value}
                                    })}
                                />
                                <input
                                    type="text"
                                    placeholder="役割"
                                    value={newUser.roleName}
                                    onChange={(e) => setNewUser({...newUser, roleName: e.target.value})}
                                />
                                <input
                                    type="password"
                                    placeholder="パスワード"
                                    value={newUser.password?.value || ""}
                                    onChange={(e) => setNewUser({...newUser, password: {value: e.target.value}})}
                                />
                                <button className="action-button" onClick={handleCreateUser}>新規</button>
                            </div>
                            <div className="collection-object-container">
                                <ul className="collection-object-list">
                                    {users.map((user) => (
                                        <li className="collection-object-item" key={user.userId.value}>
                                            <div className="collection-object-item-content" data-id={user.userId.value}>
                                                <div className="collection-object-item-content-details">名</div>
                                                <div
                                                    className="collection-object-item-content-name">{user.name.firstName}</div>
                                            </div>
                                            <div className="collection-object-item-content" data-id={user.userId.value}>
                                                <div className="collection-object-item-content-details">姓</div>
                                                <div
                                                    className="collection-object-item-content-name">{user.name.lastName}</div>
                                            </div>
                                            <div className="collection-object-item-content" data-id={user.userId.value}>
                                                <div className="collection-object-item-content-details">役割</div>
                                                <div
                                                    className="collection-object-item-content-name">{user.roleName}</div>
                                            </div>
                                            <div className="collection-object-item-actions" data-id={user.userId.value}>
                                                <button className="action-button"
                                                        onClick={() => handleDeleteUser(user.userId.value)}>削除
                                                </button>
                                            </div>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </ErrorBoundary>
        </SiteLayout>
    );
};
