import React from "react";
import { PurchaseCriteriaType, PurchaseSearchCriteriaType, PurchaseType } from "../../../../models/procurement/purchase.ts";
import { Message } from "../../../../components/application/Message.tsx";
import { PageNation, PageNationType } from "../../../application/PageNation.tsx";
import { Search } from "../../../Common.tsx";

interface PurchaseItemProps {
    purchase: PurchaseType;
    onEdit: (purchase: PurchaseType) => void;
    onDelete: (purchaseNumber: string) => void;
    onCheck: (purchase: PurchaseType) => void;
}

const PurchaseItem: React.FC<PurchaseItemProps> = ({purchase, onEdit, onDelete, onCheck}) => (
    <li className="collection-object-item" key={purchase.purchaseNumber}>
        <div className="collection-object-item-content" data-id={purchase.purchaseNumber}>
            <input type="checkbox" className="collection-object-item-checkbox" checked={purchase.checked}
                   onChange={() => onCheck(purchase)}/>
        </div>
        <div className="collection-object-item-content" data-id={purchase.purchaseNumber}>
            <div className="collection-object-item-content-details">仕入番号</div>
            <div className="collection-object-item-content-name">{purchase.purchaseNumber}</div>
        </div>
        <div className="collection-object-item-content" data-id={purchase.purchaseNumber}>
            <div className="collection-object-item-content-details">仕入日</div>
            <div className="collection-object-item-content-name">{purchase.purchaseDate.split("T")[0]}</div>
        </div>
        <div className="collection-object-item-content" data-id={purchase.purchaseNumber}>
            <div className="collection-object-item-content-details">仕入先コード</div>
            <div className="collection-object-item-content-name">{purchase.supplierCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={purchase.purchaseNumber}>
            <div className="collection-object-item-content-details">仕入金額合計</div>
            <div className="collection-object-item-content-name">{purchase.totalPurchaseAmount}</div>
        </div>
        <div className="collection-object-item-actions" data-id={purchase.purchaseNumber}>
            <button className="action-button" onClick={() => onEdit(purchase)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={purchase.purchaseNumber}>
            <button className="action-button" onClick={() => onDelete(purchase.purchaseNumber)} id="delete">削除</button>
        </div>
    </li>
);

interface PurchaseListProps {
    purchases: PurchaseType[];
    onEdit: (purchase: PurchaseType) => void;
    onDelete: (purchaseNumber: string) => void;
    onCheck: (purchase: PurchaseType) => void;
}

const PurchaseList: React.FC<PurchaseListProps> = ({purchases, onEdit, onDelete, onCheck}) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {purchases.map(purchase => (
                <PurchaseItem
                    key={purchase.purchaseNumber}
                    purchase={purchase}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface PurchaseCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchPurchaseCriteria: PurchaseSearchCriteriaType;
        setSearchPurchaseCriteria: (value: PurchaseSearchCriteriaType) => void;
        handleOpenSearchModal: () => void;
    }
    headerItems: {
        handleOpenModal: (purchase?: PurchaseType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    }
    collectionItems: {
        purchases: PurchaseType[];
        handleOpenModal: (purchase?: PurchaseType) => void;
        handleDeletePurchase: (purchaseNumber: string) => void;
        handleCheckPurchase: (purchase: PurchaseType) => void;
    }
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: PurchaseCriteriaType | null;
        fetchPurchases: () => void;
    }
}

export const PurchaseCollectionView: React.FC<PurchaseCollectionViewProps> = ({
    error,
    message,
    searchItems: {searchPurchaseCriteria, setSearchPurchaseCriteria, handleOpenSearchModal},
    headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
    collectionItems: { purchases, handleDeletePurchase, handleCheckPurchase },
    pageNationItems: { pageNation, criteria, fetchPurchases }
}) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">仕入一覧</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchPurchaseCriteria}
                    setSearchCriteria={setSearchPurchaseCriteria}
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
                <PurchaseList
                    purchases={purchases}
                    onEdit={handleOpenModal}
                    onDelete={handleDeletePurchase}
                    onCheck={handleCheckPurchase}
                />
                <PageNation pageNation={pageNation} callBack={fetchPurchases} criteria={criteria}/>
            </div>
        </div>
    </div>
);
