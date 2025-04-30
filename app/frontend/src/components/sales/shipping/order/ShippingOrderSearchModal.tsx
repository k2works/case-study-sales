import React from "react";
import Modal from "react-modal";
import { ShippingSearchSingleView } from "../../../../views/sales/shipping/list/ShippingSearch.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import { useShippingContext } from "../../../../providers/sales/Shipping.tsx";
import { useDepartmentContext } from "../../../../providers/master/Department.tsx";
import { useCustomerContext } from "../../../../providers/master/partner/Customer.tsx";
import { useEmployeeContext } from "../../../../providers/master/Employee.tsx";

export const ShippingOrderSearchModal: React.FC = () => {
    const {
        searchShippingCriteria,
        setSearchShippingCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setShippings,
        setCriteria,
        setPageNation,
        setLoading,
        setMessage,
        setError,
        shippingService
    } = useShippingContext();

    const {
        setSearchModalIsOpen: setDepartmentSearchModalIsOpen,
    } = useDepartmentContext();

    const {
        setSearchModalIsOpen: setCustomerSearchModalIsOpen,
    } = useCustomerContext();

    const {
        setSearchModalIsOpen: setEmployeeSearchModalIsOpen,
    } = useEmployeeContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    }

    const handleSelectSearchModal = async () => {
        if (!searchShippingCriteria) {
            return;
        }
        setLoading(true);
        try {
            const result = await shippingService.search(searchShippingCriteria);
            setShippings(result ? result.list : []);
            if (result.list.length === 0) {
                showErrorMessage(`検索結果は0件です`, setError);
            } else {
                setCriteria(searchShippingCriteria);
                setPageNation(result);
                setMessage("");
                setError("");
            }
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`出荷の検索に失敗しました: ${errorMessage}`, setError);
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
                <ShippingSearchSingleView
                    criteria={searchShippingCriteria}
                    setCondition={setSearchShippingCriteria}
                    handleSelect={handleSelectSearchModal}
                    handleClose={handleCloseSearchModal}
                    handleDepartmentSelect={() => setDepartmentSearchModalIsOpen(true)}
                    handleCustomerSelect={() => setCustomerSearchModalIsOpen(true)}
                    handleEmployeeSelect={() => setEmployeeSearchModalIsOpen(true)}
                />
            }
        </Modal>
    )
}
