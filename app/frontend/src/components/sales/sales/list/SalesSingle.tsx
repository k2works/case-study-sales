import React from "react";
import { useSalesContext } from "../../../../providers/sales/Sales.tsx";
import { SalesSingleView } from "../../../../views/sales/sales/list/SalesSingleView.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import { useDepartmentContext } from "../../../../providers/master/Department.tsx";
import { useEmployeeContext } from "../../../../providers/master/Employee.tsx";
import { useCustomerContext } from "../../../../providers/master/partner/Customer.tsx";
import { useProductItemContext } from "../../../../providers/master/product/ProductItem.tsx";

export const SalesSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        setEditId,
        newSales,
        setNewSales,
        fetchSales,
        salesService,
        setSelectedLineIndex
    } = useSalesContext();

    const {
        setModalIsOpen: setDepartmentModalIsOpen,
    } = useDepartmentContext();

    const {
        setModalIsOpen: setEmployeeModalIsOpen,
    } = useEmployeeContext();

    const {
        setModalIsOpen: setCustomerModalIsOpen,
    } = useCustomerContext();

    const {
        setModalIsOpen: setProductModalIsOpen,
    } = useProductItemContext();

    const handleCloseModal = () => {
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdateSales = async () => {
        try {
            if (isEditing) {
                await salesService.update(newSales);
                setMessage("売上を更新しました。");
            } else {
                await salesService.create(newSales);
                setMessage("売上を作成しました。");
            }
            await fetchSales.load();
            handleCloseModal();
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`売上の${isEditing ? '更新' : '登録'}に失敗しました: ${errorMessage}`, setError);
        }
    };

    return (
        <SalesSingleView
            error={error}
            message={message}
            isEditing={isEditing}
            newSales={newSales}
            setNewSales={setNewSales}
            setSelectedLineIndex={setSelectedLineIndex}
            handleCreateOrUpdateSales={handleCreateOrUpdateSales}
            handleCloseModal={handleCloseModal}
            handleDepartmentSelect={() => setDepartmentModalIsOpen(true)}
            handleEmployeeSelect={() => setEmployeeModalIsOpen(true)}
            handleCustomerSelect={() => setCustomerModalIsOpen(true)}
            handleProductSelect={() => setProductModalIsOpen(true)}
        />
    );
};