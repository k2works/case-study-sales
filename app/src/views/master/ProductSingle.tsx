import {SingleViewHeaderActions, SingleViewHeaderItem} from "../Common.tsx";
import {
    MiscellaneousEnumType,
    ProductEnumType,
    ProductType,
    StockAllocationEnumType,
    StockManagementTargetEnumType,
    TaxEnumType
} from "../../models";
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
                value={newProduct.productCode.value}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productCode: { ...newProduct.productCode, value: e.target.value }
                })}
                disabled={isEditing}
            />
            <FormInput
                label="商品正式名"
                id="productFormalName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品名"
                value={newProduct.productName.productFormalName}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productName: { ...newProduct.productName, productFormalName: e.target.value }
                })}
            />
            <FormInput
                label="商品名略称"
                id="productAbbreviation"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品名略称"
                value={newProduct.productName.productAbbreviation}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productName: { ...newProduct.productName, productAbbreviation: e.target.value }
                })}
            />
            <FormInput
                label="商品名カナ"
                id="productNameKana"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品名カナ"
                value={newProduct.productName.productNameKana}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productName: { ...newProduct.productName, productNameKana: e.target.value }
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
                value={newProduct.sellingPrice.amount}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    sellingPrice: { ...newProduct.sellingPrice, amount: +e.target.value }
                })}
            />
            <FormInput
                label="原価"
                id="costOfSales"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newProduct.costOfSales.amount}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    costOfSales: { ...newProduct.costOfSales, amount: +e.target.value }
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
                value={newProduct.productCategoryCode.value}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    productCategoryCode: { ...newProduct.productCategoryCode, value: e.target.value }
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
                value={newProduct.supplierCode.value}
                onChange={(e) => setNewProduct({
                    ...newProduct,
                    supplierCode: { ...newProduct.supplierCode, value: e.target.value }
                })}
            />
        </div>
    )
};

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

interface FormSelectProps<T> {
    id: string;
    label: string;
    className?: string;
    value: T;
    options: { [key: string]: T };
    onChange: (value: T) => void;
}

const FormSelect = <T extends string>({id, label, className, value, options, onChange}: FormSelectProps<T>) => (
    <FormItem label={label}>
        <select
            id={id}
            className={className}
            value={value}
            onChange={(e) => onChange(e.target.value as T)}
        >
            <option value="" disabled selected hidden></option>
            {Object.entries(options).map(([key, val]) => (
                <option key={key} value={val}>{val}</option>
            ))}
        </select>
    </FormItem>
);

interface FormInputProps {
    label: string;
    id: string;
    type: string;
    className: string;
    placeholder?: string;
    value: string | number;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    disabled?: boolean;
}

const FormInput: React.FC<FormInputProps> = ({
                                                 label,
                                                 id,
                                                 type,
                                                 className,
                                                 placeholder,
                                                 value,
                                                 onChange,
                                                 disabled,
                                             }) => {
    return (
        <FormItem label={label}>
            <input
                id={id}
                type={type}
                className={className}
                placeholder={placeholder}
                value={value}
                onChange={onChange}
                disabled={disabled}
            />
        </FormItem>
    );
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
