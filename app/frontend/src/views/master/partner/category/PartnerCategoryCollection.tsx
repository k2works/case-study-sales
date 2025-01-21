import React from "react";
import { Message } from "../../../../components/application/Message.tsx";
import { PageNation, PageNationType } from "../../../application/PageNation.tsx";
import { Search } from "../../../Common.tsx";
import {
    PartnerCategoryType,
    PartnerCategoryCriteriaType
} from "../../../../models";

interface PartnerCategoryItemProps {
    categoryItem: PartnerCategoryType;
    onEdit: (category: PartnerCategoryType) => void;
    onDelete: (categoryId: PartnerCategoryType['partnerCategoryTypeCode']) => void;
    onCheck: (category: PartnerCategoryType) => void;
}

const PartnerCategoryItem: React.FC<PartnerCategoryItemProps> = ({
                                                                     categoryItem,
                                                                     onEdit,
                                                                     onDelete,
                                                                     onCheck
                                                                 }) => (
    <li className="collection-object-item" key={categoryItem.partnerCategoryTypeCode}>
        <div className="collection-object-item-content" data-id={categoryItem.partnerCategoryTypeCode}>
            <input
                type="checkbox"
                className="collection-object-item-checkbox"
                checked={categoryItem.checked}
                onChange={() => onCheck(categoryItem)}
            />
        </div>
        <div className="collection-object-item-content" data-id={categoryItem.partnerCategoryTypeCode}>
            <div className="collection-object-item-content-details">取引先分類種別コード</div>
            <div className="collection-object-item-content-name">{categoryItem.partnerCategoryTypeCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={categoryItem.partnerCategoryTypeCode}>
            <div className="collection-object-item-content-details">取引先分類種別名</div>
            <div className="collection-object-item-content-name">{categoryItem.partnerCategoryTypeName}</div>
        </div>
        <div className="collection-object-item-actions" data-id={categoryItem.partnerCategoryTypeCode}>
            <button className="action-button" onClick={() => onEdit(categoryItem)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={categoryItem.partnerCategoryTypeCode}>
            <button className="action-button" onClick={() => onDelete(categoryItem.partnerCategoryTypeCode)} id="delete">削除</button>
        </div>
    </li>
);

interface PartnerCategoryListProps {
    categories: PartnerCategoryType[];
    onEdit: (category: PartnerCategoryType) => void;
    onDelete: (categoryId: PartnerCategoryType['partnerCategoryTypeCode']) => void;
    onCheck: (category: PartnerCategoryType) => void;
}

const PartnerCategoryList: React.FC<PartnerCategoryListProps> = ({
                                                                     categories,
                                                                     onEdit,
                                                                     onDelete,
                                                                     onCheck
                                                                 }) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {categories.map(category => (
                <PartnerCategoryItem
                    key={category.partnerCategoryTypeCode}
                    categoryItem={category}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface PartnerCategoryCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchPartnerCategoryCriteria: PartnerCategoryCriteriaType;
        setSearchPartnerCategoryCriteria: (value: PartnerCategoryCriteriaType) => void;
        handleOpenSearchModal: () => void;
    };
    headerItems: {
        handleOpenModal: (category?: PartnerCategoryType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    };
    collectionItems: {
        partnerCategories: PartnerCategoryType[];
        handleOpenModal: (category?: PartnerCategoryType) => void;
        handleDeletePartnerCategory: (categoryId: PartnerCategoryType['partnerCategoryTypeCode']) => void;
        handleCheckPartnerCategory: (category: PartnerCategoryType) => void;
    };
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: PartnerCategoryCriteriaType | null;
        fetchCategories: () => void;
    };
}

export const PartnerCategoryCollectionView: React.FC<PartnerCategoryCollectionViewProps> = ({
                                                                                                error,
                                                                                                message,
                                                                                                searchItems: {
                                                                                                    searchPartnerCategoryCriteria,
                                                                                                    setSearchPartnerCategoryCriteria,
                                                                                                    handleOpenSearchModal
                                                                                                },
                                                                                                headerItems: {
                                                                                                    handleOpenModal,
                                                                                                    handleCheckToggleCollection,
                                                                                                    handleDeleteCheckedCollection
                                                                                                },
                                                                                                collectionItems: {
                                                                                                    partnerCategories,
                                                                                                    handleOpenModal: handleEditCategory,
                                                                                                    handleDeletePartnerCategory,
                                                                                                    handleCheckPartnerCategory
                                                                                                },
                                                                                                pageNationItems: {
                                                                                                    pageNation,
                                                                                                    criteria,
                                                                                                    fetchCategories
                                                                                                }
                                                                                            }) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message} />
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">取引先分類</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchPartnerCategoryCriteria}
                    setSearchCriteria={setSearchPartnerCategoryCriteria}
                    handleSearchAudit={handleOpenSearchModal}
                />
                <div className="button-container">
                    <button
                        className="action-button"
                        onClick={() => handleOpenModal()}
                        id="new"
                    >
                        新規
                    </button>
                    <button
                        className="action-button"
                        onClick={() => handleCheckToggleCollection()}
                        id="checkAll"
                    >
                        一括選択
                    </button>
                    <button
                        className="action-button"
                        onClick={() => handleDeleteCheckedCollection()}
                        id="deleteAll"
                    >
                        一括削除
                    </button>
                </div>
                <PartnerCategoryList
                    categories={partnerCategories}
                    onEdit={handleEditCategory}
                    onDelete={handleDeletePartnerCategory}
                    onCheck={handleCheckPartnerCategory}
                />
                <PageNation
                    pageNation={pageNation}
                    callBack={fetchCategories}
                    criteria={criteria}
                />
            </div>
        </div>
    </div>
);