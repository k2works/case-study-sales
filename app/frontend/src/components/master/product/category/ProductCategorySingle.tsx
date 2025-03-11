import React from "react";
import {useProductCategoryContext} from "../../../../providers/master/product/ProductCategory.tsx";
import {useProductItemContext} from "../../../../providers/master/product/ProductItem.tsx";
import {showErrorMessage} from "../../../application/utils.ts";
import {ProductCategorySingleView} from "../../../../views/master/product/category/ProductCategorySingle.tsx";
import {ProductCollectionAddListView} from "../../../../views/master/product/item/ProductCollection.tsx";
import {ProductType} from "../../../../models/master/product";
import {ProductItemSelectModal} from "./ProductItemSelectModal.tsx";

export const ProductCategorySingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        editId,
        setEditId,
        initialProductCategory,
        newProductCategory,
        setNewProductCategory,
        fetchProductCategories,
        productCategoryService,
    } = useProductCategoryContext();

    const {
        setModalIsOpen: setProductModalIsOpen,
        setIsEditing: setProductIsEditing,
    } = useProductItemContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdateProductCategory = async () => {
        const validateProductCategory = (): boolean => {
            if (!newProductCategory.productCategoryCode.trim() || !newProductCategory.productCategoryName.trim()) {
                setError("商品分類コード、カテゴリ名は必須項目です。");
                return false;
            }
            return true;
        };
        if (!validateProductCategory()) return;
        try {
            if (isEditing && editId) {
                await productCategoryService.update(newProductCategory);
            } else {
                await productCategoryService.create(newProductCategory);
            }
            setNewProductCategory({...initialProductCategory});
            await fetchProductCategories.load();
            if (isEditing) {
                setMessage("商品分類を更新しました。");
            } else {
                setMessage("商品分類を作成しました。");
            }
            handleCloseModal();
        } catch (error: any) {
            showErrorMessage(`商品分類の作成に失敗しました: ${error?.message}`, setError);
        }
    };

    return (
        <>
            <ProductCategorySingleView
                error={error}
                message={message}
                newProductCategory={newProductCategory}
                setNewProductCategory={setNewProductCategory}
                isEditing={isEditing}
                handleCreateOrUpdateProductCategory={handleCreateOrUpdateProductCategory}
                handleCloseModal={handleCloseModal}
            />

            <ProductItemSelectModal/>

            {isEditing && (
                <ProductCollectionAddListView
                    products={newProductCategory.products.filter((e) => !e.deleteFlag)}
                    handleAdd={() => {
                        setMessage("");
                        setError("");
                        setProductIsEditing(true);
                        setProductModalIsOpen(true);
                    }}
                    handleDelete={(product: ProductType) => {
                        const newProducts = newProductCategory.products.filter((e) => e.productCode !== product.productCode);
                        if (product.productCode) {
                            newProducts.push({
                                ...product,
                                addFlag: false,
                                deleteFlag: true
                            });
                        }
                        setNewProductCategory({
                            ...newProductCategory,
                            products: newProducts
                        });
                    }}
                />
            )
            }
        </>
    );
}
