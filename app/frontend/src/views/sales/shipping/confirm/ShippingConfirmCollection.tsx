import React from "react";
import { ShippingCriteriaType, ShippingType } from "../../../../models/sales/shipping";
import { Message } from "../../../../components/application/Message.tsx";
import { PageNation, PageNationType } from "../../../application/PageNation.tsx";
import { Search } from "../../../Common.tsx";

interface ShippingItemProps {
    shipping: ShippingType;
    onEdit: (shipping: ShippingType) => void;
}

const ShippingItem: React.FC<ShippingItemProps> = ({shipping, onEdit}) => (
    <li className="collection-object-item" key={shipping.orderNumber}>
        <div className="collection-object-item-content" data-id={shipping.orderNumber}>
            <div className="collection-object-item-content-details">受注番号</div>
            <div className="collection-object-item-content-name">{shipping.orderNumber}</div>
        </div>
        <div className="collection-object-item-content" data-id={shipping.orderNumber}>
            <div className="collection-object-item-content-details">受注日</div>
            <div className="collection-object-item-content-name">{shipping.orderDate.split("T")[0]}</div>
        </div>
        <div className="collection-object-item-content" data-id={shipping.orderNumber}>
            <div className="collection-object-item-content-details">顧客コード</div>
            <div className="collection-object-item-content-name">{shipping.customerCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={shipping.orderNumber}>
            <div className="collection-object-item-content-details">商品名</div>
            <div className="collection-object-item-content-name">{shipping.productName}</div>
        </div>
        <div className="collection-object-item-content" data-id={shipping.orderNumber}>
            <div className="collection-object-item-content-details">出荷指示数量</div>
            <div className="collection-object-item-content-name">{shipping.shipmentInstructionQuantity}</div>
        </div>
        <div className="collection-object-item-content" data-id={shipping.orderNumber}>
            <div className="collection-object-item-content-details">出荷済数量</div>
            <div className="collection-object-item-content-name">{shipping.shippedQuantity}</div>
        </div>
        <div className="collection-object-item-content" data-id={shipping.orderNumber}>
            <div className="collection-object-item-content-details">完了フラグ</div>
            <div className="collection-object-item-content-name">{shipping.completionFlag ? "完了" : "未完了"}</div>
        </div>
        <div className="collection-object-item-actions" data-id={shipping.orderNumber}>
            <button className="action-button" onClick={() => onEdit(shipping)} id="edit">編集</button>
        </div>
    </li>
);

interface ShippingListProps {
    shippings: ShippingType[];
    onEdit: (shipping: ShippingType) => void;
}

const ShippingList: React.FC<ShippingListProps> = ({shippings, onEdit}) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {shippings.map(shippingItem => (
                <ShippingItem
                    key={shippingItem.orderNumber}
                    shipping={shippingItem}
                    onEdit={onEdit}
                />
            ))}
        </ul>
    </div>
);

interface ShippingCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchShippingCriteria: ShippingCriteriaType;
        setSearchShippingCriteria: (value: ShippingCriteriaType) => void;
        handleOpenSearchModal: () => void;
    }
    headerItems: {
        handleOpenModal: (shipping?: ShippingType) => void;
        handleCheckToggleCollection: () => void;
        handleConfirmShipping: () => void;
    }
    collectionItems: {
        shippings: ShippingType[];
        handleOpenModal: (shipping?: ShippingType) => void;
        handleCheckShipping: (shipping: ShippingType) => void;
    }
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: ShippingCriteriaType | null;
        fetchShippings: () => void;
    }
}

export const ShippingConfirmCollectionView: React.FC<ShippingCollectionViewProps> = ({
    error,
    message,
    searchItems: {searchShippingCriteria, setSearchShippingCriteria, handleOpenSearchModal},
    headerItems: {handleOpenModal, handleConfirmShipping},
    collectionItems: { shippings },
    pageNationItems: { pageNation, criteria, fetchShippings }
}) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">出荷確認</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchShippingCriteria}
                    setSearchCriteria={setSearchShippingCriteria}
                    handleSearchAudit={handleOpenSearchModal}
                />
                <div className="button-container">
                    <button className="action-button" onClick={() => handleConfirmShipping()} id="confirmShipping">
                        出荷確認
                    </button>
                </div>
                <ShippingList
                    shippings={shippings}
                    onEdit={handleOpenModal}
                />
                <PageNation pageNation={pageNation} callBack={fetchShippings} criteria={criteria}/>
            </div>
        </div>
    </div>
);