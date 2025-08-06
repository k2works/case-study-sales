import React from "react";
import {showErrorMessage} from "../../application/utils.ts";
import {AccountSingleView} from "../../../views/master/account/AccountSingle.tsx";
import {useAccountContext} from "../../../providers/master/Account.tsx";

export const AccountSingle: React.FC = () => {
    const {
        error,
        setError,
        message,
        setMessage,
        isEditing,
        newAccount,
        setNewAccount,
        initialAccount,
        fetchAccounts,
        accountService,
        setModalIsOpen,
        editId,
        setEditId
    } = useAccountContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdateAccount = async () => {
        const validateAccount = (): boolean => {
            if (!newAccount.accountCode.trim() || !newAccount.accountName.trim()) {
                setError("口座コード、口座名は必須項目です。");
                return false;
            }
            return true;
        };

        if (!validateAccount()) {
            return;
        }

        try {
            if (isEditing && editId) {
                await accountService.update(newAccount);
            } else {
                await accountService.create(newAccount);
            }
            setNewAccount(initialAccount);
            await fetchAccounts.load();
            if (isEditing) {
                setMessage("口座を更新しました。");
            } else {
                setMessage("口座を作成しました。");
            }
            handleCloseModal();
        } catch (error: unknown) {
            // エラーを表示する処理
            showErrorMessage(error, setError, "口座の作成に失敗しました");
        }
    }

    return (
        <>
            <AccountSingleView
                error={error}
                message={message}
                isEditing={isEditing}
                headerItems={{handleCreateOrUpdateAccount, handleCloseModal}}
                formItems={{newAccount, setNewAccount}}
            />
        </>
    );
}