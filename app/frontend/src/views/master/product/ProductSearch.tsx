import React, { MouseEventHandler } from "react";
import {FormInput, FormSelect, SingleViewHeaderItem} from "../../Common.tsx";
import {
    MiscellaneousEnumType,
    ProductCriteriaType,
    ProductEnumType, StockAllocationEnumType, StockManagementTargetEnumType, TaxEnumType
} from "../../../models/master/product";

interface FormProps {
    criteria: ProductCriteriaType,
    setCondition:(criteria: ProductCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void
    handleSelectVendor: () => void,
    handleSelectProductCategory: () => void
}

const Form = ({criteria, setCondition, handleClick, handleClose, handleSelectVendor, handleSelectProductCategory}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="商品コード"
                id="productCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品コード"
                value={criteria.productCode}
                onChange={(e) => setCondition(
                    {...criteria, productCode: e.target.value}
                )}
            /> 
            <FormInput
                label="商品正式名"
                id="productFormalName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品名"
                value={criteria.productFormalName}
                onChange={(e) => setCondition(
                    {...criteria, productFormalName: e.target.value}
                )}
            />
            <FormInput
                label="商品名略称"
                id="productAbbreviation"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品名略称"
                value={criteria.productAbbreviation}
                onChange={(e) => setCondition(
                    {...criteria, productAbbreviation: e.target.value}
                )}
            />
            <FormInput
                label="商品名カナ"
                id="productNameKana"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品名カナ"
                value={criteria.productNameKana}
                onChange={(e) => setCondition(
                    {...criteria, productNameKana: e.target.value}
                )}
            />
            <FormInput
                label="商品分類コード"
                id="ProductCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品分類コード"
                value={criteria.productCategoryCode}
                onChange={(e) => setCondition(
                    {...criteria, productCategoryCode: e.target.value}
                )}
                onClick={handleSelectProductCategory}
            />
            <FormInput
                label="仕入先コード"
                id="supplierCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="仕入先コード"
                value={criteria.vendorCode}
                onChange={(e) => setCondition(
                    {...criteria, vendorCode: e.target.value}
                )}
                onClick={handleSelectVendor}
            />
            <FormSelect
                id="productType"
                label="商品区分"
                value={criteria.productType}
                options={ProductEnumType}
                onChange={(e) => {
                    setCondition({...criteria, productType: e});
                }}
            />
            <FormSelect
                id="taxType"
                label="税区分"
                value={criteria.taxType}
                options={TaxEnumType}
                onChange={(e) => {
                    setCondition({...criteria, taxType: e});
                }}
            />
            <FormSelect
                id="miscellaneousType"
                label="雑区分"
                value={criteria.miscellaneousType}
                options={MiscellaneousEnumType}
                onChange={(e) => {
                    setCondition({...criteria, miscellaneousType: e});
                }}
            />
            <FormSelect
                id="stockManagementTargetType"
                label="在庫管理対象区分"
                value={criteria.stockManagementTargetType}
                options={StockManagementTargetEnumType}
                onChange={(e) => {
                    setCondition({...criteria, stockManagementTargetType: e});
                }}
            />
            <FormSelect
                id="stockAllocationType"
                label="在庫引当区分"
                value={criteria.stockAllocationType}
                options={StockAllocationEnumType}
                onChange={(e) => {
                    setCondition({...criteria, stockAllocationType: e});
                }}
            />
            <div className="button-container">
                <button className="action-button" id="search-all" onClick={handleClick}>
                    検索
                </button>
                <button className="action-button" onClick={handleClose} id="cancel">
                    キャンセル
                </button>
            </div>
        </div>
    );
};

interface ProductSearchSingleViewProps {
    criteria: ProductCriteriaType,
    setCondition: (criteria: ProductCriteriaType) => void,
    handleSelect: (criteria: ProductCriteriaType) => Promise<void>,
    handleClose: () => void
    handleSelectVendor: () => void,
    handleSelectProductCategory: () => void
}

export const ProductSearchSingleView: React.FC<ProductSearchSingleViewProps> = ({
                                                                                      criteria,
                                                                                      setCondition,
                                                                                      handleSelect,
                                                                                      handleClose,
                                                                                      handleSelectVendor,
                                                                                      handleSelectProductCategory
                                                                                  }) => {
    const handleClick: MouseEventHandler<HTMLButtonElement> = async(e) => {
        e.preventDefault();
        await handleSelect(criteria);
        handleClose();
    }

    const handleCancel: MouseEventHandler<HTMLButtonElement> = (e) => {
        e.preventDefault();
        handleClose();
    }

    return (
        <div className="single-view-object-container">
            <div className="single-view-header">
                <div>
                    <SingleViewHeaderItem title={"商品アイテム"} subtitle={"検索"}/>
                </div>
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <Form criteria={criteria}
                              setCondition={setCondition}
                              handleClick={handleClick}
                              handleClose={handleCancel}
                              handleSelectProductCategory={handleSelectProductCategory}
                              handleSelectVendor={handleSelectVendor}/>
                    </div>
                </div>
            </div>
        </div>
    );
};