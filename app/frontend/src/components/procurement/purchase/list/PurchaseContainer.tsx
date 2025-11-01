import React, { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils.ts";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import { PurchaseProvider, usePurchaseContext } from "../../../../providers/procurement/Purchase.tsx";
import { PurchaseCollection } from "./PurchaseCollection.tsx";
import { DepartmentProvider, useDepartmentContext } from "../../../../providers/master/Department.tsx";
import { EmployeeProvider, useEmployeeContext } from "../../../../providers/master/Employee.tsx";
import { VendorProvider, useVendorContext } from "../../../../providers/master/partner/Vendor.tsx";
import { ProductItemProvider, useProductItemContext } from "../../../../providers/master/product/ProductItem.tsx";
import { WarehouseProvider, useWarehouseContext } from "../../../../providers/master/Warehouse.tsx";

export const PurchaseContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchPurchases,
        } = usePurchaseContext();

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

        const {
            fetchWarehouses,
        } = useWarehouseContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        await fetchPurchases.load(),
                        fetchDepartments.load(),
                        fetchEmployees.load(),
                        fetchVendors.load(),
                        fetchProducts.load(),
                        fetchWarehouses.load(),
                    ]);
                } catch (error: any) {
                    showErrorMessage(`仕入情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <PurchaseCollection/>
                )}
            </>
        );
    };

    return (
        <PurchaseProvider>
            <DepartmentProvider>
                <EmployeeProvider>
                    <VendorProvider>
                        <ProductItemProvider>
                            <WarehouseProvider>
                                <Content />
                            </WarehouseProvider>
                        </ProductItemProvider>
                    </VendorProvider>
                </EmployeeProvider>
            </DepartmentProvider>
        </PurchaseProvider>
    );
};
