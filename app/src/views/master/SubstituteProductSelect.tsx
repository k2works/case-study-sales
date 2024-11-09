import React from "react";
import {SubstituteProductType} from "../../models";
import {FaTimes} from "react-icons/fa";
import {PageNation} from "../application/PageNation.tsx";

interface SubstituteProductCollectionProps {
    substituteProducts: SubstituteProductType[];
    handleAdd: () => void;
    handleDelete: (product: SubstituteProductType) => void;
}

export const SubstituteProductCollectionView: React.FC<SubstituteProductCollectionProps> = ({
                                                                                                substituteProducts,
                                                                                                handleAdd,
                                                                                                handleDelete
                                                                                            }) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">代替商品</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={handleAdd} id="add">追加</button>
                    </div>
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {substituteProducts.map(product => (
                                <li className="collection-object-item" key={product.substituteProductCode}>
                                    <div className="collection-object-item-content"
                                         data-id={product.substituteProductCode}>
                                        <div className="collection-object-item-content-details">代替商品コード</div>
                                        <div
                                            className="collection-object-item-content-name">{product.substituteProductCode}</div>
                                    </div>
                                    <div className="collection-object-item-content"
                                         data-id={product.substituteProductCode}>
                                        <div className="collection-object-item-content-details">優先度</div>
                                        <div className="collection-object-item-content-name">{product.priority}</div>
                                    </div>
                                    <div className="collection-object-item-actions"
                                         data-id={product.substituteProductCode}>
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

interface SubstituteProductCollectionSelectProps {
    substituteProducts: SubstituteProductType[];
    handleSelect: (product: SubstituteProductType) => void;
    handleClose: () => void;
    pageNation: any; // 適切な型を使用してください
    fetchProducts: () => void;
}

export const SubstituteProductCollectionSelectView: React.FC<SubstituteProductCollectionSelectProps> = ({
                                                                                                            substituteProducts,
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
                        <h2 className="single-view-title">代替商品</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {substituteProducts.map(product => (
                                <li className="collection-object-item" key={product.substituteProductCode}>
                                    <div className="collection-object-item-content"
                                         data-id={product.substituteProductCode}>
                                        <div className="collection-object-item-content-details">代替商品コード</div>
                                        <div
                                            className="collection-object-item-content-name">{product.substituteProductCode}</div>
                                    </div>
                                    <div className="collection-object-item-content"
                                         data-id={product.substituteProductCode}>
                                        <div className="collection-object-item-content-details">優先度</div>
                                        <div className="collection-object-item-content-name">{product.priority}</div>
                                    </div>
                                    <div className="collection-object-item-actions"
                                         data-id={product.substituteProductCode}>
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
