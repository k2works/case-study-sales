import React from 'react';
import {Message} from "../../../components/application/Message.tsx";
import {PageNation} from "../../application/PageNation.tsx";
import {ProductType} from "../../../models";

interface SearchBarProps {
    searchValue: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    onSearch: () => void;
}

const SearchBar: React.FC<SearchBarProps> = ({searchValue, onChange, onSearch}) => (
    <div className="search-container">
        <input
            id="search-input"
            type="text"
            placeholder="商品コードで検索"
            value={searchValue}
            onChange={onChange}
        />
        <button className="action-button" id="search-all" onClick={onSearch}>検索</button>
    </div>
);

interface ProductItemProps {
    product: ProductType;
    onEdit: (product: ProductType) => void;
    onDelete: (productCode: string) => void;
    onCheck: (product: ProductType) => void;
}

const ProductItem: React.FC<ProductItemProps> = ({product, onEdit, onDelete, onCheck}) => (
    <li className="collection-object-item" key={product.productCode.value}>
        <div className="collection-object-item-content" data-id={product.productCode.value}>
            <input
                type="checkbox"
                className="collection-object-item-checkbox"
                checked={product.checked}
                onChange={() => onCheck(product)}
            />
        </div>
        <div className="collection-object-item-content" data-id={product.productCode.value}>
            <div className="collection-object-item-content-details">商品コード</div>
            <div className="collection-object-item-content-name">{product.productCode.value}</div>
        </div>
        <div className="collection-object-item-content" data-id={product.productCode.value}>
            <div className="collection-object-item-content-details">商品名</div>
            <div className="collection-object-item-content-name">{product.productName.productFormalName}</div>
        </div>
        <div className="collection-object-item-content" data-id={product.productCode.value}>
            <div className="collection-object-item-content-details">売価</div>
            <div className="collection-object-item-content-name">{product.sellingPrice.amount}</div>
        </div>
        <div className="collection-object-item-content" data-id={product.productCode.value}>
            <div className="collection-object-item-content-details">原価</div>
            <div className="collection-object-item-content-name">{product.costOfSales.amount}</div>
        </div>
        <div className="collection-object-item-content" data-id={product.productCode.value}>
            <div className="collection-object-item-content-details">税区分</div>
            <div className="collection-object-item-content-name">{product.taxType}</div>
        </div>
        <div className="collection-object-item-content" data-id={product.productCode.value}>
            <div className="collection-object-item-content-details">商品区分</div>
            <div className="collection-object-item-content-name">{product.productType}</div>
        </div>
        <div className="collection-object-item-actions" data-id={product.productCode.value}>
            <button className="action-button" onClick={() => onEdit(product)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={product.productCode.value}>
            <button className="action-button" onClick={() => onDelete(product.productCode.value)} id="delete">削除
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
                    key={product.productCode.value}
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
        searchProductCode: string;
        setSearchProductCode: React.Dispatch<React.SetStateAction<string>>;
        handleSearchProduct: () => void;
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
        pageNation: any; // 適切な型を使用してください
        fetchProducts: () => void;
    }
}

export const ProductCollectionView: React.FC<ProductCollectionViewProps> = ({
                                                                                error,
                                                                                message,
                                                                                searchItems: {searchProductCode, setSearchProductCode, handleSearchProduct,},
                                                                                headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
                                                                                contentItems: {products, handleDeleteProduct, handleCheckProduct},
                                                                                pageNationItems: {pageNation, fetchProducts}
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
                <SearchBar
                    searchValue={searchProductCode}
                    onChange={(e) => setSearchProductCode(e.target.value)}
                    onSearch={handleSearchProduct}
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
                <PageNation pageNation={pageNation} callBack={fetchProducts}/>
            </div>
        </div>
    </div>
);
