import React from "react";
import {LocationNumberCriteriaType, LocationNumberType, LocationNumberSearchCriteriaType} from "../../../../models/master/locationnumber.ts";
import {Message} from "../../../../components/application/Message.tsx";
import {PageNation, PageNationType} from "../../../application/PageNation.tsx";
import {Search} from "../../../Common.tsx";

interface LocationNumberItemProps {
    locationNumber: LocationNumberType;
    onEdit: (locationNumber: LocationNumberType) => void;
    onDelete: (warehouseCode: string, locationNumberCode: string, productCode: string) => void;
    onCheck: (locationNumber: LocationNumberType) => void;
}

const LocationNumberItem: React.FC<LocationNumberItemProps> = ({locationNumber, onEdit, onDelete, onCheck}) => (
    <li className="collection-object-item" key={`${locationNumber.warehouseCode}-${locationNumber.locationNumberCode}-${locationNumber.productCode}`}>
        <div className="collection-object-item-content" data-id={`${locationNumber.warehouseCode}-${locationNumber.locationNumberCode}-${locationNumber.productCode}`}>
            <input type="checkbox" className="collection-object-item-checkbox" checked={locationNumber.checked}
                   onChange={() => onCheck(locationNumber)}/>
        </div>
        <div className="collection-object-item-content" data-id={locationNumber.warehouseCode}>
            <div className="collection-object-item-content-details">倉庫コード</div>
            <div className="collection-object-item-content-name">{locationNumber.warehouseCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={locationNumber.locationNumberCode}>
            <div className="collection-object-item-content-details">棚番コード</div>
            <div className="collection-object-item-content-name">{locationNumber.locationNumberCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={locationNumber.productCode}>
            <div className="collection-object-item-content-details">商品コード</div>
            <div className="collection-object-item-content-name">{locationNumber.productCode}</div>
        </div>
        <div className="collection-object-item-actions" data-id={`${locationNumber.warehouseCode}-${locationNumber.locationNumberCode}-${locationNumber.productCode}`}>
            <button className="action-button" onClick={() => onEdit(locationNumber)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={`${locationNumber.warehouseCode}-${locationNumber.locationNumberCode}-${locationNumber.productCode}`}>
            <button className="action-button" onClick={() => onDelete(locationNumber.warehouseCode, locationNumber.locationNumberCode, locationNumber.productCode)} id="delete">削除</button>
        </div>
    </li>
);

interface LocationNumberListProps {
    locationNumbers: LocationNumberType[];
    onEdit: (locationNumber: LocationNumberType) => void;
    onDelete: (warehouseCode: string, locationNumberCode: string, productCode: string) => void;
    onCheck: (locationNumber: LocationNumberType) => void;
}

const LocationNumberList: React.FC<LocationNumberListProps> = ({locationNumbers, onEdit, onDelete, onCheck}) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {locationNumbers.map(locationNumber => (
                <LocationNumberItem
                    key={`${locationNumber.warehouseCode}-${locationNumber.locationNumberCode}-${locationNumber.productCode}`}
                    locationNumber={locationNumber}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface LocationNumberCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchLocationNumberCriteria: LocationNumberSearchCriteriaType;
        setSearchLocationNumberCriteria: (value: LocationNumberSearchCriteriaType) => void;
        handleOpenSearchModal: () => void;
    }
    headerItems: {
        handleOpenModal: (locationNumber?: LocationNumberType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    }
    collectionItems: {
        locationNumbers: LocationNumberType[];
        handleOpenModal: (locationNumber?: LocationNumberType) => void;
        handleDeleteLocationNumber: (warehouseCode: string, locationNumberCode: string, productCode: string) => void;
        handleCheckLocationNumber: (locationNumber: LocationNumberType) => void;
    }
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: LocationNumberCriteriaType | null;
        fetchLocationNumbers: { load: (page?: number, criteria?: LocationNumberCriteriaType) => Promise<void> };
    }
}

export const LocationNumberCollectionView: React.FC<LocationNumberCollectionViewProps> = ({
                                                                                      error,
                                                                                      message,
                                                                                      searchItems: {searchLocationNumberCriteria, setSearchLocationNumberCriteria, handleOpenSearchModal},
                                                                                      headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
                                                                                      collectionItems: { locationNumbers, handleDeleteLocationNumber, handleCheckLocationNumber },
                                                                                      pageNationItems: { pageNation, criteria, fetchLocationNumbers }
                                                                                  }) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">棚番</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchLocationNumberCriteria}
                    setSearchCriteria={setSearchLocationNumberCriteria}
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
                <LocationNumberList
                    locationNumbers={locationNumbers}
                    onEdit={handleOpenModal}
                    onDelete={handleDeleteLocationNumber}
                    onCheck={handleCheckLocationNumber}
                />
                <PageNation pageNation={pageNation} callBack={fetchLocationNumbers.load} criteria={criteria || undefined}/>
            </div>
        </div>
    </div>
);