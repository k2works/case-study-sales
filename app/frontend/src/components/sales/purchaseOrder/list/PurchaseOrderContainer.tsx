import React, { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils.ts";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import { PurchaseOrderProvider, usePurchaseOrderContext } from "../../../../providers/sales/PurchaseOrder.tsx";
import { PurchaseOrderCollection } from "./PurchaseOrderCollection.tsx";
import { DepartmentProvider, useDepartmentContext } from "../../../../providers/master/Department.tsx";
import { EmployeeProvider, useEmployeeContext } from "../../../../providers/master/Employee.tsx";
import { VendorProvider, useVendorContext } from "../../../../providers/master/partner/Vendor.tsx";
import { ProductItemProvider, useProductItemContext } from "../../../../providers/master/product/ProductItem.tsx";

export const PurchaseOrderContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchPurchaseOrders,
        } = usePurchaseOrderContext();

        const {
            fetchDepartments,
        } = useDepartmentContext();

        const {
            fetchEmployees,
        } = useEmployeeContext();

        const {
            fetchVendors,
        } = useVendorContext();

        const {
            fetchProducts,
        } = useProductItemContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        await fetchPurchaseOrders.load(),
                        fetchDepartments.load(),
                        fetchEmployees.load(),
                        fetchVendors.load(),
                        fetchProducts.load(),
                    ]);
                } catch (error: any) {
                    showErrorMessage(`発注情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <PurchaseOrderCollection/>
                )}
            </>
        );
    };

    return (
        <PurchaseOrderProvider>
            <DepartmentProvider>
                <EmployeeProvider>
                    <VendorProvider>
                        <ProductItemProvider>
                            <Content />
                        </ProductItemProvider>
                    </VendorProvider>
                </EmployeeProvider>
            </DepartmentProvider>
        </PurchaseOrderProvider>
    );
};