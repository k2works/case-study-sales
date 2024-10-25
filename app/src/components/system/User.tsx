import React, {useEffect, useState} from "react";
import {SiteLayout} from "../application/SiteLayout";
import {UserAccountType} from "../../types";
import {useMessage} from "../application/Message";
import {usePageNation} from "../application/PageNation";
import {useModal} from "../application/hooks";
import {useUser} from "./hooks";
import {UserCollectionView, UserSingleView} from "../../ui/system/User";
import LoadingIndicator from "../application/LoadingIndicatior.tsx";

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

        useEffect(() => {
            fetchUsers().then(() => {
            });
        }, []);

        const fetchUsers = async (page: number = 1) => {
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
                    await fetchUsers();
                    setMessage("ユーザーを削除しました。");
                } catch (error: any) {
                    showErrorMessage(`ユーザーの削除に失敗しました: ${error?.message}`, setError);
                }
            };

            return (
                <UserCollectionView
                    error={error}
                    message={message}
                    searchUserId={searchUserId}
                    setSearchUserId={setSearchUserId}
                    handleSearchUser={handleSearchUser}
                    handleOpenModal={handleOpenModal}
                    users={users}
                    handleDeleteUser={handleDeleteUser}
                    fetchUsers={fetchUsers}
                    pageNation={pageNation}
                />
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
                    await fetchUsers();
                    setMessage("ユーザーを保存しました。");
                    handleCloseModal();
                } catch (error: any) {
                    showErrorMessage(`ユーザーの保存に失敗しました: ${error?.message}`, setError);
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
                    <LoadingIndicator/>
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
    );
};
