import React from "react";
import {useCustomerContext} from "../../../../providers/master/partner/Customer.tsx";
import Modal from "react-modal";
import {CustomerSearchView} from "../../../../views/master/partner/customer/CustomerSearch.tsx";
import {showErrorMessage} from "../../../application/utils.ts";

export const CustomerSearchModal: React.FC = () => {
    const {
        setLoading,
        setMessage,
        setError,
        setPageNation,
        setCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setCustomers,
        searchCustomerCriteria,
        setSearchCustomerCriteria,
        customerService
    } = useCustomerContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    };

    return (
        <Modal
            isOpen={searchModalIsOpen}
            onRequestClose={handleCloseSearchModal}
            contentLabel="検索情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <CustomerSearchView
                criteria={searchCustomerCriteria}
                setCondition={setSearchCustomerCriteria}
                handleSelect={async () => {
                    if (!searchCustomerCriteria) return;
                    setLoading(true);
                    try {
                        const result = await customerService.search(searchCustomerCriteria);
                        setCustomers(result ? result.list : []);
                        if (result.list.length === 0) {
                            showErrorMessage("検索結果は0件です", setError);
                        } else {
                            setCriteria(searchCustomerCriteria);
                            setPageNation(result);
                            setMessage("");
                            setError("");
                        }
                    } catch (error: any) {
                        showErrorMessage(`検索に失敗しました: ${error?.message}`, setError);
                    } finally {
                        setLoading(false);
                    }
                }}
                handleClose={handleCloseSearchModal}
            />
        </Modal>
    );
}
