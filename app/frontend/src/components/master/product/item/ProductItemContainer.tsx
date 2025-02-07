import React, {useEffect} from 'react';
import {showErrorMessage} from "../../../application/utils.ts";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import {ProductItemProvider, useProductItemContext} from "../../../../providers/ProductItem.tsx";
import {ProductSubstituteProvider, useProductSubstituteContext} from "../../../../providers/ProductSubstitute.tsx";
import {ProductBomProvider, useProductBomContext} from "../../../../providers/ProductBom.tsx";
import {ProductCategoryProvider, useProductCategoryContext} from "../../../../providers/ProductCategory.tsx";
import {CustomerProvider, useCustomerContext} from "../../../../providers/Customer.tsx";
import {useVendorContext, VendorProvider} from "../../../../providers/Vendor.tsx";
import {ProductItemCollection} from "./ProductItemCollection.tsx";

export const ProductItemContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchProducts,
        } = useProductItemContext();

        const {
            fetchSubstitutes,
        } = useProductSubstituteContext();

        const {
            fetchBoms,
        } = useProductBomContext();

        const {
            fetchProductCategories,
        } = useProductCategoryContext();

        const {
            fetchCustomers,
        } = useCustomerContext();

        const {
            fetchVendors,
        } = useVendorContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchProducts.load(),
                        fetchSubstitutes.load(),
                        fetchBoms.load(),
                        fetchProductCategories.load(),
                        fetchCustomers.load(),
                        fetchVendors.load()
                    ]);
                } catch (error: any) {
                    showErrorMessage(`商品情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <ProductItemCollection/>
                )}
            </>
        );
    };

    return (
        <ProductItemProvider>
            <ProductCategoryProvider>
                <ProductSubstituteProvider>
                    <ProductBomProvider>
                        <CustomerProvider>
                            <VendorProvider>
                                <Content/>
                            </VendorProvider>
                        </CustomerProvider>
                    </ProductBomProvider>
                </ProductSubstituteProvider>
            </ProductCategoryProvider>
        </ProductItemProvider>
    );
};
