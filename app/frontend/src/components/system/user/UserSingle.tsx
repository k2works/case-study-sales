import React from "react";
import {UserSingleView} from "../../../views/system/user/UserSingle.tsx";
import {useUserContext} from "../../../providers/system/User.tsx";

export const UserSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        showErrorMessage,
        setModalIsOpen,
        isEditing,
        editId,
        setEditId,
        initialUser,
        newUser,
        setNewUser,
        fetchUsers,
        userService
    } = useUserContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

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
