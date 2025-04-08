import React from "react";
import { useShippingContext } from "../../../../providers/sales/Shipping.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import { useDepartmentContext } from "../../../../providers/master/Department.tsx";
import { useEmployeeContext } from "../../../../providers/master/Employee.tsx";
import { useCustomerContext } from "../../../../providers/master/partner/Customer.tsx";
import { useProductItemContext } from "../../../../providers/master/product/ProductItem.tsx";
import {ShippingSingleView} from "../../../../views/shipping/shipping/list/ShippingSingle.tsx";

export const ShippingSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        setEditId,
        newShipping,
        setNewShipping,
        fetchShippings,
        shippingService,
    } = useShippingContext();

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

    const handleCreateOrUpdateShipping = async () => {
        try {
            if (isEditing) {
                await shippingService.save(newShipping);
                setMessage("出荷を更新しました。");
            } else {
                await shippingService.save(newShipping);
                setMessage("出荷を作成しました。");
            }
            await fetchShippings.load();
            handleCloseModal();
        } catch (error: any) {
            showErrorMessage(`出荷の更新に失敗しました: ${error?.message}`, setError);
        }
    };

    return (
        <ShippingSingleView
            error={error}
            message={message}
            isEditing={isEditing}
            newShipping={newShipping}
            setNewShipping={setNewShipping}
            handleCreateOrUpdateShipping={handleCreateOrUpdateShipping}
            handleCloseModal={handleCloseModal}
            handleDepartmentSelect={() => setDepartmentModalIsOpen(true)}
            handleEmployeeSelect={() => setEmployeeModalIsOpen(true)}
            handleCustomerSelect={() => setCustomerModalIsOpen(true)}
            handleProductSelect={() => setProductModalIsOpen(true)}
        />
    );
};
