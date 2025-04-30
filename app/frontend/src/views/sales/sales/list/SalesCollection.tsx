import React from "react";
import { SalesCriteriaType, SalesType } from "../../../../models/sales/sales";
import { Message } from "../../../../components/application/Message.tsx";
import { PageNation, PageNationType } from "../../../application/PageNation.tsx";
import { Search } from "../../../Common.tsx";

interface SalesItemProps {
    sales: SalesType;
    onEdit: (sales: SalesType) => void;
    onDelete: (salesNumber: string) => void;
    onCheck: (sales: SalesType) => void;
}

const SalesItem: React.FC<SalesItemProps> = ({sales, onEdit, onDelete, onCheck}) => (
    <li className="collection-object-item" key={sales.salesNumber}>
        <div className="collection-object-item-content" data-id={sales.salesNumber}>
            <input type="checkbox" className="collection-object-item-checkbox" checked={sales.checked}
                   onChange={() => onCheck(sales)}/>
        </div>
        <div className="collection-object-item-content" data-id={sales.salesNumber}>
            <div className="collection-object-item-content-details">売上番号</div>
            <div className="collection-object-item-content-name">{sales.salesNumber}</div>
        </div>
        <div className="collection-object-item-content" data-id={sales.salesNumber}>
            <div className="collection-object-item-content-details">売上日</div>
            <div className="collection-object-item-content-name">{sales.salesDate.split("T")[0]}</div>
        </div>
        <div className="collection-object-item-content" data-id={sales.salesNumber}>
            <div className="collection-object-item-content-details">顧客コード</div>
            <div className="collection-object-item-content-name">{sales.customerCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={sales.salesNumber}>
            <div className="collection-object-item-content-details">売上金額合計</div>
            <div className="collection-object-item-content-name">{sales.totalSalesAmount}</div>
        </div>
        <div className="collection-object-item-actions" data-id={sales.salesNumber}>
            <button className="action-button" onClick={() => onEdit(sales)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={sales.salesNumber}>
            <button className="action-button" onClick={() => onDelete(sales.salesNumber)} id="delete">削除</button>
        </div>
    </li>
);

interface SalesListProps {
    sales: SalesType[];
    onEdit: (sales: SalesType) => void;
    onDelete: (salesNumber: string) => void;
    onCheck: (sales: SalesType) => void;
}

const SalesList: React.FC<SalesListProps> = ({sales, onEdit, onDelete, onCheck}) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {sales.map(salesItem => (
                <SalesItem
                    key={salesItem.salesNumber}
                    sales={salesItem}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface SalesCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchSalesCriteria: SalesCriteriaType;
        setSearchSalesCriteria: (value: SalesCriteriaType) => void;
        handleOpenSearchModal: () => void;
    }
    headerItems: {
        handleOpenModal: (sales?: SalesType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    }
    collectionItems: {
        sales: SalesType[];
        handleOpenModal: (sales?: SalesType) => void;
        handleDeleteSales: (salesNumber: string) => void;
        handleCheckSales: (sales: SalesType) => void;
    }
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: SalesCriteriaType | null;
        fetchSales: () => void;
    }
}

export const SalesCollectionView: React.FC<SalesCollectionViewProps> = ({
    error,
    message,
    searchItems: {searchSalesCriteria, setSearchSalesCriteria, handleOpenSearchModal},
    headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
    collectionItems: { sales, handleDeleteSales, handleCheckSales },
    pageNationItems: { pageNation, criteria, fetchSales }
}) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">売上一覧</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchSalesCriteria}
                    setSearchCriteria={setSearchSalesCriteria}
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
                <SalesList
                    sales={sales}
                    onEdit={handleOpenModal}
                    onDelete={handleDeleteSales}
                    onCheck={handleCheckSales}
                />
                <PageNation pageNation={pageNation} callBack={fetchSales} criteria={criteria}/>
            </div>
        </div>
    </div>
);