import React from "react";
import { useSalesContext } from "../../../../providers/sales/Sales.tsx";
import { SalesSingleView } from "../../../../views/sales/sales/list/SalesSingle.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import { useDepartmentContext } from "../../../../providers/master/Department.tsx";
import { useEmployeeContext } from "../../../../providers/master/Employee.tsx";
import { useProductItemContext } from "../../../../providers/master/product/ProductItem.tsx";
import {usePartnerListContext} from "../../../../providers/master/partner/PartnerList.tsx";

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
        setModalIsOpen: setPartnerListModalIsOpen,
    } = usePartnerListContext();

    const {
        setModalIsOpen: setProductModalIsOpen,
    } = useProductItemContext();

    const handleCloseModal = () => {
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdateSales = async () => {
        const validate = () => {
            if (!newSales.orderNumber) {
                setError("受注番号を入力してください。");
                return false;
            }
            if (!newSales.departmentCode) {
                setError("部門を選択してください。");
                return false;
            }
            if (!newSales.customerCode) {
                setError("顧客を選択してください。");
                return false;
            }
            if (!newSales.employeeCode) {
                setError("担当者を選択してください。");
                return false;
            }
            if (!newSales.salesLines.map(line => line.productCode).every(code => code)) {
                setError("商品を選択してください。");
                return false;
            }
            return true;
        }

        if (!validate()) {
            return;
        }

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
        } catch (error: any) {
            showErrorMessage(`売上の${isEditing ? '更新' : '登録'}に失敗しました: ${error?.message}`, setError);
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
            handlePartnerSelect={() => setPartnerListModalIsOpen(true)}
            handleProductSelect={() => setProductModalIsOpen(true)}
        />
    );
};