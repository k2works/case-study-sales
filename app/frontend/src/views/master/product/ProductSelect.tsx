import React from "react";
import {ProductType} from "../../../models"; // 正しいパスに修正してください
import {FaTimes} from "react-icons/fa";
import {PageNation} from "../../application/PageNation.tsx";

interface ProductCollectionProps {
    products: ProductType[];
    handleAdd: () => void;
    handleDelete: (product: ProductType) => void;
}

export const ProductCollectionAddListView: React.FC<ProductCollectionProps> = ({products, handleAdd, handleDelete}) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">商品</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={handleAdd} id="add">追加</button>
                    </div>
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {products.map(product => (
                                <li className="collection-object-item" key={product.productCode.value}>
                                    <div className="collection-object-item-content" data-id={product.productCode.value}>
                                        <div className="collection-object-item-content-details">商品コード</div>
                                        <div
                                            className="collection-object-item-content-name">{product.productCode.value}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={product.productCode.value}>
                                        <div className="collection-object-item-content-details">商品名</div>
                                        <div
                                            className="collection-object-item-content-name">{product.productName.productFormalName}</div>
                                    </div>
                                    <div className="collection-object-item-actions" data-id={product.productCode.value}>
                                        <button className="action-button" onClick={() => handleDelete(product)}>削除
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    );
};

interface ProductCollectionSelectProps {
    products: ProductType[];
    handleSelect: (product: ProductType) => void;
    handleClose: () => void;
    pageNation: any; // 適切な型を使用してください
    fetchProducts: () => void;
}

export const ProductCollectionSelectView: React.FC<ProductCollectionSelectProps> = ({
                                                                                        products,
                                                                                        handleSelect,
                                                                                        handleClose,
                                                                                        pageNation,
                                                                                        fetchProducts
                                                                                    }) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <button className="close-modal-button" onClick={handleClose}>
                    <FaTimes aria-hidden="true"/>
                </button>
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">商品</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {products.map(product => (
                                <li className="collection-object-item" key={product.productCode.value}>
                                    <div className="collection-object-item-content" data-id={product.productCode.value}>
                                        <div className="collection-object-item-content-details">商品コード</div>
                                        <div
                                            className="collection-object-item-content-name">{product.productCode.value}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={product.productCode.value}>
                                        <div className="collection-object-item-content-details">商品名</div>
                                        <div
                                            className="collection-object-item-content-name">{product.productName.productFormalName}</div>
                                    </div>
                                    <div className="collection-object-item-actions" data-id={product.productCode.value}>
                                        <button className="action-button" onClick={() => handleSelect(product)}
                                                id="select-product">選択
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
                <PageNation pageNation={pageNation} callBack={fetchProducts}/>
            </div>
        </div>
    );
};
