import React from "react";
import { PaymentCriteriaType, PaymentSearchCriteriaType, PaymentType } from "../../../../models/procurement/payment.ts";
import { Message } from "../../../../components/application/Message.tsx";
import { PageNation, PageNationType } from "../../../application/PageNation.tsx";
import { Search } from "../../../Common.tsx";

// 日付を整数からYYYY-MM-DD形式に変換
const formatDateFromInteger = (dateInt: number | string): string => {
    const dateStr = String(dateInt);
    if (dateStr.length !== 8) return dateStr;
    return `${dateStr.slice(0, 4)}-${dateStr.slice(4, 6)}-${dateStr.slice(6, 8)}`;
};

interface PaymentItemProps {
    payment: PaymentType;
    onEdit: (payment: PaymentType) => void;
    onDelete: (paymentNumber: string) => void;
    onCheck: (payment: PaymentType) => void;
}

const PaymentItem: React.FC<PaymentItemProps> = ({payment, onEdit, onDelete, onCheck}) => (
    <li className="collection-object-item" key={payment.paymentNumber}>
        <div className="collection-object-item-checkbox">
            <input
                type="checkbox"
                checked={payment.checked || false}
                onChange={() => onCheck(payment)}
            />
        </div>
        <div className="collection-object-item-content" data-id={payment.paymentNumber}>
            <div className="collection-object-item-content-details">支払番号</div>
            <div className="collection-object-item-content-name">{payment.paymentNumber}</div>
        </div>
        <div className="collection-object-item-content" data-id={payment.paymentNumber}>
            <div className="collection-object-item-content-details">支払日</div>
            <div className="collection-object-item-content-name">{formatDateFromInteger(payment.paymentDate)}</div>
        </div>
        <div className="collection-object-item-content" data-id={payment.paymentNumber}>
            <div className="collection-object-item-content-details">部門コード</div>
            <div className="collection-object-item-content-name">{payment.departmentCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={payment.paymentNumber}>
            <div className="collection-object-item-content-details">仕入先コード</div>
            <div className="collection-object-item-content-name">{payment.supplierCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={payment.paymentNumber}>
            <div className="collection-object-item-content-details">支払金額</div>
            <div className="collection-object-item-content-name">{payment.paymentAmount}</div>
        </div>
        <div className="collection-object-item-content" data-id={payment.paymentNumber}>
            <div className="collection-object-item-content-details">支払完了</div>
            <div className="collection-object-item-content-name">{payment.paymentCompletedFlag ? "完了" : "未完了"}</div>
        </div>
        <div className="collection-object-item-actions" data-id={payment.paymentNumber}>
            <button className="action-button" onClick={() => onEdit(payment)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={payment.paymentNumber}>
            <button className="action-button" onClick={() => onDelete(payment.paymentNumber)} id="delete">削除</button>
        </div>
    </li>
);

interface PaymentListProps {
    payments: PaymentType[];
    onEdit: (payment: PaymentType) => void;
    onDelete: (paymentNumber: string) => void;
    onCheck: (payment: PaymentType) => void;
}

const PaymentList: React.FC<PaymentListProps> = ({payments, onEdit, onDelete, onCheck}) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {payments.map(payment => (
                <PaymentItem
                    key={payment.paymentNumber}
                    payment={payment}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface PaymentCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchPaymentCriteria: PaymentSearchCriteriaType;
        setSearchPaymentCriteria: (value: PaymentSearchCriteriaType) => void;
        handleOpenSearchModal: () => void;
    }
    headerItems: {
        handleOpenModal: (payment?: PaymentType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    }
    collectionItems: {
        payments: PaymentType[];
        handleOpenModal: (payment?: PaymentType) => void;
        handleDeletePayment: (paymentNumber: string) => void;
        handleCheckPayment: (payment: PaymentType) => void;
    }
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: PaymentCriteriaType | null;
        fetchPayments: () => void;
    }
}

export const PaymentCollectionView: React.FC<PaymentCollectionViewProps> = ({
    error,
    message,
    searchItems: {searchPaymentCriteria, setSearchPaymentCriteria, handleOpenSearchModal},
    headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
    collectionItems: { payments, handleOpenModal: onEdit, handleDeletePayment, handleCheckPayment },
    pageNationItems: { pageNation, criteria, fetchPayments }
}) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">支払一覧</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchPaymentCriteria}
                    setSearchCriteria={setSearchPaymentCriteria}
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
                <PaymentList
                    payments={payments}
                    onEdit={onEdit}
                    onDelete={handleDeletePayment}
                    onCheck={handleCheckPayment}
                />
                <PageNation pageNation={pageNation} callBack={fetchPayments} criteria={criteria}/>
            </div>
        </div>
    </div>
);
