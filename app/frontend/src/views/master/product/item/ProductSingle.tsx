import {FormInput, FormSelect, SingleViewHeaderActions, SingleViewHeaderItem} from "../../../Common.tsx";
import {
    MiscellaneousEnumType,
    ProductEnumType,
    ProductType,
    StockAllocationEnumType,
    StockManagementTargetEnumType,
    TaxEnumType
} from "../../../../models/master/product";
import React from "react";
import {Message} from "../../../../components/application/Message.tsx";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdateProduct: () => void;
    handleCloseModal: () => void;
}

const Header = ({
                    title,
                    subtitle,
                    isEditing,
                    handleCreateOrUpdateProduct,
                    handleCloseModal
                }: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdateProduct}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newProduct: ProductType;
    setNewProduct: React.Dispatch<React.SetStateAction<ProductType>>;
}

const Form = ({isEditing, newProduct, setNewProduct}: FormProps) => {
    const [productType, setProductType] = React.useState<ProductEnumType>(newProduct.productType as ProductEnumType);
    const [taxType, setTaxType] = React.useState<TaxEnumType>(newProduct.taxType as TaxEnumType);
    const [miscellaneousType, setMiscellaneousType] = React.useState<MiscellaneousEnumType>(newProduct.miscellaneousType as MiscellaneousEnumType);
    const [stockManagementTargetType, setStockManagementTargetType] = React.useState<StockManagementTargetEnumType>(newProduct.stockManagementTargetType as StockManagementTargetEnumType);
    const [stockAllocationType, setStockAllocationType] = React.useState<StockAllocationEnumType>(newProduct.stockAllocationType as StockAllocationEnumType);

    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="商品コード"
                id="productCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品コード"
                value={newProduct.productCode}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productCode: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="商品正式名"
                id="productFormalName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品名"
                value={newProduct.productFormalName}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productFormalName: e.target.value
                })}
            />
            <FormInput
                label="商品名略称"
                id="productAbbreviation"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品名略称"
                value={newProduct.productAbbreviation}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productAbbreviation: e.target.value
                })}
            />
            <FormInput
                label="商品名カナ"
                id="productNameKana"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品名カナ"
                value={newProduct.productNameKana}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productNameKana: e.target.value
                })}
            />
            <FormSelect
                id="productType"
                label="商品区分"
                value={productType}
                options={ProductEnumType}
                onChange={(e) => {
                    setProductType(e);
                    setNewProduct({
                        ...newProduct,
                        productType: e
                    });
                }}
            />
            <FormInput
                label="売価"
                id="sellingPrice"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newProduct.sellingPrice}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    sellingPrice: +e.target.value
                })}
            />
            <FormInput
                label="原価"
                id="costOfSales"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newProduct.costOfSales}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    costOfSales: +e.target.value
                })}
            />
            <FormSelect
                id="taxType"
                label="税区分"
                value={taxType}
                options={TaxEnumType}
                onChange={(e) => {
                    setTaxType(e);
                    setNewProduct({
                        ...newProduct,
                        taxType: e
                    });
                }}
            />
            <FormInput
                label="商品分類コード"
                id="productCategoryCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品分類コード"
                value={newProduct.productClassificationCode}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productClassificationCode: e.target.value
                })}
                disabled={isEditing}
            />
            <FormSelect
                id="miscellaneousType"
                label="雑区分"
                value={miscellaneousType}
                options={MiscellaneousEnumType}
                onChange={(e) => {
                    setMiscellaneousType(e);
                    setNewProduct({
                        ...newProduct,
                        miscellaneousType: e
                    });
                }}
            />
            <FormSelect
                id="stockManagementTargetType"
                label="在庫管理対象区分"
                value={stockManagementTargetType}
                options={StockManagementTargetEnumType}
                onChange={(e) => {
                    setStockManagementTargetType(e);
                    setNewProduct({
                        ...newProduct,
                        stockManagementTargetType: e
                    });
                }}
            />
            <FormSelect
                id="stockAllocationType"
                label="在庫引当区分"
                value={stockAllocationType}
                options={StockAllocationEnumType}
                onChange={(e) => {
                    setStockAllocationType(e);
                    setNewProduct({
                        ...newProduct,
                        stockAllocationType: e
                    });
                }}
            />
            <FormInput
                label="仕入先コード"
                id="supplierCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="仕入先コード"
                value={newProduct.vendorCode}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    vendorCode: e.target.value
                })}
            />
        </div>
    )
};

interface ProductSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    newProduct: ProductType;
    setNewProduct: React.Dispatch<React.SetStateAction<ProductType>>;
    handleCreateOrUpdateProduct: () => void;
    handleCloseModal: () => void;
}

export const ProductSingleView = ({
                                      error,
                                      message,
                                      isEditing,
                                      newProduct,
                                      setNewProduct,
                                      handleCreateOrUpdateProduct,
                                      handleCloseModal
                                  }: ProductSingleViewProps) => (
    <div className="single-view-object-container">
        <Message error={error} message={message}/>
        <div className="single-view-header">
            <Header
                title="商品"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCreateOrUpdateProduct={handleCreateOrUpdateProduct}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newProduct={newProduct}
                        setNewProduct={setNewProduct}
                    />
                </div>
            </div>
        </div>
    </div>
);
