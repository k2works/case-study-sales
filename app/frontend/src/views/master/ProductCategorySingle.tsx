import React from 'react';
import {Message} from "../../components/application/Message.tsx";
import {ProductCategoryType} from "../../models";
import {FormInput, SingleViewHeaderActions, SingleViewHeaderItem} from "../Common.tsx";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdateProductCategory: () => void;
    handleCloseModal: () => void;
}

const Header = ({
                    title,
                    subtitle,
                    isEditing,
                    handleCreateOrUpdateProductCategory,
                    handleCloseModal
                }: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdateProductCategory}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newProductCategory: ProductCategoryType;
    setNewProductCategory: React.Dispatch<React.SetStateAction<ProductCategoryType>>;
}

const Form = ({isEditing, newProductCategory, setNewProductCategory}: FormProps) => (
    <div className="single-view-content-item-form">
        <FormInput
            label="商品分類コード"
            id="productCategoryCode"
            type="text"
            className="single-view-content-item-form-item-input"
            placeholder="商品分類コード"
            value={newProductCategory.productCategoryCode.value}
            onChange={(e) => setNewProductCategory({
                ...newProductCategory,
                productCategoryCode: { value: e.target.value }
            })}
            disabled={isEditing}
        />
        <FormInput
            label="商品分類名"
            id="productCategoryName"
            type="text"
            className="single-view-content-item-form-item-input"
            placeholder="商品分類名"
            value={newProductCategory.productCategoryName}
            onChange={(e) => setNewProductCategory({
                ...newProductCategory,
                productCategoryName: e.target.value
            })}
        />
        <FormInput
            label="カテゴリ階層"
            id="productCategoryHierarchy"
            type="number"
            className="single-view-content-item-form-item-input"
            value={newProductCategory.productCategoryHierarchy}
            onChange={(e) => setNewProductCategory({
                ...newProductCategory,
                productCategoryHierarchy: +e.target.value
            })}
        />
        <FormInput
            label="カテゴリパス"
            id="productCategoryPath"
            type="text"
            className="single-view-content-item-form-item-input"
            placeholder="カテゴリパス"
            value={newProductCategory.productCategoryPath}
            onChange={(e) => setNewProductCategory({
                ...newProductCategory,
                productCategoryPath: e.target.value
            })}
        />
        <FormInput
            label="最下層区分"
            id="lowestLevelDivision"
            type="number"
            className="single-view-content-item-form-item-input"
            value={newProductCategory.lowestLevelDivision}
            onChange={(e) => setNewProductCategory({
                ...newProductCategory,
                lowestLevelDivision: +e.target.value
            })}
        />
    </div>
);

interface ProductCategorySingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    newProductCategory: ProductCategoryType;
    setNewProductCategory: React.Dispatch<React.SetStateAction<ProductCategoryType>>;
    handleCreateOrUpdateProductCategory: () => void;
    handleCloseModal: () => void;
}

export const ProductCategorySingleView = ({
                                              error,
                                              message,
                                              isEditing,
                                              newProductCategory,
                                              setNewProductCategory,
                                              handleCreateOrUpdateProductCategory,
                                              handleCloseModal
                                          }: ProductCategorySingleViewProps) => (
    <div className="single-view-object-container">
        <Message error={error} message={message}/>
        <div className="single-view-header">
            <Header
                title="商品分類"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCreateOrUpdateProductCategory={handleCreateOrUpdateProductCategory}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newProductCategory={newProductCategory}
                        setNewProductCategory={setNewProductCategory}
                    />
                </div>
            </div>
        </div>
    </div>
);
