import React from "react";
import { FaTimes } from "react-icons/fa";
import { PageNation, PageNationType } from "../../../application/PageNation.tsx";
import { VendorType } from "../../../../models/master/partner";

interface VendorSelectProps {
    handleSelect: () => void;
}

export const VendorSelectView: React.FC<VendorSelectProps> = ({handleSelect}) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">仕入先</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={handleSelect} id="select-vendor">選択</button>
                    </div>
                </div>
            </div>
        </div>
    )
}
interface VendorCollectionSelectProps {
    vendors: VendorType[];
    handleSelect: (vendor: VendorType) => void;
    handleClose: () => void;
    pageNation: PageNationType | null;
    fetchVendors: () => void;
}

export const VendorCollectionSelectView: React.FC<VendorCollectionSelectProps> = ({
                                                                                      vendors,
                                                                                      handleSelect,
                                                                                      handleClose,
                                                                                      pageNation,
                                                                                      fetchVendors,
                                                                                  }) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <button className="close-modal-button" onClick={handleClose}>
                    <FaTimes aria-hidden="true" />
                </button>
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">仕入先</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {vendors.map((vendor) => (
                                <li className="collection-object-item" key={vendor.vendorCode.code.value}>
                                    <div className="collection-object-item-content"
                                         data-id={vendor.vendorCode.code.value}>
                                        <div className="collection-object-item-content-details">仕入先コード</div>
                                        <div className="collection-object-item-content-name">
                                            {vendor.vendorCode.code.value}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-content"
                                         data-id={vendor.vendorCode.code.value}>
                                        <div className="collection-object-item-content-details">仕入先コード枝番</div>
                                        <div className="collection-object-item-content-name">
                                            {vendor.vendorCode.branchNumber}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-content"
                                         data-id={vendor.vendorCode.code.value}>
                                        <div className="collection-object-item-content-details">仕入先名</div>
                                        <div className="collection-object-item-content-name">
                                            {vendor.vendorName.value.name}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-actions"
                                         data-id={vendor.vendorCode.code.value}>
                                        <button
                                            className="action-button"
                                            onClick={() => handleSelect(vendor)}
                                            id="select-vendor">
                                            選択
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
                <PageNation pageNation={pageNation} callBack={fetchVendors}/>
            </div>
        </div>
    );
};