import React, {useEffect, useState} from 'react';
import {showErrorMessage} from "../application/utils.ts";
import {useMessage} from "../application/Message.tsx";
import {useModal} from "../application/hooks.ts";
import {usePageNation} from "../../views/application/PageNation.tsx";
import {SiteLayout} from "../../views/SiteLayout.tsx";
import LoadingIndicator from "../../views/application/LoadingIndicatior.tsx";
import {ProductCategoryType, ProductType} from "../../models";

import {useFetchProductCategories, useFetchProducts, useProduct, useProductCategory} from "./hooks.ts";
import Modal from "react-modal";
import {ProductCategoryCollectionView} from "../../views/master/ProductCategoryCollection.tsx";
import {ProductCategorySingleView} from "../../views/master/ProductCategorySingle.tsx";
import {ProductCollectionListView, ProductCollectionSelectView} from "../../views/master/ProductSelect.tsx";

export const ProductCategory: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const {message, setMessage, error, setError} = useMessage();
        const {pageNation, setPageNation} = usePageNation();

        const {
            modalIsOpen,
            setModalIsOpen,
            isEditing,
            setIsEditing,
            editId,
            setEditId
        } = useModal();

        const {
            modalIsOpen: productModalIsOpen,
            setModalIsOpen: setProductModalIsOpen,
            setIsEditing: setProductIsEditing,
            setEditId: setProductEditId
        } = useModal();

        const {
            initialProductCategory,
            productCategories,
            setProductCategories,
            newProductCategory,
            setNewProductCategory,
            searchProductCategoryCode,
            setSearchProductCategoryCode,
            productCategoryService
        } = useProductCategory();

        const fetchProductCategories = useFetchProductCategories(
            setLoading,
            setProductCategories,
            setPageNation,
            setError,
            showErrorMessage,
            productCategoryService
        );
        const {
            products,
            setProducts,
            productService
        } = useProduct();

        const fetchProducts = useFetchProducts(
            setLoading,
            setProducts,
            setPageNation,
            setError,
            showErrorMessage,
            productService
        );

        useEffect(() => {
            fetchProductCategories.load().then(() => {
                    fetchProducts.load().then();
                }
            );
        }, []);

        const handleOpenModal = (productCategory?: ProductCategoryType) => {
            setMessage("");
            setError("");
            if (productCategory) {
                setNewProductCategory(productCategory);
                setEditId(productCategory.productCategoryCode.value);
                setIsEditing(true);
            } else {
                setNewProductCategory(initialProductCategory);
                setIsEditing(false);
            }
            setModalIsOpen(true);
        };

        const handleCloseModal = () => {
            setError("");
            setModalIsOpen(false);
            setEditId(null);
        };

        const collectionView = () => {
            const handleSearchProductCategory = async () => {
                if (!searchProductCategoryCode.trim()) return;
                setLoading(true);
                try {
                    const fetchedProductCategory = await productCategoryService.find(searchProductCategoryCode.trim());
                    setProductCategories(fetchedProductCategory ? [fetchedProductCategory] : []);
                    setMessage("");
                    setError("");
                } catch (error: any) {
                    showErrorMessage(`製品カテゴリの検索に失敗しました: ${error?.message}`, setError);
                } finally {
                    setLoading(false);
                }
            };

            const handleDeleteProductCategory = async (productCategoryCode: string) => {
                try {
                    if (!window.confirm(`製品カテゴリコード:${productCategoryCode} を削除しますか？`)) return;
                    await productCategoryService.destroy(productCategoryCode);
                    await fetchProductCategories.load();
                    setMessage("製品カテゴリを削除しました。");
                } catch (error: any) {
                    showErrorMessage(`製品カテゴリの削除に失敗しました: ${error?.message}`, setError);
                }
            };

            const handleCheckProductCategory = (productCategory: ProductCategoryType) => {
                const newProductCategories = productCategories.map((cat) => {
                    if (cat.productCategoryCode.value === productCategory.productCategoryCode.value) {
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
                    setError("削除する製品カテゴリを選択してください。");
                    return;
                }
                try {
                    if (!window.confirm(`選択した製品カテゴリを削除しますか？`)) return;
                    const checkedProductCategories = productCategories.filter((cat) => cat.checked);
                    await Promise.all(checkedProductCategories.map((cat) => productCategoryService.destroy(cat.productCategoryCode.value)));
                    await fetchProductCategories.load();
                    setMessage("選択した製品カテゴリを削除しました。");
                } catch (error: any) {
                    showErrorMessage(`製品カテゴリの削除に失敗しました: ${error?.message}`, setError);
                }
            }

            return (
                <>
                    <Modal
                        isOpen={modalIsOpen}
                        onRequestClose={handleCloseModal}
                        contentLabel="製品カテゴリ情報を入力"
                        className="modal"
                        overlayClassName="modal-overlay"
                        bodyOpenClassName="modal-open"
                    >
                        {singleView()}
                    </Modal>
                    <ProductCategoryCollectionView
                        error={error}
                        message={message}
                        searchProductCategoryCode={searchProductCategoryCode}
                        setSearchProductCategoryCode={setSearchProductCategoryCode}
                        handleSearchProductCategory={handleSearchProductCategory}
                        handleOpenModal={handleOpenModal}
                        productCategories={productCategories}
                        handleDeleteProductCategory={handleDeleteProductCategory}
                        handleCheckProductCategory={handleCheckProductCategory}
                        handleCheckToggleCollection={handleCheckAllProductCategories}
                        handleDeleteCheckedCollection={handleDeleteCheckedProductCategories}
                        pageNation={pageNation}
                        fetchProductCategories={fetchProductCategories.load}
                    />
                </>
            )
        };

        const singleView = () => {
            const handleCreateOrUpdateProductCategory = async () => {
                const validateProductCategory = (): boolean => {
                    if (!newProductCategory.productCategoryCode.value.trim() || !newProductCategory.productCategoryName.trim()) {
                        setError("製品カテゴリコード、カテゴリ名は必須項目です。");
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
                        setMessage("製品カテゴリを更新しました。");
                    } else {
                        setMessage("製品カテゴリを作成しました。");
                    }
                    handleCloseModal();
                } catch (error: any) {
                    showErrorMessage(`製品カテゴリの作成に失敗しました: ${error?.message}`, setError);
                }
            };

            const handleCloseProductModal = () => {
                setError("");
                setProductModalIsOpen(false);
                setProductEditId(null);
            }

            return (
                <>
                    <Modal
                        isOpen={productModalIsOpen}
                        onRequestClose={handleCloseProductModal}
                        contentLabel="商品情報を入力"
                        className="modal"
                        overlayClassName="modal-overlay"
                        bodyOpenClassName="modal-open"
                    >
                        {
                            <ProductCollectionSelectView
                                products={products}
                                handleSelect={(product) => {
                                    const newProducts = newProductCategory.products.filter((e) => e.productCode.value !== product.productCode.value);
                                    if (product.productCode.value) {
                                        newProducts.push({
                                            ...product,
                                            addFlag: true,
                                            deleteFlag: false
                                        });
                                    }
                                    setNewProductCategory({
                                        ...newProductCategory,
                                        products: newProducts
                                    });
                                }}
                                handleClose={handleCloseProductModal}
                                pageNation={pageNation}
                                fetchProducts={fetchProducts.load}
                            />
                        }
                    </Modal>

                    <ProductCategorySingleView
                        error={error}
                        message={message}
                        newProductCategory={newProductCategory}
                        setNewProductCategory={setNewProductCategory}
                        isEditing={isEditing}
                        handleCreateOrUpdateProductCategory={handleCreateOrUpdateProductCategory}
                        handleCloseModal={handleCloseModal}
                    />

                    {isEditing && (
                        <ProductCollectionListView
                            products={newProductCategory.products.filter((e) => !e.deleteFlag)}
                            handleAdd={() => {
                                setMessage("");
                                setError("");
                                setProductIsEditing(true);
                                setProductModalIsOpen(true);
                            }}
                            handleDelete={(product: ProductType) => {
                                const newProducts = newProductCategory.products.filter((e) => e.productCode.value !== product.productCode.value);
                                if (product.productCode.value) {
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
        };

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    collectionView()
                )}
            </>
        );
    };

    return (
        <SiteLayout>
            <Content/>
        </SiteLayout>
    );
};
