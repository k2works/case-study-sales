import React from "react";
import Modal from "react-modal";
import { PurchaseOrderSearchSingleView } from "../../../../views/sales/purchaseOrder/list/PurchaseOrderSearch.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import { usePurchaseOrderContext } from "../../../../providers/sales/PurchaseOrder.tsx";
import { PurchaseOrderCriteriaType } from "../../../../models/sales/purchaseOrder.ts";
import {EmployeeSelectModal} from "./EmployeeSelectModal.tsx";
import {VendorSelectModal} from "./VendorSelectModal.tsx";
import {useVendorContext} from "../../../../providers/master/partner/Vendor.tsx";
import {useEmployeeContext} from "../../../../providers/master/Employee.tsx";

export const PurchaseOrderSearchModal: React.FC = () => {
    const {
        searchPurchaseOrderCriteria,
        setSearchPurchaseOrderCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setPurchaseOrders,
        setCriteria,
        setPageNation,
        setLoading,
        setMessage,
        setError,
        purchaseOrderService
    } = usePurchaseOrderContext();

    const {
        setSearchModalIsOpen: setVendorSearchModalIsOpen,
    } = useVendorContext();

    const {
        setSearchModalIsOpen: setEmployeeSearchModalIsOpen,
    } = useEmployeeContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    }

    const handleSelectSearchModal = async () => {
        if (!searchPurchaseOrderCriteria) {
            return;
        }
        setLoading(true);
        try {
            const mappedCriteria: PurchaseOrderCriteriaType = {
                ...searchPurchaseOrderCriteria,
                completionFlag: searchPurchaseOrderCriteria.completionFlag === "true"
            };
            const result = await purchaseOrderService.search(mappedCriteria);
            setPurchaseOrders(result ? result.list : []);
            if (result.list.length === 0) {
                showErrorMessage(`検索結果は0件です`, setError);
            } else {
                setCriteria(mappedCriteria);
                setPageNation(result);
                setMessage("");
                setError("");
            }
        } catch (error: any) {
            showErrorMessage(`発注の検索に失敗しました: ${error?.message}`, setError);
        } finally {
            setLoading(false);
        }
    }

    return (
        <Modal
            isOpen={searchModalIsOpen}
            onRequestClose={handleCloseSearchModal}
            contentLabel="検索情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <PurchaseOrderSearchSingleView
                    criteria={searchPurchaseOrderCriteria}
                    setCondition={setSearchPurchaseOrderCriteria}
                    handleSelect={handleSelectSearchModal}
                    handleClose={handleCloseSearchModal}
                    handleVendorSelect={() => setVendorSearchModalIsOpen(true)}
                    handleEmployeeSelect={() => setEmployeeSearchModalIsOpen(true)}
                />
            }
            <VendorSelectModal type={"search"}/>
            <EmployeeSelectModal type={"search"}/>
        </Modal>
    )
}