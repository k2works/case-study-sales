import React from "react";
import {useProductCategoryContext} from "../../../../providers/master/product/ProductCategory.tsx";
import {showErrorMessage} from "../../../application/utils.ts";
import {ProductCategoryType} from "../../../../models/master/product";
import {ProductCategoryCollectionView} from "../../../../views/master/product/category/ProductCategoryCollection.tsx";
import {ProductCategorySearchModal} from "./ProductCategorySearchModal.tsx";
import {ProductCategoryEditModal} from "./ProductCategoryEditModal.tsx";

export const ProductCategoryCollection: React.FC = () => {
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
        initialProductCategory,
        productCategories,
        setProductCategories,
        setNewProductCategory,
        searchProductCategoryCriteria,
        setSearchProductCategoryCriteria,
        fetchProductCategories,
        productCategoryService,
    } = useProductCategoryContext();

    const handleOpenModal = (productCategory?: ProductCategoryType) => {
        setMessage("");
        setError("");
        if (productCategory) {
            setNewProductCategory(productCategory);
            setEditId(productCategory.productCategoryCode);
            setIsEditing(true);
        } else {
            setNewProductCategory(initialProductCategory);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    }

    const handleDeleteProductCategory = async (productCategoryCode: string) => {
        try {
            if (!window.confirm(`商品分類コード:${productCategoryCode} を削除しますか？`)) return;
            await productCategoryService.destroy(productCategoryCode);
            await fetchProductCategories.load();
            setMessage("商品分類を削除しました。");
        } catch (error: any) {
            showErrorMessage(`商品分類の削除に失敗しました: ${error?.message}`, setError);
        }
    };

    const handleCheckProductCategory = (productCategory: ProductCategoryType) => {
        const newProductCategories = productCategories.map((cat) => {
            if (cat.productCategoryCode === productCategory.productCategoryCode) {
                return {
                    ...cat,
                    checked: !cat.checked
                };
            }
            return cat;
        });
        setProductCategories(newProductCategories);
    }

    const handleCheckAllProductCategories = () => {
        const newProductCategories = productCategories.map((cat) => {
            return {
                ...cat,
                checked: !productCategories.every((cat) => cat.checked)
            };
        });
        setProductCategories(newProductCategories);
    }

    const handleDeleteCheckedProductCategories = async () => {
        if (!productCategories.some((cat) => cat.checked)) {
            setError("削除する商品分類を選択してください。");
            return;
        }
        try {
            if (!window.confirm(`選択した商品分類を削除しますか？`)) return;
            const checkedProductCategories = productCategories.filter((cat) => cat.checked);
            await Promise.all(checkedProductCategories.map((cat) => productCategoryService.destroy(cat.productCategoryCode)));
            await fetchProductCategories.load();
            setMessage("選択した商品分類を削除しました。");
        } catch (error: any) {
            showErrorMessage(`商品分類の削除に失敗しました: ${error?.message}`, setError);
        }
    }

    return (
        <>
            <ProductCategorySearchModal/>
            <ProductCategoryEditModal/>
            <ProductCategoryCollectionView
                error={error}
                message={message}
                searchItems={{searchProductCategoryCriteria, setSearchProductCategoryCriteria, handleOpenSearchModal}}
                headerItems={{handleOpenModal, handleCheckToggleCollection:handleCheckAllProductCategories, handleDeleteCheckedCollection:handleDeleteCheckedProductCategories}}
                collectionItems={{productCategories, handleDeleteProductCategory, handleCheckProductCategory}}
                pageNationItems={{pageNation, fetchProductCategories: fetchProductCategories.load, criteria}}
            />
        </>
    )
}
