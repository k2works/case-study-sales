import React, {useEffect, useState} from "react";
import {UserAccountType} from "../../models";
import {useMessage} from "../application/Message";
import {useModal} from "../application/hooks";
import {useFetchUsers, useUser} from "./hooks";
import {usePageNation} from "../../views/application/PageNation.tsx";
import LoadingIndicator from "../../views/application/LoadingIndicatior.tsx";
import {SiteLayout} from "../../views/SiteLayout.tsx";
import {UserCollectionView} from "../../views/system/UserCollection.tsx";
import {UserSingleView} from "../../views/system/UserSingle.tsx";

export const User: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const {message, setMessage, error, setError, showErrorMessage} = useMessage();
        const {pageNation, setPageNation} = usePageNation();
        const {modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId, Modal} = useModal();
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

        const fetchUsers = useFetchUsers(
            setLoading,
            setUsers,
            setPageNation,
            setError,
            showErrorMessage,
            userService
        );

        useEffect(() => {
            fetchUsers.load().then(() => {
            });
        }, []);

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
                    if (!window.confirm(`ユーザーID:${userId} を削除しますか？`)) return;
                    await userService.destroy(userId);
                    await fetchUsers.load();
                    setMessage("ユーザーを削除しました。");
                } catch (error: any) {
                    showErrorMessage(`ユーザーの削除に失敗しました: ${error?.message}`, setError);
                }
            };

            return (
                <>
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

                    <UserCollectionView
                        error={error}
                        message={message}
                        searchUserId={searchUserId}
                        setSearchUserId={setSearchUserId}
                        handleSearchUser={handleSearchUser}
                        handleOpenModal={handleOpenModal}
                        users={users}
                        handleDeleteUser={handleDeleteUser}
                        fetchUsers={fetchUsers.load}
                        pageNation={pageNation}
                    />
                </>
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
                    setNewUser(initialUser);
                    await fetchUsers.load();
                    if (isEditing) {
                        setMessage("ユーザーを更新しました。");
                    } else {
                        setMessage("ユーザーを作成しました。");
                    }
                    handleCloseModal();
                } catch (error: any) {
                    showErrorMessage(`ユーザーの作成に失敗しました: ${error?.message}`, setError);
                }
            };
            return (
                <UserSingleView
                    error={error}
                    message={message}
                    isEditing={isEditing}
                    handleCreateOrUpdateUser={handleCreateOrUpdateUser}
                    handleCloseModal={handleCloseModal}
                    newUser={newUser}
                    setNewUser={setNewUser}
                />
            )
        }

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    collectionView()
                )}
            </>
        );
    }

    return (
        <SiteLayout>
            <Content/>
        </SiteLayout>
    );
};
