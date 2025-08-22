import React from "react";
import Modal from "react-modal";
import {useVendorContext} from "../../../../providers/master/partner/Vendor.tsx";
import {usePurchaseOrderContext} from "../../../../providers/sales/PurchaseOrder.tsx";

interface Props {
    type: "edit" | "search";
}

export const VendorSelectModal: React.FC<Props> = ({ type }) => {
    const {
        vendors,
        modalIsOpen,
        setModalIsOpen,
        fetchVendors
    } = useVendorContext();

    const {
        newPurchaseOrder,
        setNewPurchaseOrder,
        searchCriteria,
        setSearchCriteria
    } = usePurchaseOrderContext();

    React.useEffect(() => {
        if (modalIsOpen && vendors.length === 0) {
            fetchVendors.load();
        }
    }, [modalIsOpen]);

    const handleVendorSelect = (vendorCode: string, branchNumber: number) => {
        if (type === "edit") {
            setNewPurchaseOrder({
                ...newPurchaseOrder,
                supplierCode: vendorCode,
                supplierBranchNumber: branchNumber
            });
        } else {
            setSearchCriteria({
                ...searchCriteria,
                supplierCode: vendorCode
            });
        }
        setModalIsOpen(false);
    };

    const handleCloseModal = () => {
        setModalIsOpen(false);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="仕入先を選択"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <div className="single-view-object-container">
                <div className="single-view-header">
                    <div className="single-view-header-title">
                        <h2>仕入先選択</h2>
                        <button onClick={handleCloseModal} className="close-button">
                            ✕
                        </button>
                    </div>
                </div>
                <div className="single-view-container">
                    <div className="select-modal-content">
                        <div className="select-modal-list">
                            {vendors.map((vendor) => (
                                <div
                                    key={`${vendor.vendorCode}-${vendor.vendorBranchNumber}`}
                                    className="select-modal-item"
                                    onClick={() => handleVendorSelect(vendor.vendorCode, vendor.vendorBranchNumber)}
                                >
                                    <span className="select-modal-item-code">
                                        {vendor.vendorCode}-{vendor.vendorBranchNumber}
                                    </span>
                                    <span className="select-modal-item-name">
                                        {vendor.vendorName}
                                    </span>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </Modal>
    );
};