import React from "react";
import {ProductCategoryCriteriaType, ProductCategoryType} from "../../../../models/master/product";
import {PageNation, PageNationType} from "../../../application/PageNation.tsx";
import {Message} from "../../../../components/application/Message.tsx";
import {Search} from "../../../Common.tsx";

interface ProductCategoryItemProps {
    productCategory: ProductCategoryType;
    onEdit: (productCategory: ProductCategoryType) => void;
    onDelete: (productCategoryCode: string) => void;
    onCheck: (productCategory: ProductCategoryType) => void;
}

const ProductCategoryItem: React.FC<ProductCategoryItemProps> = ({productCategory, onEdit, onDelete, onCheck}) => (
    <li className="collection-object-item" key={productCategory.productCategoryCode}>
        <div className="collection-object-item-content" data-id={productCategory.productCategoryCode}>
            <input type="checkbox" className="collection-object-item-checkbox" checked={productCategory.checked}
                   onChange={() => onCheck(productCategory)}/>
        </div>
        <div className="collection-object-item-content" data-id={productCategory.productCategoryCode}>
            <div className="collection-object-item-content-details">商品分類コード</div>
            <div className="collection-object-item-content-name">{productCategory.productCategoryCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={productCategory.productCategoryCode}>
            <div className="collection-object-item-content-details">商品分類名</div>
            <div className="collection-object-item-content-name">{productCategory.productCategoryName}</div>
        </div>
        <div className="collection-object-item-actions" data-id={productCategory.productCategoryCode}>
            <button className="action-button" onClick={() => onEdit(productCategory)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={productCategory.productCategoryCode}>
            <button className="action-button" onClick={() => onDelete(productCategory.productCategoryCode)}
                    id="delete">削除
            </button>
        </div>
    </li>
);

interface ProductCategoryListProps {
    productCategories: ProductCategoryType[];
    onEdit: (productCategory: ProductCategoryType) => void;
    onDelete: (productCategoryCode: string) => void;
    onCheck: (productCategory: ProductCategoryType) => void;
}

const ProductCategoryList: React.FC<ProductCategoryListProps> = ({productCategories, onEdit, onDelete, onCheck}) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {productCategories.map(productCategory => (
                <ProductCategoryItem
                    key={productCategory.productCategoryCode}
                    productCategory={productCategory}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface ProductCategoryCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchProductCategoryCriteria: ProductCategoryCriteriaType;
        setSearchProductCategoryCriteria: (value: ProductCategoryCriteriaType) => void;
        handleOpenSearchModal: () => void;
    }
    headerItems: {
        handleOpenModal: (productCategory?: ProductCategoryType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    }
    collectionItems: {
        productCategories: ProductCategoryType[];
        handleDeleteProductCategory: (productCategoryCode: string) => void;
        handleCheckProductCategory: (productCategory: ProductCategoryType) => void;
    }
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: ProductCategoryCriteriaType | null;
        fetchProductCategories: () => void;
    }
}

export const ProductCategoryCollectionView: React.FC<ProductCategoryCollectionViewProps> = ({
                                                                                                error,
                                                                                                message,
                                                                                                searchItems: {searchProductCategoryCriteria, setSearchProductCategoryCriteria, handleOpenSearchModal},
                                                                                                collectionItems: {productCategories, handleDeleteProductCategory, handleCheckProductCategory},
                                                                                                headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
                                                                                                pageNationItems: {pageNation, fetchProductCategories, criteria}
                                                                                            }) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">商品分類</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchProductCategoryCriteria}
                    setSearchCriteria={setSearchProductCategoryCriteria}
                    handleSearchAudit={handleOpenSearchModal}
                />
                <div className="button-container">
                    <button className="action-button" onClick={() => handleOpenModal()} id="new">
                        新規
                    </button>
                    <button className="action-button" onClick={() => handleCheckToggleCollection()} id="checkAll">
                        一括選択
                    </button>
                    <button className="action-button" onClick={() => handleDeleteCheckedCollection()} id="deleteAll">
                        一括削除
                    </button>
                </div>
                <ProductCategoryList
                    productCategories={productCategories}
                    onEdit={handleOpenModal}
                    onDelete={handleDeleteProductCategory}
                    onCheck={handleCheckProductCategory}
                />
                <PageNation pageNation={pageNation} callBack={fetchProductCategories} criteria={criteria}/>
            </div>
        </div>
    </div>
);
