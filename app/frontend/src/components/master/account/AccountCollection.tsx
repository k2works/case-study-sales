import React from "react";
import {useAccountContext} from "../../../providers/master/Account.tsx";
import {AccountCollectionView} from "../../../views/master/account/AccountCollection.tsx";
import {AccountType} from "../../../models/master/account.ts";
import {showErrorMessage} from "../../application/utils.ts";
import {AccountSearchModal} from "./AccountSearchModal.tsx";
import {AccountEditModal} from "./AccountEditModal.tsx";

export const AccountCollection: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        pageNation,
        criteria,
        setModalIsOpen,
        setIsEditing,
        setEditId,
        initialAccount,
        accounts,
        setAccounts,
        setNewAccount,
        searchAccountCriteria,
        setSearchAccountCriteria,
        fetchAccounts,
        accountService,
        setSearchModalIsOpen,
    } = useAccountContext();

    const handleOpenModal = (account?: AccountType) => {
        setMessage("");
        setError("");
        if (account) {
            setNewAccount(account);
            setEditId(account.accountCode);
            setIsEditing(true);
        } else {
            setNewAccount(initialAccount);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    }

    const handleDeleteAccount = async (accountCode: string, startDate: string) => {
        try {
            if (!window.confirm(`口座コード:${accountCode} を削除しますか？`)) return;
            await accountService.destroy(accountCode, startDate);
            await fetchAccounts.load();
            setMessage("口座を削除しました。");
        } catch (error: any) {
            showErrorMessage(`口座の削除に失敗しました: ${error?.message}`, setError);
        }
    };

    const handleCheckAccount = (account: AccountType) => {
        const newAccounts = accounts.map((a: AccountType) => {
            if (a.accountCode === account.accountCode && a.startDate === account.startDate) {
                return {
                    ...a,
                    checked: !a.checked
                };
            }
            return a;
        });
        setAccounts(newAccounts);
    }

    const handleCheckAllAccount = () => {
        const newAccounts = accounts.map((a: AccountType) => {
            return {
                ...a,
                checked: !accounts.every((a: AccountType) => a.checked)
            };
        });
        setAccounts(newAccounts);
    }

    const handleDeleteCheckedCollection = async () => {
        const checkedAccounts = accounts.filter((a: AccountType) => a.checked);
        if (!checkedAccounts.length) {
            setError("削除する口座を選択してください。");
            return;
        }

        try {
            if (!window.confirm("選択した口座を削除しますか？")) return;
            await Promise.all(checkedAccounts.map((a: AccountType) => accountService.destroy(a.accountCode, a.startDate)));
            await fetchAccounts.load();
            setMessage("選択した口座を削除しました。");
        } catch (error: any) {
            showErrorMessage(`選択した口座の削除に失敗しました: ${error?.message}`, setError);
        }
    }

    return (
        <>
            <AccountSearchModal/>
            <AccountEditModal/>
            <AccountCollectionView
                error={error}
                message={message}
                searchItems={{searchAccountCriteria, setSearchAccountCriteria, handleOpenSearchModal,}}
                headerItems={{handleOpenModal, handleCheckToggleCollection:handleCheckAllAccount, handleDeleteCheckedCollection}}
                collectionItems={{accounts, handleOpenModal, handleDeleteAccount, handleCheckAccount}}
                pageNationItems={{pageNation, fetchAccounts: fetchAccounts.load, criteria}}
            />
        </>
    );
}