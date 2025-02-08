import React from "react";
import {useProductItemContext} from "../../../../providers/master/product/ProductItem.tsx";
import {showErrorMessage} from "../../../application/utils.ts";
import {ProductType} from "../../../../models/master/product";
import {ProductCollectionView} from "../../../../views/master/product/item/ProductCollection.tsx";
import {ProductItemSearchModal} from "./ProductItemSearchModal.tsx";
import {ProductItemEditModal} from "./ProductItemEditModal.tsx";

export const ProductItemCollection: React.FC = () => {
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
        initialProduct,
        products,
        setProducts,
        setNewProduct,
        searchProductCriteria,
        setSearchProductCriteria,
        fetchProducts,
        productService
    } = useProductItemContext();

    const handleOpenModal = (product?: ProductType) => {
        setMessage("");
        setError("");
        if (product) {
            setNewProduct(product);
            setEditId(product.productCode.value);
            setIsEditing(true);
        } else {
            setNewProduct(initialProduct);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    }

    const handleDeleteProduct = async (productCode: string) => {
        try {
            if (!window.confirm(`商品コード:${productCode} を削除しますか？`)) return;
            await productService.destroy(productCode);
            await fetchProducts.load();
            setMessage("商品を削除しました。");
        } catch (error: any) {
            showErrorMessage(`商品の削除に失敗しました: ${error?.message}`, setError);
        }
    };

    const handleCheckProduct = (product: ProductType) => {
        const newProducts = products.map((prod) => {
            if (prod.productCode.value === product.productCode.value) {
                return {
                    ...prod,
                    checked: !prod.checked
                };
            }
            return prod;
        });
        setProducts(newProducts);
    }

    const handleCheckAllProducts = () => {
        const newProducts = products.map((prod) => {
            return {
                ...prod,
                checked: !products.every((prod) => prod.checked)
            };
        });
        setProducts(newProducts);
    }

    const handleDeleteCheckedProducts = async () => {
        if (!products.some((prod) => prod.checked)) {
            setError("削除する商品を選択してください。");
            return;
        }
        try {
            if (!window.confirm(`選択した商品を削除しますか？`)) return;
            const checkedProducts = products.filter((prod) => prod.checked);
            await Promise.all(checkedProducts.map((prod) => productService.destroy(prod.productCode.value)));
            await fetchProducts.load();
            setMessage("選択した商品を削除しました。");
        } catch (error: any) {
            showErrorMessage(`商品の削除に失敗しました: ${error?.message}`, setError);
        }
    }

    return (
        <>
            <ProductItemSearchModal/>
            <ProductItemEditModal/>
            <ProductCollectionView
                error={error}
                message={message}
                searchItems={{searchProductCriteria, setSearchProductCriteria, handleOpenSearchModal}}
                headerItems={{handleOpenModal, handleCheckToggleCollection: handleCheckAllProducts, handleDeleteCheckedCollection: handleDeleteCheckedProducts}}
                contentItems={{products, handleDeleteProduct, handleCheckProduct}}
                pageNationItems={{pageNation, fetchProducts: fetchProducts.load, criteria}}
            />
        </>
    )
}
