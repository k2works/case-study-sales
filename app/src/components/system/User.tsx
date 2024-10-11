import React, {useEffect, useState} from "react";
import Modal from 'react-modal';
import {SiteLayout} from "../application/SiteLayout";
import ErrorBoundary from "../application/ErrorBoundary";
import {UserAccountType} from "../../types";
import {UserService} from "../../services/user";
import {ErrorScreen} from "../application/ErrorScreen";

Modal.setAppElement('#root');

export const User: React.FC = () => {
    const [error, setError] = useState<string>("");
    const [users, setUsers] = useState<UserAccountType[]>([]);
    const [newUser, setNewUser] = useState<UserAccountType>({
        userId: {value: ""},
        name: {firstName: "", lastName: ""},
        password: {value: ""},
        roleName: ""
    });
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false);
    const [isEditing, setIsEditing] = useState<boolean>(false);
    const [editUserId, setEditUserId] = useState<string | null>(null);

    const userService = UserService();

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const fetchedUsers = await userService.select();
            setUsers(fetchedUsers);
            setError("");
        } catch (error: any) {
            console.error("ユーザー情報の取得に失敗しました:", error);
            setError(`ユーザー情報の取得に失敗しました: ${error?.message}`);
        }
    };

    const handleOpenModal = (user?: UserAccountType) => {
        if (user) {
            setNewUser(user);
            setIsEditing(true);
        } else {
            setNewUser({
                userId: {value: ""},
                name: {firstName: "", lastName: ""},
                password: {value: ""},
                roleName: ""
            });
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleCloseModal = () => {
        setModalIsOpen(false);
        setEditUserId(null);
    };

    const handleCreateOrUpdateUser = async () => {
        try {
            if (isEditing && editUserId) {
                //await userService.update(editUserId, newUser);
            } else {
                await userService.create(newUser);
            }
            setNewUser({
                userId: {value: ""},
                name: {firstName: "", lastName: ""},
                password: {value: ""},
                roleName: ""
            });
            await fetchUsers();
            handleCloseModal();
        } catch (error: any) {
            console.error("ユーザーの保存に失敗しました:", error);
            setError(`ユーザーの保存に失敗しました: ${error?.message}`);
        }
    };

    const handleDeleteUser = async (userId: string) => {
        try {
            await userService.destroy(userId);
            await fetchUsers();
        } catch (error: any) {
            console.error("ユーザーの削除に失敗しました:", error);
            setError(`ユーザーの削除に失敗しました: ${error?.message}`);
        }
    };

    const ErrorMessage = () => {
        if (error) {
            return <ErrorScreen error={{message: error}}/>;
        }
        return null;
    }

    return (
        <SiteLayout>
            {!modalIsOpen && (
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
                                <button className="action-button" onClick={() => handleOpenModal()}>新規</button>
                            </div>
                            <div className="collection-object-container">
                                <ul className="collection-object-list">
                                    {users.map((user) => (
                                        <li className="collection-object-item" key={user.userId.value}>
                                            <div className="collection-object-item-content" data-id={user.userId.value}>
                                                <div className="collection-object-item-content-details">ユーザーID</div>
                                                <div
                                                    className="collection-object-item-content-name">{user.userId.value}</div>
                                            </div>
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
                                                        onClick={() => handleOpenModal(user)}>編集
                                                </button>
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
            )}
            <Modal
                isOpen={modalIsOpen}
                onRequestClose={handleCloseModal}
                contentLabel="ユーザー情報を入力"
                className="modal"
                overlayClassName="modal-overlay"
                bodyOpenClassName="modal-open"
            >
                <ErrorBoundary>
                    <ErrorMessage/>
                </ErrorBoundary>
                <h2>{isEditing ? "ユーザー編集" : "新規ユーザー作成"}</h2>
                <form>
                    <input
                        type="text"
                        placeholder="ユーザーID"
                        value={newUser.userId.value}
                        onChange={(e) => setNewUser({...newUser, userId: {value: e.target.value}})}
                    />
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
                </form>
                <button className="action-button"
                        onClick={handleCreateOrUpdateUser}>{isEditing ? "更新" : "作成"}</button>
                <button className="action-button" onClick={handleCloseModal}>キャンセル</button>
            </Modal>
        </SiteLayout>
    );
};
