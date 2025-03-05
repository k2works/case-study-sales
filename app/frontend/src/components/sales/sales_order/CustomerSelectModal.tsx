import React from "react";
import Modal from "react-modal";
import {CustomerCollectionSelectView} from "../../../views/master/partner/customer/CustomerSelect.tsx";
import {useCustomerContext} from "../../../providers/master/partner/Customer.tsx";
import {useSalesOrderContext} from "../../../providers/sales/SalesOrder.tsx";

export const CustomerSelectModal: React.FC = () => {
    const {
        newSalesOrder,
        setNewSalesOrder,
    } = useSalesOrderContext();

    const {
        pageNation: customerPageNation,
        modalIsOpen: customerModalIsOpen,
        setModalIsOpen: setCustomerModalIsOpen,
        customers,
        fetchCustomers,
    } = useCustomerContext();

    return (
        <Modal
            isOpen={customerModalIsOpen}
            onRequestClose={() => setCustomerModalIsOpen(false)}
            contentLabel="顧客情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <CustomerCollectionSelectView
                    customers={customers}
                    handleSelect={(customer) => {
                        setNewSalesOrder({
                            ...newSalesOrder,
                            customerCode: customer.customerCode.code.value,
                            customerBranchNumber: customer.customerCode.branchNumber
                        });
                        setCustomerModalIsOpen(false);
                    }}
                    handleClose={() => setCustomerModalIsOpen(false)}
                    pageNation={customerPageNation}
                    fetchCustomers={fetchCustomers.load}
                />
            }
        </Modal>
    );
};