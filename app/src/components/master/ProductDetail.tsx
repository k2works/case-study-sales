import React, {useEffect, useState} from 'react';
import Modal from "react-modal";
import {showErrorMessage} from "../application/utils.ts";
import {useMessage} from "../application/Message.tsx";
import {useModal, useTab} from "../application/hooks.ts";
import {usePageNation} from "../../views/application/PageNation.tsx";
import LoadingIndicator from "../../views/application/LoadingIndicatior.tsx";
import {useFetchBoms, useFetchProducts, useProduct} from "./hooks.ts";
import {ProductType} from "../../models";
import {ProductCollectionView} from "../../views/master/ProductCollection.tsx";
import {ProductSingleView} from "../../views/master/ProductSingle.tsx";
import {ProductCollectionSelectView} from "../../views/master/ProductSelect.tsx";
import {SubstituteProductCollectionView} from "../../views/master/SubstituteProductCollection.tsx";
import {BomCollectionView} from "../../views/master/BomCollection.tsx";
import {
    CustomerSpecificSellingPriceCollectionView
} from "../../views/master/CustomerSpecificSellingPriceCollection.tsx";
import {Tab, TabList, TabPanel, Tabs} from "react-tabs";

export const ProductDetail: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const {message, setMessage, error, setError} = useMessage();
        const {pageNation, setPageNation} = usePageNation();
        const {pageNation: bomPageNation, setPageNation: setBomPageNation} = usePageNation();

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
            modalIsOpen: bomModalIsOpen,
            setModalIsOpen: setBomModalIsOpen,
            setIsEditing: setBomIsEditing,
            setEditId: setBomEditId
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

        const {
            products: boms,
            setProducts: setBoms,
            productService: bomService
        } = useProduct();

        const fetchBoms = useFetchBoms(
            setLoading,
            setBoms,
            setBomPageNation,
            setError,
            showErrorMessage,
            bomService
        );

        useEffect(() => {
            fetchProducts.load();
            fetchBoms.load();
        }, []);

        const {
            Tab,
            TabList,
            TabPanel,
            Tabs,
        } = useTab();

        // TODO:顧客マスタの作成後に実装
        const collectionView = () => {
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

            const handleCloseModal = () => {
                setError("");
                setModalIsOpen(false);
                setEditId(null);
            };

            const handleCloseProductModal = () => {
                setError("");
                setProductModalIsOpen(false);
                setProductEditId(null);
            }

            const handleCloseBomModal = () => {
                setError("");
                setBomModalIsOpen(false);
                setBomEditId(null);
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

                    <Modal
                        isOpen={bomModalIsOpen}
                        onRequestClose={handleCloseBomModal}
                        contentLabel="部品情報を入力"
                        className="modal"
                        overlayClassName="modal-overlay"
                        bodyOpenClassName="modal-open"
                    >
                        {
                            <ProductCollectionSelectView
                                products={boms}
                                handleSelect={(bom) => {
                                    const newProducts = newProduct.boms.filter((e) => e.productCode.value !== bom.productCode.value);
                                    newProducts.push({
                                        productCode: newProduct.productCode,
                                        componentCode: bom.productCode,
                                        componentQuantity: {
                                            amount: 1,
                                            unit: "個"
                                        }
                                    });
                                    setNewProduct({
                                        ...newProduct,
                                        boms: newProducts
                                    });
                                }}
                                handleClose={handleCloseBomModal}
                                pageNation={bomPageNation}
                                fetchProducts={fetchBoms.load}
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
                        <>
                            <Tabs>
                                <TabList>
                                    <Tab>代替品</Tab>
                                    <Tab>部品表</Tab>
                                    <Tab>顧客別販売単価</Tab>
                                </TabList>
                                <TabPanel>
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
                                </TabPanel>
                                <TabPanel>
                                    <BomCollectionView
                                        boms={newProduct.boms}
                                        handleAdd={() => {
                                            setMessage("");
                                            setError("");
                                            setBomIsEditing(true);
                                            setBomModalIsOpen(true);
                                        }}
                                        handleDelete={(product) => {
                                            setNewProduct({
                                                ...newProduct,
                                                boms: newProduct.boms.filter(
                                                    (bomProduct) => bomProduct.componentCode !== product.componentCode
                                                )
                                            });
                                        }}
                                    />
                                </TabPanel>
                                <TabPanel>
                                    <CustomerSpecificSellingPriceCollectionView
                                        prices={newProduct.customerSpecificSellingPrices}
                                        handleAdd={() => {
                                            setNewProduct({
                                                ...newProduct,
                                                customerSpecificSellingPrices: newProduct.customerSpecificSellingPrices.concat({
                                                    productCode: newProduct.productCode,
                                                    customerCode: "XXXX",
                                                    sellingPrice: {
                                                        amount: 0,
                                                        currency: "JPY"
                                                    }
                                                })
                                            });
                                        }}
                                        handleDelete={(price) => {
                                            setNewProduct({
                                                ...newProduct,
                                                customerSpecificSellingPrices: newProduct.customerSpecificSellingPrices.filter(
                                                    (sellingPrice) => sellingPrice.customerCode !== price.customerCode
                                                )
                                            });
                                        }}
                                    />
                                </TabPanel>
                            </Tabs>
                        </>
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
        <Content/>
    );
};
