import React from "react";
import { PurchaseOrderCriteriaType, PurchaseOrderType } from "../../../../models/procurement/purchaseOrder.ts";
import { Message } from "../../../../components/application/Message.tsx";
import { PageNation, PageNationType } from "../../../application/PageNation.tsx";
import { Search } from "../../../Common.tsx";

interface PurchaseOrderItemProps {
    purchaseOrder: PurchaseOrderType;
    onEdit: (purchaseOrder: PurchaseOrderType) => void;
    onDelete: (purchaseOrderNumber: string) => void;
    onCheck: (purchaseOrder: PurchaseOrderType) => void;
}

const PurchaseOrderItem: React.FC<PurchaseOrderItemProps> = ({purchaseOrder, onEdit, onDelete, onCheck}) => (
    <li className="collection-object-item" key={purchaseOrder.purchaseOrderNumber}>
        <div className="collection-object-item-content" data-id={purchaseOrder.purchaseOrderNumber}>
            <input type="checkbox" className="collection-object-item-checkbox" checked={purchaseOrder.checked}
                   onChange={() => onCheck(purchaseOrder)}/>
        </div>
        <div className="collection-object-item-content" data-id={purchaseOrder.purchaseOrderNumber}>
            <div className="collection-object-item-content-details">発注番号</div>
            <div className="collection-object-item-content-name">{purchaseOrder.purchaseOrderNumber}</div>
        </div>
        <div className="collection-object-item-content" data-id={purchaseOrder.purchaseOrderNumber}>
            <div className="collection-object-item-content-details">発注日</div>
            <div className="collection-object-item-content-name">{purchaseOrder.purchaseOrderDate.split("T")[0]}</div>
        </div>
        <div className="collection-object-item-content" data-id={purchaseOrder.purchaseOrderNumber}>
            <div className="collection-object-item-content-details">仕入先コード</div>
            <div className="collection-object-item-content-name">{purchaseOrder.supplierCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={purchaseOrder.purchaseOrderNumber}>
            <div className="collection-object-item-content-details">発注金額合計</div>
            <div className="collection-object-item-content-name">{purchaseOrder.totalPurchaseAmount}</div>
        </div>
        <div className="collection-object-item-actions" data-id={purchaseOrder.purchaseOrderNumber}>
            <button className="action-button" onClick={() => onEdit(purchaseOrder)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={purchaseOrder.purchaseOrderNumber}>
            <button className="action-button" onClick={() => onDelete(purchaseOrder.purchaseOrderNumber)} id="delete">削除</button>
        </div>
    </li>
);

interface PurchaseOrderListProps {
    purchaseOrders: PurchaseOrderType[];
    onEdit: (purchaseOrder: PurchaseOrderType) => void;
    onDelete: (purchaseOrderNumber: string) => void;
    onCheck: (purchaseOrder: PurchaseOrderType) => void;
}

const PurchaseOrderList: React.FC<PurchaseOrderListProps> = ({purchaseOrders, onEdit, onDelete, onCheck}) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {purchaseOrders.map(purchaseOrder => (
                <PurchaseOrderItem
                    key={purchaseOrder.purchaseOrderNumber}
                    purchaseOrder={purchaseOrder}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface PurchaseOrderCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchPurchaseOrderCriteria: PurchaseOrderCriteriaType;
        setSearchPurchaseOrderCriteria: (value: PurchaseOrderCriteriaType) => void;
        handleOpenSearchModal: () => void;
    }
    headerItems: {
        handleOpenModal: (purchaseOrder?: PurchaseOrderType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    }
    collectionItems: {
        purchaseOrders: PurchaseOrderType[];
        handleOpenModal: (purchaseOrder?: PurchaseOrderType) => void;
        handleDeletePurchaseOrder: (purchaseOrderNumber: string) => void;
        handleCheckPurchaseOrder: (purchaseOrder: PurchaseOrderType) => void;
    }
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: PurchaseOrderCriteriaType | null;
        fetchPurchaseOrders: () => void;
    }
}

export const PurchaseOrderCollectionView: React.FC<PurchaseOrderCollectionViewProps> = ({
    error,
    message,
    searchItems: {searchPurchaseOrderCriteria, setSearchPurchaseOrderCriteria, handleOpenSearchModal},
    headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
    collectionItems: { purchaseOrders, handleDeletePurchaseOrder, handleCheckPurchaseOrder },
    pageNationItems: { pageNation, criteria, fetchPurchaseOrders }
}) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">発注一覧</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchPurchaseOrderCriteria}
                    setSearchCriteria={setSearchPurchaseOrderCriteria}
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
                <PurchaseOrderList
                    purchaseOrders={purchaseOrders}
                    onEdit={handleOpenModal}
                    onDelete={handleDeletePurchaseOrder}
                    onCheck={handleCheckPurchaseOrder}
                />
                <PageNation pageNation={pageNation} callBack={fetchPurchaseOrders} criteria={criteria}/>
            </div>
        </div>
    </div>
);