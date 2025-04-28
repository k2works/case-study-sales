import React from 'react';
import { FaTimes } from 'react-icons/fa';
import { SalesOrderType } from "../../../../models/sales/order.ts";
import { convertToDateInputFormat } from "../../../../components/application/utils.ts";
import { PageNation, PageNationType } from "../../../application/PageNation.tsx";

interface SalesOrderSelectProps {
    handleSelect: () => void;
}

export const SalesOrderSelectView: React.FC<SalesOrderSelectProps> = ({handleSelect}) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">受注</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={handleSelect} id="select-order">選択</button>
                    </div>
                </div>
            </div>
        </div>
    )
}

interface SalesOrderCollectionSelectProps {
    salesOrders: SalesOrderType[];
    handleSelect: (salesOrder: SalesOrderType) => void;
    handleClose: () => void;
    pageNation: PageNationType;
    fetchSalesOrders: () => void;
}

export const SalesOrderCollectionSelectView: React.FC<SalesOrderCollectionSelectProps> = ({
    salesOrders,
    handleSelect,
    handleClose,
    pageNation,
    fetchSalesOrders
}) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <button className="close-modal-button" onClick={handleClose}>
                    <FaTimes aria-hidden="true"/>
                </button>
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">受注一覧</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {salesOrders.map(salesOrder => (
                                <li className="collection-object-item" key={salesOrder.orderNumber}>
                                    <div className="collection-object-item-content" data-id={salesOrder.orderNumber}>
                                        <div className="collection-object-item-content-details">受注番号</div>
                                        <div className="collection-object-item-content-name">{salesOrder.orderNumber}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={salesOrder.orderNumber}>
                                        <div className="collection-object-item-content-details">受注日</div>
                                        <div className="collection-object-item-content-name">
                                            {convertToDateInputFormat(salesOrder.orderDate)}
                                        </div>
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
                                        <button className="action-button" onClick={() => handleSelect(salesOrder)}>選択</button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
                <PageNation pageNation={pageNation} callBack={fetchSalesOrders}/>
            </div>
        </div>
    );
};
