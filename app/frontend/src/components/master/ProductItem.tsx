import React, {useEffect, useState} from 'react';
import Modal from "react-modal";
import {showErrorMessage} from "../application/utils.ts";
import {useMessage} from "../application/Message.tsx";
import {useModal, useTab} from "../application/hooks.ts";
import {usePageNation} from "../../views/application/PageNation.tsx";
import LoadingIndicator from "../../views/application/LoadingIndicatior.tsx";
import {useFetchBoms, useFetchProducts, useFetchSubstitutes, useProduct} from "./hooks.ts";
import {ProductCriteriaType, ProductType} from "../../models";
import {ProductType} from "../../models";
import {ProductCollectionView} from "../../views/master/product/ProductCollection.tsx";
import {ProductSingleView} from "../../views/master/product/ProductSingle.tsx";
import {ProductCollectionSelectView} from "../../views/master/product/ProductSelect.tsx";
import {SubstituteProductCollectionView} from "../../views/master/product/SubstituteProductCollection.tsx";
import {BomCollectionView} from "../../views/master/product/BomCollection.tsx";
import {
    CustomerSpecificSellingPriceCollectionView
} from "../../views/master/product/CustomerSpecificSellingPriceCollection.tsx";
import {ProductSearchSingleView} from "../../views/master/product/ProductSearch.tsx";

export const ProductItem: React.FC = () => {
    const Content: React.FC = () => {
        const [loading, setLoading] = useState<boolean>(false);
        const {message, setMessage, error, setError} = useMessage();
        const {pageNation, setPageNation, criteria, setCriteria} = usePageNation<ProductCriteriaType>();
        const {pageNation: substitutePageNation, setPageNation: setSubstitutePageNation} = usePageNation();
        const {pageNation: bomPageNation, setPageNation: setBomPageNation} = usePageNation();
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
            searchProductCriteria,
            setSearchProductCriteria,
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
            products: substitutes,
            setProducts: setSubstitutes,
            productService: substituteService
        } = useProduct();

        const fetchSubstitutes = useFetchSubstitutes(
            setLoading,
            setSubstitutes,
            setSubstitutePageNation,
            setError,
            showErrorMessage,
            substituteService
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
            (async () => {
                try {
                    await Promise.all([
                        fetchProducts.load(),
                        fetchSubstitutes.load(),
                        fetchBoms.load()
                    ]);
                } catch (error: any) {
                    showErrorMessage(`商品情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        const {
            Tab,
            TabList,
            TabPanel,
            Tabs,
        } = useTab();

        const modalView = () => {
            const editModal = () => {
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

                const editModalView = () => {
                    return (
                        <Modal
                            isOpen={modalIsOpen}
                            onRequestClose={handleCloseModal}
                            contentLabel="商品情報を入力"
                            className="modal"
                            overlayClassName="modal-overlay"
                            bodyOpenClassName="modal-open"
                        >
                            {singleView()}

                            {substituteModal().substituteEditModalView()}
                            {bomModal().bomEditModalView()}

                            {isEditing && (
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

            const substituteModal = () => {
                const handleCloseProductModal = () => {
                    setError("");
                    setProductModalIsOpen(false);
                    setProductEditId(null);
                }

                const substituteEditModalView = () => {
                    return (
                        <Modal
                            isOpen={productModalIsOpen}
                            onRequestClose={handleCloseProductModal}
                            contentLabel="代替商品情報を入力"
                            className="modal"
                            overlayClassName="modal-overlay"
                            bodyOpenClassName="modal-open"
                        >
                            {
                                <ProductCollectionSelectView
                                    products={substitutes}
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
                                    pageNation={substitutePageNation}
                                    fetchProducts={fetchSubstitutes.load}
                                />
                            }
                        </Modal>

                    )
                }

                return {
                    substituteEditModalView
                }
            }

            const bomModal = () => {
                const handleCloseBomModal = () => {
                    setError("");
                    setBomModalIsOpen(false);
                    setBomEditId(null);
                }

                const bomEditModalView = () => {
                    return (
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

                    )
                }

                return {
                    bomEditModalView
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
                                <ProductSearchSingleView
                                    criteria={searchProductCriteria}
                                    setCondition={setSearchProductCriteria}
                                    handleSelect={async () => {
                                        if (!searchProductCriteria) {
                                            return;
                                        }
                                        setLoading(true);
                                        try {
                                            const result = await productService.search(searchProductCriteria);
                                            setProducts(result ? result.list : []);
                                            if (result.list.length === 0) {
                                                showErrorMessage(`検索結果は0件です`, setError);
                                            } else {
                                                setCriteria(searchProductCriteria);
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

        // TODO:顧客マスタの作成後に実装
        const collectionView = () => {
            const {handleOpenModal} = modalView().editModal();
            const {handleOpenSearchModal} = modalView().searchModal();

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
                    <ProductCollectionView
                        error={error}
                        message={message}
                        searchItems={{searchProductCriteria, setSearchProductCriteria, handleOpenSearchModal}}
                        headerItems={{handleOpenModal, handleCheckToggleCollection: handleCheckAllProducts, handleDeleteCheckedCollection: handleDeleteCheckedProducts}}
                        contentItems={{products, handleDeleteProduct, handleCheckProduct}}
                        pageNationItems={{pageNation, fetchProducts: fetchProducts.load, criteria}}
                    />
            )
        };

        const singleView = () => {
            const {handleCloseModal} = modalView().editModal();

            const handleCreateOrUpdateProduct = async () => {
                const validateProduct = (): boolean => {
                    if (!newProduct.productCode.value.trim() || !newProduct.productName.productFormalName.trim()) {
                        setError("商品コード、商品名は必須項目です。");
                        return false;
                    }
                    //販売区分を選択してください
                    if (!newProduct.productType) {
                        setError("販売区分を選択してください。");
                        return false;
                    }
                    if (!newProduct.taxType) {
                        setError("税区分を選択してください。");
                        return false;
                    }
                    if (!newProduct.miscellaneousType) {
                        setError("雑区分を選択してください。");
                        return false;
                    }
                    if (!newProduct.stockManagementTargetType) {
                        setError("在庫管理対象区分を選択してください。");
                        return false;
                    }
                    if (!newProduct.stockAllocationType) {
                        setError("在庫引当区分を選択してください。");
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

            return (
                    <ProductSingleView
                        error={error}
                        message={message}
                        newProduct={newProduct}
                        setNewProduct={setNewProduct}
                        isEditing={isEditing}
                        handleCreateOrUpdateProduct={handleCreateOrUpdateProduct}
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
