import React from "react";
import { useInvoiceContext } from "../../../../providers/sales/Invoice.tsx";
import {
    InvoiceSearchSingleView
} from "../../../../views/sales/invoice/list/InvoiceSearch.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import Modal from "react-modal";

export const InvoiceSearchModal: React.FC = () => {
    const {
        setError,
        searchModalIsOpen,
        setSearchModalIsOpen,
        searchInvoiceCriteria,
        setSearchInvoiceCriteria,
        fetchInvoices,
        setCriteria,
    } = useInvoiceContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    };

    const handleSearch = async () => {
        try {
            setCriteria(searchInvoiceCriteria);
            await fetchInvoices.load(1, searchInvoiceCriteria);
            setSearchModalIsOpen(false);
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`請求の検索に失敗しました: ${errorMessage}`, setError);
        }
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
            {
                <InvoiceSearchSingleView
                    criteria={searchInvoiceCriteria}
                    setCondition={setSearchInvoiceCriteria}
                    handleSelect={handleSearch}
                    handleClose={handleCloseSearchModal}
                />
            }
        </Modal>
    );
};