import React, {useEffect, } from 'react';
import {showErrorMessage} from "../../../application/utils.ts";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import {useProductCategoryContext, ProductCategoryProvider} from "../../../../providers/ProductCategory.tsx";
import {ProductItemProvider, useProductItemContext} from "../../../../providers/ProductItem.tsx";
import {ProductCategoryCollection} from "./ProductCategoryCollection.tsx";

export const ProductCategoryContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchProductCategories,
        } = useProductCategoryContext();

        const {
            fetchProducts,
        } = useProductItemContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchProductCategories.load(),
                        fetchProducts.load()
                    ]);
                } catch (error: any) {
                    showErrorMessage(`商品分類情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <ProductCategoryCollection/>
                )}
            </>
        );
    };

    return (
        <ProductCategoryProvider>
            <ProductItemProvider>
                <Content/>
            </ProductItemProvider>
        </ProductCategoryProvider>
    );
};
