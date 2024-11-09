import React from 'react';
import {Message} from "../../components/application/Message.tsx";
import {ProductCategoryType} from "../../models";
import {SingleViewHeaderActions, SingleViewHeaderItem} from "../Common.tsx";

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
        {/* 商品分類コードフィールド */}
        <FormItem label="商品分類コード">
            <input
                id="productCategoryCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品分類コード"
                value={newProductCategory.productCategoryCode.value}
                onChange={(e) => setNewProductCategory({
                    ...newProductCategory,
                    productCategoryCode: {value: e.target.value}
                })}
                disabled={isEditing}
            />
        </FormItem>
        {/* 商品分類名フィールド */}
        <FormItem label="商品分類名">
            <input
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
        </FormItem>
        {/* カテゴリ階層フィールド */}
        <FormItem label="カテゴリ階層">
            <input
                id="productCategoryHierarchy"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newProductCategory.productCategoryHierarchy}
                onChange={(e) => setNewProductCategory({
                    ...newProductCategory,
                    productCategoryHierarchy: +e.target.value
                })}
            />
        </FormItem>
        {/* カテゴリパスフィールド */}
        <FormItem label="カテゴリパス">
            <input
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
        </FormItem>
        {/* 最下層区分フィールド */}
        <FormItem label="最下層区分">
            <input
                id="lowestLevelDivision"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newProductCategory.lowestLevelDivision}
                onChange={(e) => setNewProductCategory({
                    ...newProductCategory,
                    lowestLevelDivision: +e.target.value
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
