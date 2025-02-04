import React, {useEffect, useState} from 'react';
import {showErrorMessage} from "../../application/utils.ts";
import {useMessage} from "../../application/Message.tsx";
import {useModal} from "../../application/hooks.ts";
import {usePageNation} from "../../../views/application/PageNation.tsx";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import {ProductCategoryCriteriaType, ProductCategoryType, ProductType} from "../../../models/master/product";

import {useFetchProductCategories, useFetchProducts, useProduct, useProductCategory} from "../hooks/product";
import Modal from "react-modal";
import {ProductCollectionAddListView} from "../../../views/master/product/item/ProductCollection.tsx";
import {ProductCollectionSelectView} from "../../../views/master/product/item/ProductSelect.tsx";
import {ProductCategorySearchSingleView} from "../../../views/master/product/ProductCategorySearch.tsx";
import {ProductCategoryCollectionView} from "../../../views/master/product/category/ProductCategoryCollection.tsx";
import {ProductCategorySingleView} from "../../../views/master/product/category/ProductCategorySingle.tsx";

export const ProductCategory: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const {message, setMessage, error, setError} = useMessage();
        const {pageNation, setPageNation, criteria, setCriteria} = usePageNation<ProductCategoryCriteriaType>();
        const {modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen,} = useModal();

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
            searchProductCategoryCriteria,
            setSearchProductCategoryCriteria,
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

        const modalView = () => {
            const editModal = () => {
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

                const editModalView = () => {
                    return (
                        <Modal
                            isOpen={modalIsOpen}
                            onRequestClose={handleCloseModal}
                            contentLabel="商品分類情報を入力"
                            className="modal"
                            overlayClassName="modal-overlay"
                            bodyOpenClassName="modal-open"
                        >
                            {singleView()}

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
                        </Modal>
                    )
                }

                return {
                    handleOpenModal,
                    handleCloseModal,
                    editModalView
                }
            }

            const productModal = () => {
                const handleCloseProductModal = () => {
                    setError("");
                    setProductModalIsOpen(false);
                    setProductEditId(null);
                }

                const productModalView = () => {
                    return (
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

                    )
                }

                return {
                    productModalView,
                }
            }

            const searchModal = () => {
                const handleOpenSearchModal = () => {
                    setSearchModalIsOpen(true);
                }

                const handleCloseSearchModal = () => {
                    setSearchModalIsOpen(false);
                }

                const searchModalView = () => {
                    return (
                        <Modal
                            isOpen={searchModalIsOpen}
                            onRequestClose={handleCloseSearchModal}
                            contentLabel="検索情報を入力"
                            className="modal"
                            overlayClassName="modal-overlay"
                            bodyOpenClassName="modal-open"
                        >
                            {
                                    <ProductCategorySearchSingleView
                                        criteria={searchProductCategoryCriteria}
                                        setCondition={setSearchProductCategoryCriteria}
                                        handleSelect={async () => {
                                            if (!searchProductCategoryCriteria) {
                                                return;
                                            }
                                            setLoading(true);
                                            try {
                                                const result = await productCategoryService.search(searchProductCategoryCriteria);
                                                setProductCategories(result ? result.list : []);
                                                if (result.list.length === 0) {
                                                    showErrorMessage(`検索結果は0件です`, setError);
                                                } else {
                                                    setCriteria(searchProductCategoryCriteria);
                                                    setPageNation(result);
                                                    setMessage("");
                                                    setError("");
                                                }
                                            } catch (error: any) {
                                                showErrorMessage(`実行履歴情報の検索に失敗しました: ${error?.message}`, setError);
                                            } finally {
                                                setLoading(false);
                                            }
                                        }}
                                        handleClose={handleCloseSearchModal}
                                    />
                            }
                        </Modal>
                    )
                }

                return {
                    searchModalView,
                    handleOpenSearchModal,
                    handleCloseSearchModal
                }
            }

            const init = () => {
                return (
                    <>
                        {editModal().editModalView()}
                        {productModal().productModalView()}
                        {searchModal().searchModalView()}
                    </>
                )
            }

            return {
                searchModal,
                editModal,
                init
            }
        }

        const collectionView = () => {
            const {handleOpenModal} = modalView().editModal()
            const {handleOpenSearchModal} = modalView().searchModal()

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
                    setError("削除する商品分類を選択してください。");
                    return;
                }
                try {
                    if (!window.confirm(`選択した商品分類を削除しますか？`)) return;
                    const checkedProductCategories = productCategories.filter((cat) => cat.checked);
                    await Promise.all(checkedProductCategories.map((cat) => productCategoryService.destroy(cat.productCategoryCode.value)));
                    await fetchProductCategories.load();
                    setMessage("選択した商品分類を削除しました。");
                } catch (error: any) {
                    showErrorMessage(`商品分類の削除に失敗しました: ${error?.message}`, setError);
                }
            }

            return (
                    <ProductCategoryCollectionView
                        error={error}
                        message={message}
                        searchItems={{searchProductCategoryCriteria, setSearchProductCategoryCriteria, handleOpenSearchModal}}
                        headerItems={{handleOpenModal, handleCheckToggleCollection:handleCheckAllProductCategories, handleDeleteCheckedCollection:handleDeleteCheckedProductCategories}}
                        collectionItems={{productCategories, handleDeleteProductCategory, handleCheckProductCategory}}
                        pageNationItems={{pageNation, fetchProductCategories: fetchProductCategories.load, criteria}}
                    />
            )
        };

        const singleView = () => {
            const {handleCloseModal} = modalView().editModal();

            const handleCreateOrUpdateProductCategory = async () => {
                const validateProductCategory = (): boolean => {
                    if (!newProductCategory.productCategoryCode.value.trim() || !newProductCategory.productCategoryName.trim()) {
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
                    <ProductCategorySingleView
                        error={error}
                        message={message}
                        newProductCategory={newProductCategory}
                        setNewProductCategory={setNewProductCategory}
                        isEditing={isEditing}
                        handleCreateOrUpdateProductCategory={handleCreateOrUpdateProductCategory}
                        handleCloseModal={handleCloseModal}
                    />
            );
        };

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <>
                        {modalView().init()}
                        {collectionView()}
                    </>

                )}
            </>
        );
    };

    return (
        <Content/>
    );
};
