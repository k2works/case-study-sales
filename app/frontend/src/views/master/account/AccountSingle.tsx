import React from 'react';
import {Message} from "../../../components/application/Message.tsx";
import {AccountType, PaymentAccountEnumType, BankAccountEnumType} from "../../../models/master/account.ts";
import {convertToDateInputFormat} from "../../../components/application/utils.ts";
import {FormInput, FormSelect, SingleViewHeaderActions, SingleViewHeaderItem} from "../../Common.tsx";
import {useDepartmentContext} from "../../../providers/master/Department.tsx";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdateAccount: () => void;
    handleCloseModal: () => void;
}

const Header = ({
                    title,
                    subtitle,
                    isEditing,
                    handleCreateOrUpdateAccount,
                    handleCloseModal
                }: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdateAccount}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newAccount: AccountType;
    setNewAccount: React.Dispatch<React.SetStateAction<AccountType>>;
}

const Form = ({isEditing, newAccount, setNewAccount}: FormProps) => {
    const { setModalIsOpen: setDepartmentModalIsOpen } = useDepartmentContext();

    const handleOpenDepartmentModal = () => {
        setDepartmentModalIsOpen(true);
    };

    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="口座コード"
                id="accountCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="口座コード"
                value={newAccount.accountCode}
                onChange={(e) => setNewAccount({
                    ...newAccount,
                    accountCode: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="口座名"
                id="accountName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="口座名"
                value={newAccount.accountName}
                onChange={(e) => setNewAccount({
                    ...newAccount,
                    accountName: e.target.value
                })}
            />
            <FormInput
                label="開始日"
                id="startDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newAccount.startDate)}
                onChange={(e) => setNewAccount({
                    ...newAccount,
                    startDate: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="終了日"
                id="endDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newAccount.endDate)}
                onChange={(e) => setNewAccount({
                    ...newAccount,
                    endDate: e.target.value
                })}
            />
            <FormInput
                label="適用開始後口座名"
                id="accountNameAfterStart"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="適用開始後口座名"
                value={newAccount.accountNameAfterStart}
                onChange={(e) => setNewAccount({
                    ...newAccount,
                    accountNameAfterStart: e.target.value
                })}
            />
            {/* 口座区分フィールド */}
            <FormSelect
                label="口座区分"
                id="accountType"
                value={newAccount.accountType}
                options={PaymentAccountEnumType}
                onChange={(e) => setNewAccount({
                    ...newAccount,
                    accountType: e
                })}
            />
            <FormInput
                label="口座番号"
                id="accountNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="口座番号"
                value={newAccount.accountNumber}
                onChange={(e) => setNewAccount({
                    ...newAccount,
                    accountNumber: e.target.value
                })}
            />
            {/* 銀行口座種別フィールド */}
            <FormSelect
                label="銀行口座種別"
                id="bankAccountType"
                value={newAccount.bankAccountType}
                options={BankAccountEnumType}
                onChange={(e) => setNewAccount({
                    ...newAccount,
                    bankAccountType: e
                })}
            />
            <FormInput
                label="口座名義人"
                id="accountHolder"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="口座名義人"
                value={newAccount.accountHolder}
                onChange={(e) => setNewAccount({
                    ...newAccount,
                    accountHolder: e.target.value
                })}
            />
            <div className="form-item">
                <label htmlFor="departmentCode">部門コード</label>
                <div style={{ display: 'flex', gap: '10px' }}>
                    <input
                        id="departmentCode"
                        type="text"
                        className="single-view-content-item-form-item-input"
                        placeholder="部門コード"
                        value={newAccount.departmentCode}
                        onChange={(e) => setNewAccount({
                            ...newAccount,
                            departmentCode: e.target.value
                        })}
                    />
                    <button
                        type="button"
                        className="single-view-content-item-form-item-button"
                        onClick={handleOpenDepartmentModal}
                    >
                        選択
                    </button>
                </div>
            </div>
            <FormInput
                label="部門開始日"
                id="departmentStartDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newAccount.departmentStartDate)}
                onChange={(e) => setNewAccount({
                    ...newAccount,
                    departmentStartDate: e.target.value
                })}
            />
            <FormInput
                label="銀行コード"
                id="bankCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="銀行コード"
                value={newAccount.bankCode}
                onChange={(e) => setNewAccount({
                    ...newAccount,
                    bankCode: e.target.value
                })}
            />
            <FormInput
                label="支店コード"
                id="branchCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="支店コード"
                value={newAccount.branchCode}
                onChange={(e) => setNewAccount({
                    ...newAccount,
                    branchCode: e.target.value
                })}
            />
        </div>
    );
};

interface AccountSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    headerItems: {
        handleCreateOrUpdateAccount: () => void;
        handleCloseModal: () => void;
    }
    formItems: {
        newAccount: AccountType;
        setNewAccount: React.Dispatch<React.SetStateAction<AccountType>>;
    }
}

export const AccountSingleView = ({
                                     error,
                                     message,
                                     isEditing,
                                     headerItems: {
                                         handleCreateOrUpdateAccount,
                                         handleCloseModal,
                                     },
                                     formItems: {
                                         newAccount,
                                         setNewAccount
                                     }
                                 }: AccountSingleViewProps) => (
    <div className="single-view-object-container">
        <Message error={error} message={message}/>
        <div className="single-view-header">
            <Header
                title="口座"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCreateOrUpdateAccount={handleCreateOrUpdateAccount}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newAccount={newAccount}
                        setNewAccount={setNewAccount}
                    />
                </div>
            </div>
        </div>
    </div>
);
