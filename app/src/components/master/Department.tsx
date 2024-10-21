import React, {useEffect, useState} from "react";
import {Message, useMessage} from "../application/Message.tsx";
import {PageNation, usePageNation} from "../application/PageNation.tsx";
import {useModal} from "../application/hooks.ts";
import {useUser} from "../system/hooks.ts";
import {showErrorMessage} from "../application/utils.ts";
import {UserAccountType} from "../../types";
import Modal from "react-modal";
import BeatLoader from "react-spinners/BeatLoader";
import {SiteLayout} from "../application/SiteLayout.tsx";

export const Department: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const {message, setMessage, error, setError} = useMessage();
        const {pageNation, setPageNation} = usePageNation();
        const {modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId} = useModal();
        const {
            initialUser,
            users,
            setUsers,
            newUser,
            setNewUser,
            searchUserId,
            setSearchUserId,
            userService
        } = useUser();

        useEffect(() => {
            fetchDepartments().then(() => {
            });
        }, []);

        const fetchDepartments = async (page: number = 1) => {
            setLoading(true);
            try {
                const fetchedUsers = await userService.select(page);
                setUsers(fetchedUsers.list);
                setPageNation({...fetchedUsers});
                setError("");
            } catch (error: any) {
                showErrorMessage(`ユーザー情報の取得に失敗しました: ${error?.message}`, setError);
            } finally {
                setLoading(false);
            }
        };

        const handleOpenModal = (user?: UserAccountType) => {
            setMessage("");
            setError("");
            if (user) {
                user.password = {value: ""};
                setNewUser(user);
                setEditId(user.userId.value);
                setIsEditing(true);
            } else {
                setNewUser(initialUser);
                setIsEditing(false);
            }
            setModalIsOpen(true);
        };

        const handleCloseModal = () => {
            setError("");
            setModalIsOpen(false);
            setEditId(null);
        };

        const collectionView = () => {
            const handleSearchUser = async () => {
                if (!searchUserId.trim()) {
                    return;
                }
                setLoading(true);
                try {
                    const fetchedUser = await userService.find(searchUserId.trim());
                    setUsers(fetchedUser ? [fetchedUser] : []);
                    setMessage("");
                    setError("");
                } catch (error: any) {
                    showErrorMessage(`ユーザーの検索に失敗しました: ${error?.message}`, setError);
                } finally {
                    setLoading(false);
                }
            };

            const handleDeleteUser = async (userId: string) => {
                try {
                    await userService.destroy(userId);
                    await fetchDepartments();
                    setMessage("ユーザーを削除しました。");
                } catch (error: any) {
                    showErrorMessage(`ユーザーの削除に失敗しました: ${error?.message}`, setError);
                }
            };

            return (
                <div className="collection-view-object-container">
                    <div className="view-message-container" id="message">
                        <Message error={error} message={message}/>
                    </div>
                    <div className="collection-view-container">
                        <div className="collection-view-header">
                            <div className="single-view-header-item">
                                <h1 className="single-view-title">部門</h1>
                            </div>
                        </div>
                        <div className="collection-view-content">
                            <div className="search-container">
                                <input type="text" id="search-input" placeholder="ユーザーIDで検索"
                                       value={searchUserId} onChange={(e) => setSearchUserId(e.target.value)}/>
                                <button className="action-button" id="search-all"
                                        onClick={handleSearchUser}>検索
                                </button>
                            </div>
                            <div className="button-container">
                                <button className="action-button" onClick={() => handleOpenModal()}>新規
                                </button>
                            </div>
                            <div className="collection-object-container">
                                <ul className="collection-object-list">
                                    {users.map((user) => (
                                        <li className="collection-object-item" key={user.userId.value}>
                                            <div className="collection-object-item-content"
                                                 data-id={user.userId.value}>
                                                <div
                                                    className="collection-object-item-content-details">ユーザーID
                                                </div>
                                                <div
                                                    className="collection-object-item-content-name">{user.userId.value}</div>
                                            </div>
                                            <div className="collection-object-item-content"
                                                 data-id={user.userId.value}>
                                                <div className="collection-object-item-content-details">名</div>
                                                <div
                                                    className="collection-object-item-content-name">{user.name.firstName}</div>
                                            </div>
                                            <div className="collection-object-item-content"
                                                 data-id={user.userId.value}>
                                                <div className="collection-object-item-content-details">姓</div>
                                                <div
                                                    className="collection-object-item-content-name">{user.name.lastName}</div>
                                            </div>
                                            <div className="collection-object-item-content"
                                                 data-id={user.userId.value}>
                                                <div className="collection-object-item-content-details">役割
                                                </div>
                                                <div
                                                    className="collection-object-item-content-name">{user.roleName}</div>
                                            </div>
                                            <div className="collection-object-item-actions"
                                                 data-id={user.userId.value}>
                                                <button className="action-button"
                                                        onClick={() => handleOpenModal(user)}>編集
                                                </button>
                                            </div>
                                            <div className="collection-object-item-actions"
                                                 data-id={user.userId.value}>
                                                <button className="action-button"
                                                        onClick={() => handleDeleteUser(user.userId.value)}>削除
                                                </button>
                                            </div>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                            <PageNation
                                pageNation={pageNation}
                                callBack={fetchDepartments}
                            />
                        </div>
                    </div>
                </div>
            )
        }

        const singleView = () => {
            const handleCreateOrUpdateUser = async () => {
                const validateUser = (): boolean => {
                    if (!newUser.userId.value.trim() || !newUser.name?.firstName?.trim() || !newUser.name?.lastName?.trim() || !newUser.roleName?.trim()) {
                        setError("ユーザーID、姓、名、役割は必須項目です。");
                        return false;
                    }
                    return true;
                };

                if (!validateUser()) {
                    return;
                }
                try {
                    if (isEditing && editId) {
                        await userService.update(newUser);
                    } else {
                        await userService.create(newUser);
                    }
                    setNewUser({
                        userId: {value: ""},
                        name: {firstName: "", lastName: ""},
                        password: {value: ""},
                        roleName: ""
                    });
                    await fetchDepartments();
                    setMessage("ユーザーを保存しました。");
                    handleCloseModal();
                } catch (error: any) {
                    showErrorMessage(`ユーザーの保存に失敗しました: ${error?.message}`, setError);
                }
            };
            return (
                <div className="single-view-object-container">
                    <div className="view-message-container" id="message">
                        <Message error={error} message={message}/>
                    </div>
                    <div className="single-view-container">
                        <div className="single-view-header">
                            <div className="single-view-header-item">
                                <h1 className="single-view-title">ユーザー</h1>
                                <p className="single-view-subtitle">{isEditing ? "編集" : "新規作成"}</p>
                            </div>
                            <div className="collection-object-item-actions">
                                <div className="button-container">
                                    <button className="action-button"
                                            onClick={handleCreateOrUpdateUser}>{isEditing ? "更新" : "作成"}</button>
                                    <button className="action-button" onClick={handleCloseModal}>キャンセル
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div className="single-view-content">
                            <div className="single-view-content-item">
                                <div className="single-view-content-item-form">
                                    <div className="single-view-content-item-form-item">
                                        <label
                                            className="single-view-content-item-form-item-label">ユーザーID</label>
                                        <input
                                            type="text"
                                            className="single-view-content-item-form-item-input"
                                            placeholder="ユーザーID"
                                            value={newUser.userId.value}
                                            onChange={(e) => setNewUser({
                                                ...newUser,
                                                userId: {value: e.target.value}
                                            })}
                                            disabled={isEditing}
                                        />
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">姓</label>
                                        <input
                                            type="text"
                                            className="single-view-content-item-form-item-input"
                                            placeholder="姓"
                                            value={newUser.name?.lastName || ""}
                                            onChange={(e) => setNewUser({
                                                ...newUser,
                                                name: {...newUser.name, lastName: e.target.value}
                                            })}
                                        />
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">名</label>
                                        <input
                                            type="text"
                                            className="single-view-content-item-form-item-input"
                                            placeholder="名"
                                            value={newUser.name?.firstName || ""}
                                            onChange={(e) => setNewUser({
                                                ...newUser,
                                                name: {...newUser.name, firstName: e.target.value}
                                            })}
                                        />
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label className="single-view-content-item-form-item-label">役割</label>
                                        <select
                                            className="single-view-content-item-form-item"
                                            name="roleNameList"
                                            id="roleName"
                                            value={newUser.roleName}
                                            onChange={(e) => setNewUser({...newUser, roleName: e.target.value})}
                                        >
                                            <option value="">選択してください</option>
                                            <option value="USER">ユーザー</option>
                                            <option value="ADMIN">管理者</option>
                                        </select>
                                    </div>
                                    <div className="single-view-content-item-form-item">
                                        <label
                                            className="single-view-content-item-form-item-label">パスワード</label>
                                        <input
                                            type="password"
                                            className="single-view-content-item-form-item-input"
                                            placeholder="パスワード"
                                            value={newUser.password?.value || ""}
                                            onChange={(e) => setNewUser({
                                                ...newUser,
                                                password: {value: e.target.value}
                                            })}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )
        }

        const modalView = () => {
            return (
                <Modal
                    isOpen={modalIsOpen}
                    onRequestClose={handleCloseModal}
                    contentLabel="ユーザー情報を入力"
                    className="modal"
                    overlayClassName="modal-overlay"
                    bodyOpenClassName="modal-open"
                >
                    {singleView()}
                </Modal>
            )
        }

        return (
            <>
                {loading ? (
                    <div className="loading">
                        <BeatLoader color="#36D7B7"/>
                    </div>
                ) : (
                    <>
                        {!modalIsOpen && collectionView()}
                        {modalIsOpen && modalView()}
                    </>
                )}
            </>
        );
    }

    return (
        <SiteLayout>
            <Content/>
        </SiteLayout>
    )
}
