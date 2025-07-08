import React from "react";
import Modal from "react-modal";
import { PaymentSearchSingleView } from "../../../../views/sales/payment/list/PaymentSearch.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import { usePaymentContext } from "../../../../providers/sales/Payment.tsx";
import { useDepartmentContext } from "../../../../providers/master/Department.tsx";
import { useCustomerContext } from "../../../../providers/master/partner/Customer.tsx";

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
        setSearchModalIsOpen: setDepartmentSearchModalIsOpen,
    } = useDepartmentContext();

    const {
        setSearchModalIsOpen: setCustomerSearchModalIsOpen,
    } = useCustomerContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    }

    const handleSelectSearchModal = async () => {
        if (!searchPaymentCriteria) {
            return;
        }
        setLoading(true);
        try {
            const result = await paymentService.search(searchPaymentCriteria);
            setPayments(result ? result.list : []);
            if (result.list.length === 0) {
                showErrorMessage(`検索結果は0件です`, setError);
            } else {
                setCriteria(searchPaymentCriteria);
                setPageNation(result);
                setMessage("");
                setError("");
            }
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`入金の検索に失敗しました: ${errorMessage}`, setError);
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
                    handleDepartmentSelect={() => setDepartmentSearchModalIsOpen(true)}
                    handleCustomerSelect={() => setCustomerSearchModalIsOpen(true)}
                />
            }
        </Modal>
    )
}