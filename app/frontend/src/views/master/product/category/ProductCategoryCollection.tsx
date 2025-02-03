import React from "react";
import {ProductCategoryType} from "../../../../models/master/product";
import {Message} from "../../../../components/application/Message.tsx";
import {PageNation} from "../../../application/PageNation.tsx";

interface SearchBarProps {
    searchValue: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    onSearch: () => void;
}

const SearchBar: React.FC<SearchBarProps> = ({searchValue, onChange, onSearch}) => (
    <div className="search-container">
        <input id="search-input"
               type="text"
               placeholder="商品分類コードで検索"
               value={searchValue}
               onChange={onChange}/>
        <button className="action-button" id="search-all" onClick={onSearch}>検索</button>
    </div>
);

interface ProductCategoryItemProps {
    productCategory: ProductCategoryType;
    onEdit: (productCategory: ProductCategoryType) => void;
    onDelete: (productCategoryCode: string) => void;
    onCheck: (productCategory: ProductCategoryType) => void;
}

const ProductCategoryItem: React.FC<ProductCategoryItemProps> = ({productCategory, onEdit, onDelete, onCheck}) => (
    <li className="collection-object-item" key={productCategory.productCategoryCode.value}>
        <div className="collection-object-item-content" data-id={productCategory.productCategoryCode.value}>
            <input type="checkbox" className="collection-object-item-checkbox" checked={productCategory.checked}
                   onChange={() => onCheck(productCategory)}/>
        </div>
        <div className="collection-object-item-content" data-id={productCategory.productCategoryCode.value}>
            <div className="collection-object-item-content-details">商品分類コード</div>
            <div className="collection-object-item-content-name">{productCategory.productCategoryCode.value}</div>
        </div>
        <div className="collection-object-item-content" data-id={productCategory.productCategoryCode.value}>
            <div className="collection-object-item-content-details">商品分類名</div>
            <div className="collection-object-item-content-name">{productCategory.productCategoryName}</div>
        </div>
        <div className="collection-object-item-actions" data-id={productCategory.productCategoryCode.value}>
            <button className="action-button" onClick={() => onEdit(productCategory)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={productCategory.productCategoryCode.value}>
            <button className="action-button" onClick={() => onDelete(productCategory.productCategoryCode.value)}
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
                    key={productCategory.productCategoryCode.value}
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
        searchProductCategoryCode: string;
        setSearchProductCategoryCode: React.Dispatch<React.SetStateAction<string>>;
        handleSearchProductCategory: () => void;
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
        pageNation: any;
        fetchProductCategories: () => void;
    }
}

export const ProductCategoryCollectionView: React.FC<ProductCategoryCollectionViewProps> = ({
                                                                                                error,
                                                                                                message,
                                                                                                searchItems: {searchProductCategoryCode, setSearchProductCategoryCode, handleSearchProductCategory},
                                                                                                collectionItems: {productCategories, handleDeleteProductCategory, handleCheckProductCategory},
                                                                                                headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
                                                                                                pageNationItems: {pageNation, fetchProductCategories}
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
                <SearchBar
                    searchValue={searchProductCategoryCode}
                    onChange={(e) => setSearchProductCategoryCode(e.target.value)}
                    onSearch={handleSearchProductCategory}
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
                <PageNation pageNation={pageNation} callBack={fetchProductCategories}/>
            </div>
        </div>
    </div>
);
