import React, { MouseEventHandler } from "react";
import { FormInput, SingleViewHeaderItem } from "../../Common.tsx";
import {ProductCategoryCriteriaType} from "../../../models";

interface FormProps {
    criteria: ProductCategoryCriteriaType,
    setCondition:(criteria: ProductCategoryCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void
}

const Form = ({criteria, setCondition, handleClick, handleClose}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="商品分類コード"
                id="productCategoryCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品分類コード"
                value={criteria.productCategoryCode}
                onChange={(e) => setCondition(
                    {...criteria, productCategoryCode: e.target.value}
                )}
            />
            <FormInput
                label="商品分類名"
                id="productCategoryName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品分類名"
                value={criteria.productCategoryName}
                onChange={(e) => setCondition(
                    {...criteria, productCategoryName: e.target.value}
                )}
            />
            <FormInput
                label="商品分類パス"
                id="productCategoryPath"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品分類パス"
                value={criteria.productCategoryPath}
                onChange={(e) => setCondition(
                    {...criteria, productCategoryPath: e.target.value}
                )}
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

interface ProductCategorySearchSingleViewProps {
    criteria: ProductCategoryCriteriaType,
    setCondition: (criteria: ProductCategoryCriteriaType) => void,
    handleSelect: (criteria: ProductCategoryCriteriaType) => Promise<void>,
    handleClose: () => void
}

export const ProductCategorySearchSingleView: React.FC<ProductCategorySearchSingleViewProps> = ({
                                                                                      criteria,
                                                                                      setCondition,
                                                                                      handleSelect,
                                                                                      handleClose,
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
                    <SingleViewHeaderItem title={"部門"} subtitle={"検索"}/>
                </div>
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <Form criteria={criteria} setCondition={setCondition} handleClick={handleClick} handleClose={handleCancel}/>
                    </div>
                </div>
            </div>
        </div>
    );
};