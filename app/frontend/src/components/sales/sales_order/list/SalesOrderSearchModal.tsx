import React from "react";
import Modal from "react-modal";
import { SalesOrderSearchSingleView } from "../../../../views/sales/sales_order/list/SalesOrderSearch.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import { useSalesOrderContext } from "../../../../providers/sales/SalesOrder.tsx";
import {DepartmentSelectModal} from "./DepartmentSelectModal.tsx";
import {useDepartmentContext} from "../../../../providers/master/Department.tsx";
import {CustomerSelectModal} from "./CustomerSelectModal.tsx";
import {EmployeeSelectModal} from "./EmployeeSelectModal.tsx";
import {useCustomerContext} from "../../../../providers/master/partner/Customer.tsx";
import {useEmployeeContext} from "../../../../providers/master/Employee.tsx";

export const SalesOrderSearchModal: React.FC = () => {
    const {
        searchSalesOrderCriteria,
        setSearchSalesOrderCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setSalesOrders,
        setCriteria,
        setPageNation,
        setLoading,
        setMessage,
        setError,
        salesOrderService
    } = useSalesOrderContext();

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
        if (!searchSalesOrderCriteria) {
            return;
        }
        setLoading(true);
        try {
            const result = await salesOrderService.search(searchSalesOrderCriteria);
            setSalesOrders(result ? result.list : []);
            if (result.list.length === 0) {
                showErrorMessage(`検索結果は0件です`, setError);
            } else {
                setCriteria(searchSalesOrderCriteria);
                setPageNation(result);
                setMessage("");
                setError("");
            }
        } catch (error: any) {
            showErrorMessage(`受注の検索に失敗しました: ${error?.message}`, setError);
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
                <SalesOrderSearchSingleView
                    criteria={searchSalesOrderCriteria}
                    setCondition={setSearchSalesOrderCriteria}
                    handleSelect={handleSelectSearchModal}
                    handleClose={handleCloseSearchModal}
                    handleDepartmentSelect={() => setDepartmentSearchModalIsOpen(true)}
                    handleCustomerSelect={() => setCustomerSearchModalIsOpen(true)}
                    handleEmployeeSelect={() => setEmployeeSearchModalIsOpen(true)}
                />
            }
            <DepartmentSelectModal type={"search"}/>
            <CustomerSelectModal type={"search"}/>
            <EmployeeSelectModal type={"search"}/>
        </Modal>
    )
}