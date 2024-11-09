import {SingleViewHeaderActions, SingleViewHeaderItem} from "../Common.tsx";
import {ProductType} from "../../models";
import React from "react";
import {Message} from "../../components/application/Message.tsx";

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

const Form = ({isEditing, newProduct, setNewProduct}: FormProps) => (
    <div className="single-view-content-item-form">
        {/* 商品コードフィールド */}
        <FormItem label="商品コード">
            <input
                id="productCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品コード"
                value={newProduct.productCode.value}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productCode: {...newProduct.productCode, value: e.target.value}
                })}
                disabled={isEditing}
            />
        </FormItem>
        {/* 商品名フィールド */}
        <FormItem label="商品正式名">
            <input
                id="productFormalName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品名"
                value={newProduct.productName.productFormalName}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productName: {...newProduct.productName, productFormalName: e.target.value}
                })}
            />
        </FormItem>
        {/* 商品名フィールド */}
        <FormItem label="商品名略称">
            <input
                id="productAbbreviation"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品名略称"
                value={newProduct.productName.productAbbreviation}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productName: {...newProduct.productName, productAbbreviation: e.target.value}
                })}
            />
        </FormItem>
        {/* 商品名フィールド */}
        <FormItem label="商品名カナ">
            <input
                id="productNameKana"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品名略称"
                value={newProduct.productName.productNameKana}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productName: {...newProduct.productName, productNameKana: e.target.value}
                })}
            />
        </FormItem>
        {/* 商品区分フィールド */}
        <FormItem label="商品区分">
            <input
                id="productType"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品区分"
                value={newProduct.productType}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productType: e.target.value
                })}
            />
        </FormItem>
        {/* 売価フィールド */}
        <FormItem label="売価">
            <input
                id="sellingPrice"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newProduct.sellingPrice.amount}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    sellingPrice: {...newProduct.sellingPrice, amount: +e.target.value}
                })}
            />
        </FormItem>
        {/* 原価フィールド */}
        <FormItem label="原価">
            <input
                id="costOfSales"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newProduct.costOfSales.amount}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    costOfSales: {...newProduct.costOfSales, amount: +e.target.value}
                })}
            />
        </FormItem>
        {/* 税区分フィールド */}
        <FormItem label="税区分">
            <input
                id="taxType"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="税区分"
                value={newProduct.taxType}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    taxType: e.target.value
                })}
            />
        </FormItem>
        {/* 商品分類コードフィールド */}
        <FormItem label="商品分類コード">
            <input
                id="productCategoryCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品分類コード"
                value={newProduct.productCategoryCode.value}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productCategoryCode: {...newProduct.productCategoryCode, value: e.target.value}
                })}
                disabled={isEditing}
            />
        </FormItem>
        {/* 雑区分フィールド */}
        <FormItem label="雑区分">
            <input
                id="miscellaneousType"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="雑区分"
                value={newProduct.miscellaneousType}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    miscellaneousType: e.target.value
                })}
            />
        </FormItem>
        {/* 在庫管理対象区分分フィールド */}
        <FormItem label="在庫管理対象区分">
            <input
                id="miscellaneousType"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="在庫管理対象区分"
                value={newProduct.stockManagementTargetType}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    stockManagementTargetType: e.target.value
                })}
            />
        </FormItem>
        {/* 在庫引当区分分フィールド */}
        <FormItem label="在庫引当区分">
            <input
                id="stockAllocationType"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="在庫引当区分"
                value={newProduct.stockAllocationType}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    stockAllocationType: e.target.value
                })}
            />
        </FormItem>
        {/* 仕入先コードフィールド */}
        <FormItem label="仕入先コード">
            <input
                id="supplierCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="仕入先コード"
                value={newProduct.supplierCode.value}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    supplierCode: {...newProduct.supplierCode, value: e.target.value}
                })}
            />
        </FormItem>
    </div>
);

interface FormItemProps {
    label: string;
    children: React.ReactNode;
}

const FormItem = ({label, children}: FormItemProps) => (
    <div className="single-view-content-item-form-item">
        <label className="single-view-content-item-form-item-label">{label}</label>
        {children}
    </div>
);

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
