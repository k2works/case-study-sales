import React from "react";
import {useUserContext} from "../../../providers/User.tsx";
import {UserAccountType} from "../../../models";
import {UserCollectionView} from "../../../views/system/user/UserCollection.tsx";
import {UserEditModal} from "./UserEditModal.tsx";

export const UserCollection: React.FC = () => {
    const {
        setLoading,
        message,
        setMessage,
        error,
        setError,
        showErrorMessage,
        pageNation,
        setModalIsOpen,
        setIsEditing,
        setEditId,
        initialUser,
        users,
        setUsers,
        setNewUser,
        searchUserId,
        setSearchUserId,
        fetchUsers,
        userService
    } = useUserContext();

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
            <UserEditModal/>
            <UserCollectionView
                error={error}
                message={message}
                searchItems={{ searchUserId, setSearchUserId, handleSearchUser }}
                headerItems={{ handleOpenModal }}
                collectionItems={{ users, handleDeleteUser }}
                pageNationItems={{ pageNation, fetchUsers: fetchUsers.load }}
            />
        </>
    )
}
