import React from "react";
import {useCustomerContext} from "../../../../providers/master/partner/Customer.tsx";
import {CustomerType} from "../../../../models/master/partner";
import {showErrorMessage} from "../../../application/utils.ts";
import {CustomerCollectionView} from "../../../../views/master/partner/customer/CustomerCollection.tsx";
import {CustomerSearchModal} from "./CustomerSearchModal.tsx";
import {CustomerEditModal} from "./CustomerEditModal.tsx";

export const CustomerCollection: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        pageNation,
        criteria,
        setSearchModalIsOpen,
        setModalIsOpen,
        setIsEditing,
        setEditId,
        initialCustomer,
        customers,
        setCustomers,
        setNewCustomer,
        searchCustomerCriteria,
        setSearchCustomerCriteria,
        fetchCustomers,
        customerService
    } = useCustomerContext();

    const handleOpenModal = (customer?: CustomerType) => {
        setMessage("");
        setError("");
        if (customer) {
            setNewCustomer(customer);
            setEditId(customer.customerCode);
            setIsEditing(true);
        } else {
            setNewCustomer(initialCustomer);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    };

    const handleDeleteCustomer = async (customerCode: string, branchNumber: number) => {
        try {
            if (!window.confirm(`顧客コード:${customerCode} 枝番:${branchNumber} を削除しますか？`)) return;
            await customerService.destroy(customerCode, branchNumber);
            await fetchCustomers.load();
            setMessage("顧客を削除しました。");
        } catch (error: any) {
            showErrorMessage(`顧客の削除に失敗しました: ${error?.message}`, setError);
        }
    };

    const handleCheckCustomer = (customer: CustomerType) => {
        const newCustomers = customers.map((c) => {
            if (c.customerCode === customer.customerCode && c.customerBranchNumber === customer.customerBranchNumber) {
                return { ...c, checked: !c.checked };
            }
            return c;
        });
        setCustomers(newCustomers);
    }

    const handleCheckAllCustomers = () => {
        const newCustomers = customers.map((c) => {
            return { ...c, checked: !c.checked };
        });
        setCustomers(newCustomers);
    }

    const handleDeleteCheckCollection = async () => {
        const checkedCustomers = customers.filter((c) => c.checked);
        if (checkedCustomers.length === 0) {
            showErrorMessage("削除する顧客を選択してください", setError);
            return;
        }
        try {
            if (!window.confirm("選択した顧客を削除しますか？")) return;
            await Promise.all(
                checkedCustomers.map((customer) =>
                    customerService.destroy(customer.customerCode, customer.customerBranchNumber)
                )
            );
            await fetchCustomers.load();
            setMessage("選択した顧客を削除しました。");
        } catch (error: any) {
            showErrorMessage(`選択した顧客の削除に失敗しました: ${error?.message}`, setError);
        }
    }

    return (
        <>
            <CustomerSearchModal/>
            <CustomerEditModal/>
            <CustomerCollectionView
                error={error}
                message={message}
                searchItems={{
                    searchCustomerCriteria,
                    setSearchCustomerCriteria,
                    handleOpenSearchModal,
                }}
                headerItems={{
                    handleOpenModal,
                    handleCheckToggleCollection: handleCheckAllCustomers,
                    handleDeleteCheckedCollection: handleDeleteCheckCollection,
                }}
                collectionItems={{
                    customers,
                    handleOpenModal,
                    handleDeleteCustomer,
                    handleCheckCustomer,
                }}
                pageNationItems={{
                    pageNation,
                    fetchCustomers: fetchCustomers.load,
                    criteria,
                }}
            />
        </>
    );
}
