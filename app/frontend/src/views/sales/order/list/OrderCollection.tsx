import React from "react";
import { SalesOrderCriteriaType, SalesOrderType } from "../../../../models/sales/order.ts";
import { Message } from "../../../../components/application/Message.tsx";
import { PageNation, PageNationType } from "../../../application/PageNation.tsx";
import { Search } from "../../../Common.tsx";

interface SalesOrderItemProps {
    salesOrder: SalesOrderType;
    onEdit: (salesOrder: SalesOrderType) => void;
    onDelete: (orderNumber: string) => void;
    onCheck: (salesOrder: SalesOrderType) => void;
}

const SalesOrderItem: React.FC<SalesOrderItemProps> = ({salesOrder, onEdit, onDelete, onCheck}) => (
    <li className="collection-object-item" key={salesOrder.orderNumber}>
        <div className="collection-object-item-content" data-id={salesOrder.orderNumber}>
            <input type="checkbox" className="collection-object-item-checkbox" checked={salesOrder.checked}
                   onChange={() => onCheck(salesOrder)}/>
        </div>
        <div className="collection-object-item-content" data-id={salesOrder.orderNumber}>
            <div className="collection-object-item-content-details">受注番号</div>
            <div className="collection-object-item-content-name">{salesOrder.orderNumber}</div>
        </div>
        <div className="collection-object-item-content" data-id={salesOrder.orderNumber}>
            <div className="collection-object-item-content-details">受注日</div>
            <div className="collection-object-item-content-name">{salesOrder.orderDate.split("T")[0]}</div>
        </div>
        <div className="collection-object-item-content" data-id={salesOrder.orderNumber}>
            <div className="collection-object-item-content-details">顧客コード</div>
            <div className="collection-object-item-content-name">{salesOrder.customerCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={salesOrder.orderNumber}>
            <div className="collection-object-item-content-details">受注金額合計</div>
            <div className="collection-object-item-content-name">{salesOrder.totalOrderAmount}</div>
        </div>
        <div className="collection-object-item-actions" data-id={salesOrder.orderNumber}>
            <button className="action-button" onClick={() => onEdit(salesOrder)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={salesOrder.orderNumber}>
            <button className="action-button" onClick={() => onDelete(salesOrder.orderNumber)} id="delete">削除</button>
        </div>
    </li>
);

interface SalesOrderListProps {
    salesOrders: SalesOrderType[];
    onEdit: (salesOrder: SalesOrderType) => void;
    onDelete: (orderNumber: string) => void;
    onCheck: (salesOrder: SalesOrderType) => void;
}

const SalesOrderList: React.FC<SalesOrderListProps> = ({salesOrders, onEdit, onDelete, onCheck}) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {salesOrders.map(salesOrder => (
                <SalesOrderItem
                    key={salesOrder.orderNumber}
                    salesOrder={salesOrder}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface SalesOrderCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchSalesOrderCriteria: SalesOrderCriteriaType;
        setSearchSalesOrderCriteria: (value: SalesOrderCriteriaType) => void;
        handleOpenSearchModal: () => void;
    }
    headerItems: {
        handleOpenModal: (salesOrder?: SalesOrderType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    }
    collectionItems: {
        salesOrders: SalesOrderType[];
        handleOpenModal: (salesOrder?: SalesOrderType) => void;
        handleDeleteSalesOrder: (orderNumber: string) => void;
        handleCheckSalesOrder: (salesOrder: SalesOrderType) => void;
    }
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: SalesOrderCriteriaType | null;
        fetchSalesOrders: () => void;
    }
}

export const SalesOrderCollectionView: React.FC<SalesOrderCollectionViewProps> = ({
    error,
    message,
    searchItems: {searchSalesOrderCriteria, setSearchSalesOrderCriteria, handleOpenSearchModal},
    headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
    collectionItems: { salesOrders, handleDeleteSalesOrder, handleCheckSalesOrder },
    pageNationItems: { pageNation, criteria, fetchSalesOrders }
}) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">受注一覧</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchSalesOrderCriteria}
                    setSearchCriteria={setSearchSalesOrderCriteria}
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
                <SalesOrderList
                    salesOrders={salesOrders}
                    onEdit={handleOpenModal}
                    onDelete={handleDeleteSalesOrder}
                    onCheck={handleCheckSalesOrder}
                />
                <PageNation pageNation={pageNation} callBack={fetchSalesOrders} criteria={criteria}/>
            </div>
        </div>
    </div>
);