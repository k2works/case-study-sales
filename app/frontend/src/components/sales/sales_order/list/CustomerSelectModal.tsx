import React from "react";
import Modal from "react-modal";
import { CustomerCollectionSelectView } from "../../../../views/master/partner/customer/CustomerSelect.tsx";
import { useCustomerContext } from "../../../../providers/master/partner/Customer.tsx";
import { useSalesOrderContext } from "../../../../providers/sales/SalesOrder.tsx";
import { CustomerType } from "../../../../models/master/partner";

type CustomerSelectModalProps = {
    type: "edit" | "search";
};

export const CustomerSelectModal: React.FC<CustomerSelectModalProps> = ({ type }) => {
    const {
        newSalesOrder,
        setNewSalesOrder,
        searchSalesOrderCriteria,
        setSearchSalesOrderCriteria,
    } = useSalesOrderContext();

    const {
        pageNation: customerPageNation,
        modalIsOpen: customerModalIsOpen,
        setModalIsOpen: setCustomerModalIsOpen,
        searchModalIsOpen: customerSearchModalIsOpen,
        setSearchModalIsOpen: setCustomerSearchModalIsOpen,
        customers,
        fetchCustomers,
    } = useCustomerContext();

    // 編集モーダルを閉じる
    const handleCloseEditModal = () => {
        setCustomerModalIsOpen(false);
    };

    // 検索モーダルを閉じる
    const handleCloseSearchModal = () => {
        setCustomerSearchModalIsOpen(false);
    };

    // 編集モード用モーダルビュー
    const customerEditModalView = () => (
        <Modal
            isOpen={customerModalIsOpen}
            onRequestClose={handleCloseEditModal}
            contentLabel="顧客情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <CustomerCollectionSelectView
                customers={customers}
                handleSelect={(customer: CustomerType) => {
                    setNewSalesOrder({
                        ...newSalesOrder,
                        customerCode: customer.customerCode,
                        customerBranchNumber: customer.customerBranchNumber,
                    });
                    setCustomerModalIsOpen(false);
                }}
                handleClose={handleCloseEditModal}
                pageNation={customerPageNation}
                fetchCustomers={fetchCustomers.load}
            />
        </Modal>
    );

    // 検索モード用モーダルビュー
    const customerSearchModalView = () => (
        <Modal
            isOpen={customerSearchModalIsOpen}
            onRequestClose={handleCloseSearchModal}
            contentLabel="顧客情報を検索"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <CustomerCollectionSelectView
                customers={customers}
                handleSelect={(customer: CustomerType) => {
                    setSearchSalesOrderCriteria({
                        ...searchSalesOrderCriteria,
                        customerCode: customer.customerCode,
                    });
                    setCustomerSearchModalIsOpen(false);
                }}
                handleClose={handleCloseSearchModal}
                pageNation={customerPageNation}
                fetchCustomers={fetchCustomers.load}
            />
        </Modal>
    );

    return (
        <>
            {type === "edit" ? customerEditModalView() : null}
            {type === "search" ? customerSearchModalView() : null}
        </>
    );
};
