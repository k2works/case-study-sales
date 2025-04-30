import React, { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils.ts";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import { ShippingProvider, useShippingContext } from "../../../../providers/sales/Shipping.tsx";
import { DepartmentProvider, useDepartmentContext } from "../../../../providers/master/Department.tsx";
import { EmployeeProvider, useEmployeeContext } from "../../../../providers/master/Employee.tsx";
import { CustomerProvider, useCustomerContext } from "../../../../providers/master/partner/Customer.tsx";
import { ProductItemProvider, useProductItemContext } from "../../../../providers/master/product/ProductItem.tsx";
import { ShippingConfirmCollection } from "./ShippingConfirmCollection.tsx";

export const ShippingConfirmContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchShippings,
            searchShippingCriteria
        } = useShippingContext();

        const {
            fetchDepartments,
        } = useDepartmentContext();

        const {
            fetchEmployees,
        } = useEmployeeContext();

        const {
            fetchCustomers,
        } = useCustomerContext();

        const {
            fetchProducts,
        } = useProductItemContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        await fetchShippings.load(1, searchShippingCriteria),
                        fetchDepartments.load(),
                        fetchEmployees.load(),
                        fetchCustomers.load(),
                        fetchProducts.load(),
                    ]);
                } catch (error: unknown) {
                    const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
                    showErrorMessage(`出荷情報の取得に失敗しました: ${errorMessage}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <ShippingConfirmCollection/>
                )}
            </>
        );
    };

    return (
        <ShippingProvider>
            <DepartmentProvider>
                <EmployeeProvider>
                    <CustomerProvider>
                        <ProductItemProvider>
                            <Content />
                        </ProductItemProvider>
                    </CustomerProvider>
                </EmployeeProvider>
            </DepartmentProvider>
        </ShippingProvider>
    );
};