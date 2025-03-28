import React from 'react';
import {ProductCriteriaType, ProductType} from "../../../../models/master/product";
import {PageNation, PageNationType} from "../../../application/PageNation.tsx";
import {Search} from "../../../Common.tsx";
import {Message} from "../../../../components/application/Message.tsx";

interface ProductItemProps {
    product: ProductType;
    onEdit: (product: ProductType) => void;
    onDelete: (productCode: string) => void;
    onCheck: (product: ProductType) => void;
}

const ProductItem: React.FC<ProductItemProps> = ({product, onEdit, onDelete, onCheck}) => (
    <li className="collection-object-item" key={product.productCode}>
        <div className="collection-object-item-content" data-id={product.productCode}>
            <input
                type="checkbox"
                className="collection-object-item-checkbox"
                checked={product.checked}
                onChange={() => onCheck(product)}
            />
        </div>
        <div className="collection-object-item-content" data-id={product.productCode}>
            <div className="collection-object-item-content-details">商品コード</div>
            <div className="collection-object-item-content-name">{product.productCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={product.productCode}>
            <div className="collection-object-item-content-details">商品名</div>
            <div className="collection-object-item-content-name">{product.productFormalName}</div>
        </div>
        <div className="collection-object-item-content" data-id={product.productCode}>
            <div className="collection-object-item-content-details">売価</div>
            <div className="collection-object-item-content-name">{product.sellingPrice}</div>
        </div>
        <div className="collection-object-item-content" data-id={product.productCode}>
            <div className="collection-object-item-content-details">原価</div>
            <div className="collection-object-item-content-name">{product.costOfSales}</div>
        </div>
        <div className="collection-object-item-content" data-id={product.productCode}>
            <div className="collection-object-item-content-details">税区分</div>
            <div className="collection-object-item-content-name">{product.taxType}</div>
        </div>
        <div className="collection-object-item-content" data-id={product.productCode}>
            <div className="collection-object-item-content-details">商品区分</div>
            <div className="collection-object-item-content-name">{product.productType}</div>
        </div>
        <div className="collection-object-item-actions" data-id={product.productCode}>
            <button className="action-button" onClick={() => onEdit(product)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={product.productCode}>
            <button className="action-button" onClick={() => onDelete(product.productCode)} id="delete">削除
            </button>
        </div>
    </li>
);

interface ProductListProps {
    products: ProductType[];
    onEdit: (product: ProductType) => void;
    onDelete: (productCode: string) => void;
    onCheck: (product: ProductType) => void;
}

const ProductList: React.FC<ProductListProps> = ({products, onEdit, onDelete, onCheck}) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {products.map(product => (
                <ProductItem
                    key={product.productCode}
                    product={product}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface ProductCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchProductCriteria: ProductCriteriaType;
        setSearchProductCriteria: (value: ProductCriteriaType) => void;
        handleOpenSearchModal: () => void;
    }
    headerItems: {
        handleOpenModal: (product?: ProductType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    }
    contentItems: {
        products: ProductType[];
        handleDeleteProduct: (productCode: string) => void;
        handleCheckProduct: (product: ProductType) => void;
    }
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: ProductCriteriaType | null;
        fetchProducts: () => void;
    }
}

export const ProductCollectionView: React.FC<ProductCollectionViewProps> = ({
                                                                                error,
                                                                                message,
                                                                                searchItems: {searchProductCriteria, setSearchProductCriteria, handleOpenSearchModal},
                                                                                headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
                                                                                contentItems: {products, handleDeleteProduct, handleCheckProduct},
                                                                                pageNationItems: {pageNation, fetchProducts, criteria}
                                                                            }) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">商品アイテム</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchProductCriteria}
                    setSearchCriteria={setSearchProductCriteria}
                    handleSearchAudit={handleOpenSearchModal}
                />
                <div className="button-container">
                    <button className="action-button" onClick={() => handleOpenModal()} id="new">新規</button>
                    <button className="action-button" onClick={() => handleCheckToggleCollection()}
                            id="checkAll">一括選択
                    </button>
                    <button className="action-button" onClick={() => handleDeleteCheckedCollection()}
                            id="deleteAll">一括削除
                    </button>
                </div>
                <ProductList
                    products={products}
                    onEdit={handleOpenModal}
                    onDelete={handleDeleteProduct}
                    onCheck={handleCheckProduct}
                />
                <PageNation pageNation={pageNation} callBack={fetchProducts} criteria={criteria}/>
            </div>
        </div>
    </div>
);

interface ProductCollectionAddListProps {
    products: ProductType[];
    handleAdd: () => void;
    handleDelete: (product: ProductType) => void;
}

export const ProductCollectionAddListView: React.FC<ProductCollectionAddListProps> = ({products, handleAdd, handleDelete}) => {
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
                                <li className="collection-object-item" key={product.productCode}>
                                    <div className="collection-object-item-content" data-id={product.productCode}>
                                        <div className="collection-object-item-content-details">商品コード</div>
                                        <div
                                            className="collection-object-item-content-name">{product.productCode}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={product.productCode}>
                                        <div className="collection-object-item-content-details">商品名</div>
                                        <div
                                            className="collection-object-item-content-name">{product.productFormalName}</div>
                                    </div>
                                    <div className="collection-object-item-actions" data-id={product.productCode}>
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
