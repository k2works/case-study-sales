import React from "react";
import {AccountCriteriaType, AccountType} from "../../../models/master/account.ts";
import {Message} from "../../../components/application/Message.tsx";
import {PageNation, PageNationType} from "../../application/PageNation.tsx";
import {Search} from "../../Common.tsx";

interface AccountItemProps {
    account: AccountType;
    onEdit: (account: AccountType) => void;
    onDelete: (accountCode: string, startDate: string) => void;
    onCheck: (account: AccountType) => void;
}

const AccountItem: React.FC<AccountItemProps> = ({account, onEdit, onDelete, onCheck}) => (
    <li className="collection-object-item" key={account.accountCode}>
        <div className="collection-object-item-content" data-id={account.accountCode}>
            <input type="checkbox" className="collection-object-item-checkbox" checked={account.checked}
                   onChange={() => onCheck(account)}/>
        </div>
        <div className="collection-object-item-content" data-id={account.accountCode}>
            <div className="collection-object-item-content-details">口座コード</div>
            <div className="collection-object-item-content-name">{account.accountCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={account.accountCode}>
            <div className="collection-object-item-content-details">口座名</div>
            <div className="collection-object-item-content-name">{account.accountName}</div>
        </div>
        <div className="collection-object-item-content" data-id={account.accountCode}>
            <div className="collection-object-item-content-details">口座区分</div>
            <div className="collection-object-item-content-name">{account.accountType}</div>
        </div>
        <div className="collection-object-item-content" data-id={account.bankAccountType}>
            <div className="collection-object-item-content-details">口座種別</div>
            <div className="collection-object-item-content-name">{account.bankAccountType}</div>
        </div>
        <div className="collection-object-item-content" data-id={account.accountNumber}>
            <div className="collection-object-item-content-details">口座番号</div>
            <div className="collection-object-item-content-name">{account.accountNumber}</div>
        </div>
        <div className="collection-object-item-content" data-id={account.accountHolder}>
            <div className="collection-object-item-content-details">口座名義人</div>
            <div className="collection-object-item-content-name">{account.accountHolder}</div>
        </div>
        <div className="collection-object-item-actions" data-id={account.accountCode}>
            <button className="action-button" onClick={() => onEdit(account)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={account.accountCode}>
            <button className="action-button" onClick={() => onDelete(account.accountCode, account.startDate)} id="delete">削除
            </button>
        </div>
    </li>
);

interface AccountListProps {
    accounts: AccountType[];
    onEdit: (account: AccountType) => void;
    onDelete: (accountCode: string, startDate: string) => void;
    onCheck: (account: AccountType) => void;
}

const AccountList: React.FC<AccountListProps> = ({accounts, onEdit, onDelete, onCheck}) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {accounts.map(account => (
                <AccountItem
                    key={account.accountCode}
                    account={account}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface AccountCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchAccountCriteria: AccountCriteriaType;
        setSearchAccountCriteria: (value: AccountCriteriaType) => void;
        handleOpenSearchModal: () => void;
    }
    headerItems: {
        handleOpenModal: (account?: AccountType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    }
    collectionItems: {
        accounts: AccountType[];
        handleOpenModal: (account?: AccountType) => void;
        handleDeleteAccount: (accountCode: string, startDate: string) => void;
        handleCheckAccount: (account: AccountType) => void;
    }
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: AccountCriteriaType | null;
        fetchAccounts: () => void;
    }
}

export const AccountCollectionView: React.FC<AccountCollectionViewProps> = ({
                                                                                error,
                                                                                message,
                                                                                searchItems: {searchAccountCriteria, setSearchAccountCriteria, handleOpenSearchModal},
                                                                                headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
                                                                                collectionItems: { accounts, handleDeleteAccount, handleCheckAccount },
                                                                                pageNationItems: { pageNation, criteria, fetchAccounts }
                                                                            }) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">口座</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchAccountCriteria}
                    setSearchCriteria={setSearchAccountCriteria}
                    handleSearchAudit={handleOpenSearchModal}
                />
                <div className="button-container">
                    <button className="action-button" onClick={() => handleOpenModal()} id="new">
                        新規
                    </button>
                    <button className="action-button" onClick={() => handleCheckToggleCollection()} id="checkAll">
                        一括選択
                    </button>
                    <button className="action-button" onClick={() => handleDeleteCheckedCollection()} id="deleteAll">
                        一括削除
                    </button>
                </div>
                <AccountList
                    accounts={accounts}
                    onEdit={handleOpenModal}
                    onDelete={handleDeleteAccount}
                    onCheck={handleCheckAccount}
                />
                <PageNation pageNation={pageNation} callBack={fetchAccounts} criteria={criteria}/>
            </div>
        </div>
    </div>
);