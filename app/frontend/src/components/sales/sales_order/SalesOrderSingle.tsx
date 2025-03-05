import React from "react";
import { useSalesOrderContext } from "../../../providers/sales/SalesOrder.tsx";
import { SalesOrderSingleView } from "../../../views/sales/sales_order/SalesOrderSingle.tsx";
import { showErrorMessage } from "../../application/utils.ts";
import {useDepartmentContext} from "../../../providers/master/Department.tsx";
import {useEmployeeContext} from "../../../providers/master/Employee.tsx";
import {useCustomerContext} from "../../../providers/master/partner/Customer.tsx";

export const SalesOrderSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        setEditId,
        newSalesOrder,
        setNewSalesOrder,
        fetchSalesOrders,
        salesOrderService
    } = useSalesOrderContext();

    const {
        setModalIsOpen: setDepartmentModalIsOpen,
    } = useDepartmentContext();

    const {
        setModalIsOpen: setEmployeeModalIsOpen,
    } = useEmployeeContext();

    const {
        setModalIsOpen: setCustomerModalIsOpen,
    } = useCustomerContext();

    const handleCloseModal = () => {
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdateSalesOrder = async () => {
        try {
            if (isEditing) {
                await salesOrderService.update(newSalesOrder);
                setMessage("受注を更新しました。");
            } else {
                await salesOrderService.create(newSalesOrder);
                setMessage("受注を作成しました。");
            }
            await fetchSalesOrders.load();
            handleCloseModal();
        } catch (error: any) {
            showErrorMessage(`受注の更新に失敗しました: ${error?.message}`, setError);
        }
    };

    return (
        <SalesOrderSingleView
            error={error}
            message={message}
            isEditing={isEditing}
            newSalesOrder={newSalesOrder}
            setNewSalesOrder={setNewSalesOrder}
            handleCreateOrUpdateSalesOrder={handleCreateOrUpdateSalesOrder}
            handleCloseModal={handleCloseModal}
            handleDepartmentSelect={() => setDepartmentModalIsOpen(true)}
            handleEmployeeSelect={() => setEmployeeModalIsOpen(true)}
            handleCustomerSelect={() => setCustomerModalIsOpen(true)}
        />
    );
};
