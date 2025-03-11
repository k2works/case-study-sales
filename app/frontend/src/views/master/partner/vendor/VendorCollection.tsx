import React from "react";
import {VendorType, VendorCriteriaType} from "../../../../models/master/partner";
import { PageNation, PageNationType } from "../../../application/PageNation.tsx";
import { Message } from "../../../../components/application/Message.tsx";
import { Search } from "../../../Common.tsx";

interface VendorItemProps {
    vendor: VendorType;
    onEdit: (vendor: VendorType) => void;
    onDelete: (vendorCode: string, vendorBranchNumber: number) => void;
    onCheck: (vendor: VendorType) => void;
}

const VendorItem: React.FC<VendorItemProps> = ({
                                                   vendor,
                                                   onEdit,
                                                   onDelete,
                                                   onCheck,
                                               }) => (
    <li className="collection-object-item" key={vendor.vendorCode + "-" + vendor.vendorBranchNumber}>
        <div className="collection-object-item-content" data-id={vendor.vendorBranchNumber}>
            <input
                type="checkbox"
                className="collection-object-item-checkbox"
                checked={vendor.checked || false}
                onChange={() => onCheck(vendor)}
            />
        </div>
        <div className="collection-object-item-content" data-id={vendor.vendorBranchNumber}>
            <div className="collection-object-item-content-details">仕入先コード</div>
            <div className="collection-object-item-content-name">{vendor.vendorCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={vendor.vendorBranchNumber}>
            <div className="collection-object-item-content-details">仕入先コード枝番</div>
            <div className="collection-object-item-content-name">{vendor.vendorBranchNumber}</div>
        </div>
        <div className="collection-object-item-content" data-id={vendor.vendorBranchNumber}>
            <div className="collection-object-item-content-details">仕入先名</div>
            <div className="collection-object-item-content-name">{vendor.vendorName}</div>
        </div>
        <div className="collection-object-item-actions" data-id={vendor.vendorBranchNumber}>
            <button className="action-button" onClick={() => onEdit(vendor)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={vendor.vendorBranchNumber}>
            <button className="action-button" onClick={() => onDelete(vendor.vendorCode, vendor.vendorBranchNumber)} id="delete">削除</button>
        </div>
    </li>
);

interface VendorListProps {
    vendors: VendorType[];
    onEdit: (vendor: VendorType) => void;
    onDelete: (vendorCode: string, vendorBranchNumber: number) => void;
    onCheck: (vendor: VendorType) => void;
}

const VendorList: React.FC<VendorListProps> = ({
                                                   vendors,
                                                   onEdit,
                                                   onDelete,
                                                   onCheck,
                                               }) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {vendors.map((vendor) => (
                <VendorItem
                    key={vendor.vendorCode + "-" + vendor.vendorBranchNumber}
                    vendor={vendor}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface VendorCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchVendorCriteria: VendorCriteriaType;
        setSearchVendorCriteria: (value: VendorCriteriaType) => void;
        handleOpenSearchModal: () => void;
    };
    headerItems: {
        handleOpenModal: (vendor?: VendorType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    };
    collectionItems: {
        vendors: VendorType[];
        handleOpenModal: (vendor?: VendorType) => void;
        handleDeleteVendor: (vendorCode: string, vendorBranchNumber: number) => void;
        handleCheckVendor: (vendor: VendorType) => void;
    };
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: VendorCriteriaType | null;
        fetchVendors: () => void;
    };
}

/**
 * VendorCollectionView コンポーネント
 */
export const VendorCollectionView: React.FC<VendorCollectionViewProps> = ({
                                                                              error,
                                                                              message,
                                                                              searchItems: {
                                                                                  searchVendorCriteria,
                                                                                  setSearchVendorCriteria,
                                                                                  handleOpenSearchModal,
                                                                              },
                                                                              headerItems: {
                                                                                  handleOpenModal,
                                                                                  handleCheckToggleCollection,
                                                                                  handleDeleteCheckedCollection,
                                                                              },
                                                                              collectionItems: {
                                                                                  vendors,
                                                                                  handleOpenModal: handleEditVendor,
                                                                                  handleDeleteVendor,
                                                                                  handleCheckVendor,
                                                                              },
                                                                              pageNationItems: {
                                                                                  pageNation,
                                                                                  criteria,
                                                                                  fetchVendors,
                                                                              },
                                                                          }) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message} />
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">仕入先</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchVendorCriteria}
                    setSearchCriteria={setSearchVendorCriteria}
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
                <VendorList
                    vendors={vendors}
                    onEdit={handleEditVendor}
                    onDelete={handleDeleteVendor}
                    onCheck={handleCheckVendor}
                />
                <PageNation
                    pageNation={pageNation}
                    callBack={fetchVendors}
                    criteria={criteria}
                />
            </div>
        </div>
    </div>
);
