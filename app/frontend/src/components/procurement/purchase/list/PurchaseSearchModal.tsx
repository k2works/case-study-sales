import React from "react";
import Modal from "react-modal";
import { PurchaseSearchSingleView } from "../../../../views/procurement/purchase/list/PurchaseSearch.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import { usePurchaseReceiptContext } from "../../../../providers/procurement/Purchase.tsx";
import { PurchaseCriteriaType } from "../../../../models/procurement/purchase.ts";
import {EmployeeSelectModal} from "./EmployeeSelectModal.tsx";
import {VendorSelectModal} from "./VendorSelectModal.tsx";
import {useVendorContext} from "../../../../providers/master/partner/Vendor.tsx";
import {useEmployeeContext} from "../../../../providers/master/Employee.tsx";
import {useDepartmentContext} from "../../../../providers/master/Department.tsx";
import {DepartmentSelectModal} from "./DepartmentSelectModal.tsx";

export const PurchaseSearchModal: React.FC = () => {
    const {
        searchPurchaseCriteria,
        setSearchPurchaseCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setPurchases,
        setCriteria,
        setPageNation,
        setLoading,
        setMessage,
        setError,
        purchaseService
    } = usePurchaseReceiptContext();

    const {
        setSearchModalIsOpen: setVendorSearchModalIsOpen,
    } = useVendorContext();

    const {
        setSearchModalIsOpen: setEmployeeSearchModalIsOpen,
    } = useEmployeeContext();

    const {
        setSearchModalIsOpen: setDepartmentSearchModalIsOpen,
    } = useDepartmentContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    }

    const handleSelectSearchModal = async () => {
        if (!searchPurchaseCriteria) {
            return;
        }
        setLoading(true);
        try {
            const mappedCriteria: PurchaseCriteriaType = {
                ...searchPurchaseCriteria,
                supplierBranchNumber: searchPurchaseCriteria.supplierBranchNumber
                    ? Number(searchPurchaseCriteria.supplierBranchNumber)
                    : undefined
            };
            const result = await purchaseService.search(mappedCriteria);
            setPurchases(result ? result.list : []);
            if (result.list.length === 0) {
                showErrorMessage(`検索結果は0件です`, setError);
            } else {
                setCriteria(mappedCriteria);
                setPageNation(result);
                setMessage("");
                setError("");
            }
        } catch (error: any) {
            showErrorMessage(`仕入の検索に失敗しました: ${error?.message}`, setError);
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
                <PurchaseSearchSingleView
                    criteria={searchPurchaseCriteria}
                    setCondition={setSearchPurchaseCriteria}
                    handleSelect={handleSelectSearchModal}
                    handleClose={handleCloseSearchModal}
                    handleVendorSelect={() => setVendorSearchModalIsOpen(true)}
                    handleEmployeeSelect={() => setEmployeeSearchModalIsOpen(true)}
                    handleDepartmentSelect={() => setDepartmentSearchModalIsOpen(true)}
                />
            }
            <VendorSelectModal type={"search"}/>
            <EmployeeSelectModal type={"search"}/>
            <DepartmentSelectModal type={"search"}/>
        </Modal>
    )
}
