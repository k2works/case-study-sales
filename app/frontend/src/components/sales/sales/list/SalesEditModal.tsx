import React from "react";
import Modal from "react-modal";
import { useSalesContext } from "../../../../providers/sales/Sales.tsx";

export const SalesEditModal: React.FC = () => {
    const {
        modalIsOpen,
        setModalIsOpen,
        isEditing,
        newSales,
    } = useSalesContext();

    const handleCloseModal = () => {
        setModalIsOpen(false);
    }

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel={isEditing ? "売上を編集" : "売上を登録"}
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <div className="single-view-object-container">
                <div className="single-view-header">
                    <div className="single-view-header-item">
                        <h1 className="single-view-title">
                            {isEditing ? `売上編集: ${newSales.salesNumber}` : "売上登録"}
                        </h1>
                    </div>
                </div>
                <div className="single-view-container">
                    <div className="single-view-content">
                        <p>売上編集機能は実装中です。</p>
                        <div className="button-container">
                            <button className="action-button" onClick={handleCloseModal}>
                                閉じる
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </Modal>
    );
};
