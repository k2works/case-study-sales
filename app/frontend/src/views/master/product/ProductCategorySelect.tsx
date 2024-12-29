import React from "react";
import {ProductCategoryType} from "../../../models";
import {FaTimes} from "react-icons/fa";
import {PageNation} from "../../application/PageNation.tsx";

interface ProductCategorySelectProps {
    handleSelect: () => void;
}

export const ProductCategorySelectView: React.FC<ProductCategorySelectProps> = ({handleSelect}) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">商品分類</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={handleSelect} id="select-product-category">選択
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}

interface ProductCategoryCollectionSelectProps {
    productCategories: ProductCategoryType[];
    handleSelect: (product: ProductCategoryType) => void;
    handleClose: () => void;
    pageNation: any; // 適切な型を使用してください
    fetchProductCategories: () => void;
}

export const ProductCategoryCollectionSelectView: React.FC<ProductCategoryCollectionSelectProps> = ({
                                                                                        productCategories,
                                                                                        handleSelect,
                                                                                        handleClose,
                                                                                        pageNation,
                                                                                        fetchProductCategories
                                                                                    }) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <button className="close-modal-button" onClick={handleClose}>
                    <FaTimes aria-hidden="true"/>
                </button>
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">商分類品</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {productCategories.map(productCategory => (
                                <li className="collection-object-item" key={productCategory.productCategoryCode.value}>
                                    <div className="collection-object-item-content" data-id={productCategory.productCategoryCode.value}>
                                        <div className="collection-object-item-content-details">商品分類コード</div>
                                        <div
                                            className="collection-object-item-content-name">{productCategory.productCategoryCode.value}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={productCategory.productCategoryCode.value}>
                                        <div className="collection-object-item-content-details">商品分類名</div>
                                        <div
                                            className="collection-object-item-content-name">{productCategory.productCategoryName}</div>
                                    </div>
                                    <div className="collection-object-item-actions" data-id={productCategory.productCategoryCode.value}>
                                        <button className="action-button" onClick={() => handleSelect(productCategory)} id="select-product">選択</button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
                <PageNation pageNation={pageNation} callBack={fetchProductCategories}/>
            </div>
        </div>
    );
};
