import React from "react";
import {useProductItemContext} from "../../../../providers/master/product/ProductItem.tsx";
import {useProductSubstituteContext} from "../../../../providers/master/product/ProductSubstitute.tsx";
import {useProductBomContext} from "../../../../providers/master/product/ProductBom.tsx";
import {useCustomerContext} from "../../../../providers/master/partner/Customer.tsx";
import {useVendorContext} from "../../../../providers/master/partner/Vendor.tsx";
import {showErrorMessage} from "../../../application/utils.ts";
import {Tab, TabList, TabPanel, Tabs} from "react-tabs";
import {ProductSingleView} from "../../../../views/master/product/item/ProductSingle.tsx";
import {
    SubstituteProductCollectionAddListView
} from "../../../../views/master/product/item/SubstituteProductCollection.tsx";
import {BomCollectionAddListView} from "../../../../views/master/product/item/BomCollection.tsx";
import {
    CustomerSpecificSellingPriceCollectionAddListView
} from "../../../../views/master/product/item/CustomerSpecificSellingPriceCollection.tsx";
import {VendorSelectModal} from "./VendorSelectModal.tsx";
import {ProductItemSubstituteModal} from "./ProductItemSubstituteModal.tsx";
import {ProductItemBomModal} from "./ProductItemBomModal.tsx";
import {ProductCategorySelectModal} from "./ProductCategorySelectModal.tsx";
import {useProductCategoryContext} from "../../../../providers/master/product/ProductCategory.tsx";
import {CustomerSelectModal} from "./CustomerSelectModal.tsx";

export const ProductItemSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        editId,
        setEditId,
        initialProduct,
        newProduct,
        setNewProduct,
        fetchProducts,
        productService
    } = useProductItemContext();

    const {
        setProductModalIsOpen,
        setProductIsEditing,
    } = useProductSubstituteContext();

    const {
        setBomModalIsOpen,
        setBomIsEditing,
    } = useProductBomContext();

    const {
        setModalIsOpen: setCustomerModalIsOpen,
    } = useCustomerContext();

    const {
        setModalIsOpen: setVendorModalIsOpen,
    } = useVendorContext();

    const {
        setModalIsOpen: setProductCategoryModalIsOpen,
    } = useProductCategoryContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdateProduct = async () => {
        const validateProduct = (): boolean => {
            if (!newProduct.productCode.trim() || !newProduct.productFormalName.trim()) {
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
        <>
            <VendorSelectModal type={"edit"}/>
            <ProductCategorySelectModal  type={"edit"}/>
            <CustomerSelectModal type={"edit"}/>
            <ProductItemSubstituteModal/>
            <ProductItemBomModal/>
            {isEditing ? (
                <Tabs>
                    <TabList>
                        <Tab>商品アイテム</Tab>
                        <Tab>代替品</Tab>
                        <Tab>部品表</Tab>
                        <Tab>顧客別販売単価</Tab>
                    </TabList>
                    <TabPanel>
                        <ProductSingleView
                            error={error}
                            message={message}
                            newProduct={newProduct}
                            setNewProduct={setNewProduct}
                            isEditing={isEditing}
                            handleCreateOrUpdateProduct={handleCreateOrUpdateProduct}
                            handleCloseModal={handleCloseModal}
                            handleSelectVendor={() => setVendorModalIsOpen(true)}
                            handleSelectProductCategory={() => setProductCategoryModalIsOpen(true)}
                        />
                    </TabPanel>
                    <TabPanel>
                        <SubstituteProductCollectionAddListView
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
                                        (substituteProduct) =>
                                            substituteProduct.substituteProductCode !==
                                            product.substituteProductCode
                                    )
                                });
                            }}
                        />
                    </TabPanel>
                    <TabPanel>
                        <BomCollectionAddListView
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
                                        (bomProduct) =>
                                            bomProduct.componentCode !==
                                            product.componentCode
                                    )
                                });
                            }}
                        />
                    </TabPanel>
                    <TabPanel>
                        <CustomerSpecificSellingPriceCollectionAddListView
                            prices={newProduct.customerSpecificSellingPrices}
                            handleAdd={() => {
                                setMessage("");
                                setError("");
                                setCustomerModalIsOpen(true);
                            }}
                            handleDelete={(price) => {
                                setNewProduct({
                                    ...newProduct,
                                    customerSpecificSellingPrices: newProduct.customerSpecificSellingPrices.filter(
                                        (sellingPrice) =>
                                            sellingPrice.customerCode !== price.customerCode
                                    )
                                });
                            }}
                        />
                    </TabPanel>
                </Tabs>
            ) : (
                    <ProductSingleView
                        error={error}
                        message={message}
                        newProduct={newProduct}
                        setNewProduct={setNewProduct}
                        isEditing={isEditing}
                        handleCreateOrUpdateProduct={handleCreateOrUpdateProduct}
                        handleCloseModal={handleCloseModal}
                        handleSelectVendor={() => setVendorModalIsOpen(true)}
                        handleSelectProductCategory={() => setProductCategoryModalIsOpen(true)}
                    />
            )}
        </>
    );
}
