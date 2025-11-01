import React from "react";
import Modal from "react-modal";
import { PaymentSearchSingleView } from "../../../../views/procurement/payment/list/PaymentSearch.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import { usePaymentContext } from "../../../../providers/procurement/Payment.tsx";
import { PaymentCriteriaType, convertCodeToPaymentMethodType } from "../../../../models/procurement/payment.ts";
import { VendorSelectModal } from "./VendorSelectModal.tsx";
import { useVendorContext } from "../../../../providers/master/partner/Vendor.tsx";
import { useDepartmentContext } from "../../../../providers/master/Department.tsx";
import { DepartmentSelectModal } from "./DepartmentSelectModal.tsx";

export const PaymentSearchModal: React.FC = () => {
    const {
        searchPaymentCriteria,
        setSearchPaymentCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setPayments,
        setCriteria,
        setPageNation,
        setLoading,
        setMessage,
        setError,
        paymentService
    } = usePaymentContext();

    const {
        setSearchModalIsOpen: setVendorSearchModalIsOpen,
    } = useVendorContext();

    const {
        setSearchModalIsOpen: setDepartmentSearchModalIsOpen,
    } = useDepartmentContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    }

    const handleSelectSearchModal = async () => {
        if (!searchPaymentCriteria) {
            return;
        }
        setLoading(true);
        try {
            const mappedCriteria: PaymentCriteriaType = {
                ...searchPaymentCriteria,
                paymentMethodType: searchPaymentCriteria.paymentMethodType,
                paymentCompletedFlag: searchPaymentCriteria.paymentCompletedFlag === "true" ? true :
                                      searchPaymentCriteria.paymentCompletedFlag === "false" ? false : undefined
            };
            const result = await paymentService.search(mappedCriteria);
            setPayments(result ? result.list.map(payment => ({
                ...payment,
                paymentMethodType: convertCodeToPaymentMethodType(payment.paymentMethodType)
            })) : []);
            if (result.list.length === 0) {
                showErrorMessage(`検索結果は0件です`, setError);
            } else {
                setCriteria(mappedCriteria);
                setPageNation(result);
                setMessage("");
                setError("");
            }
        } catch (error: any) {
            showErrorMessage(`支払の検索に失敗しました: ${error?.message}`, setError);
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
                <PaymentSearchSingleView
                    criteria={searchPaymentCriteria}
                    setCondition={setSearchPaymentCriteria}
                    handleSelect={handleSelectSearchModal}
                    handleClose={handleCloseSearchModal}
                    handleVendorSelect={() => setVendorSearchModalIsOpen(true)}
                    handleDepartmentSelect={() => setDepartmentSearchModalIsOpen(true)}
                />
            }
            <VendorSelectModal type={"search"}/>
            <DepartmentSelectModal type={"search"}/>
        </Modal>
    )
}
