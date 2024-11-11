import React, {useEffect, useState} from 'react';
import Modal from "react-modal";
import {showErrorMessage} from "../application/utils.ts";
import {useMessage} from "../application/Message.tsx";
import {useModal} from "../application/hooks.ts";
import {usePageNation} from "../../views/application/PageNation.tsx";
import {SiteLayout} from "../../views/SiteLayout.tsx";
import LoadingIndicator from "../../views/application/LoadingIndicatior.tsx";
import {useFetchProducts, useProduct} from "./hooks.ts";
import {ProductType} from "../../models";
import {ProductCollectionView} from "../../views/master/ProductCollection.tsx";
import {ProductSingleView} from "../../views/master/ProductSingle.tsx";
import {SubstituteProductCollectionView} from "../../views/master/SubstituteProductSelect.tsx";
import {ProductCollectionSelectView} from "../../views/master/ProductSelect.tsx";

export const Product: React.FC = () => {
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
            initialProduct,
            products,
            setProducts,
            newProduct,
            setNewProduct,
            searchProductCode,
            setSearchProductCode,
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
            fetchProducts.load();
        }, []);

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

        const handleCloseModal = () => {
            setError("");
            setModalIsOpen(false);
            setEditId(null);
        };

        const collectionView = () => {
            const handleSearchProduct = async () => {
                if (!searchProductCode.trim()) return;
                setLoading(true);
                try {
                    const fetchedProduct = await productService.find(searchProductCode.trim());
                    setProducts(fetchedProduct ? [fetchedProduct] : []);
                    setMessage("");
                    setError("");
                } catch (error: any) {
                    showErrorMessage(`商品の検索に失敗しました: ${error?.message}`, setError);
                } finally {
                    setLoading(false);
                }
            };

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
                    <Modal
                        isOpen={modalIsOpen}
                        onRequestClose={handleCloseModal}
                        contentLabel="商品情報を入力"
                        className="modal"
                        overlayClassName="modal-overlay"
                        bodyOpenClassName="modal-open"
                    >
                        {singleView()}
                    </Modal>
                    <ProductCollectionView
                        error={error}
                        message={message}
                        searchProductCode={searchProductCode}
                        setSearchProductCode={setSearchProductCode}
                        handleSearchProduct={handleSearchProduct}
                        handleOpenModal={handleOpenModal}
                        products={products}
                        handleDeleteProduct={handleDeleteProduct}
                        handleCheckProduct={handleCheckProduct}
                        handleCheckToggleCollection={handleCheckAllProducts}
                        handleDeleteCheckedCollection={handleDeleteCheckedProducts}
                        pageNation={pageNation}
                        fetchProducts={fetchProducts.load}
                    />
                </>
            )
        };

        const singleView = () => {
            const handleCreateOrUpdateProduct = async () => {
                const validateProduct = (): boolean => {
                    if (!newProduct.productCode.value.trim() || !newProduct.productName.productFormalName.trim()) {
                        setError("商品コード、商品名は必須項目です。");
                        return false;
                    }
                    return true;
                };
                if (!validateProduct()) return;
                try {
                    if (isEditing && editId) {
                        await productService.update(newProduct);
                    } else {
                        await productService.create(newProduct);
                    }
                    setNewProduct({...initialProduct});
                    await fetchProducts.load();
                    setMessage(isEditing ? "商品を更新しました。" : "商品を作成しました。");
                    handleCloseModal();
                } catch (error: any) {
                    showErrorMessage(`商品の作成に失敗しました: ${error?.message}`, setError);
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
                                    const newProducts = newProduct.substituteProduct.filter((e) => e.substituteProductCode.value !== product.productCode.value);
                                    newProducts.push({
                                        productCode: newProduct.productCode,
                                        substituteProductCode: product.productCode,
                                        priority: 0
                                    });
                                    setNewProduct({
                                        ...newProduct,
                                        substituteProduct: newProducts
                                    });
                                }}
                                handleClose={handleCloseProductModal}
                                pageNation={pageNation}
                                fetchProducts={fetchProducts.load}
                            />
                        }
                    </Modal>

                    <ProductSingleView
                        error={error}
                        message={message}
                        newProduct={newProduct}
                        setNewProduct={setNewProduct}
                        isEditing={isEditing}
                        handleCreateOrUpdateProduct={handleCreateOrUpdateProduct}
                        handleCloseModal={handleCloseModal}
                    />

                    {isEditing && (
                        <SubstituteProductCollectionView
                            substituteProducts={newProduct.substituteProduct}
                            handleAdd={() => {
                                setMessage("");
                                setError("");
                                setProductIsEditing(true);
                                setProductModalIsOpen(true);
                            }}
                            handleDelete={(product) => {
                                setNewProduct({
                                    ...newProduct,
                                    substituteProduct: newProduct.substituteProduct.filter(
                                        (substituteProduct) => substituteProduct.substituteProductCode !== product.substituteProductCode
                                    )
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
