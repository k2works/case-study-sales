import React from "react";
import {PartnerCriteriaType, PartnerType} from "../../../models/master/partner";
import {PageNation, PageNationType} from "../../application/PageNation.tsx";
import {Message} from "../../../components/application/Message.tsx";
import {Search} from "../../Common.tsx";

interface PartnerItemProps {
    partner: PartnerType;
    onEdit: (partner: PartnerType) => void;
    onDelete: (partnerCode: string) => void;
    onCheck: (partner: PartnerType) => void;
}

const PartnerItem: React.FC<PartnerItemProps> = ({
                                                     partner,
                                                     onEdit,
                                                     onDelete,
                                                     onCheck
                                                 }) => (
    <li className="collection-object-item" key={partner.partnerCode.value}>
        <div className="collection-object-item-content" data-id={partner.partnerCode.value}>
            <input
                type="checkbox"
                className="collection-object-item-checkbox"
                checked={(partner as any).checked || false}
                onChange={() => onCheck(partner)}
            />
        </div>
        <div className="collection-object-item-content" data-id={partner.partnerCode.value}>
            <div className="collection-object-item-content-details">取引先コード</div>
            <div className="collection-object-item-content-name">{partner.partnerCode.value}</div>
        </div>
        <div className="collection-object-item-content" data-id={partner.partnerCode.value}>
            <div className="collection-object-item-content-details">取引先名</div>
            <div className="collection-object-item-content-name">{partner.partnerName.name}</div>
        </div>
        <div className="collection-object-item-actions" data-id={partner.partnerCode.value}>
            <button className="action-button" onClick={() => onEdit(partner)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={partner.partnerCode.value}>
            <button className="action-button" onClick={() => onDelete(partner.partnerCode.value)} id="delete">削除</button>
        </div>
    </li>
);

interface PartnerListProps {
    partners: PartnerType[];
    onEdit: (partner: PartnerType) => void;
    onDelete: (partnerCode: string) => void;
    onCheck: (partner: PartnerType) => void;
}

const PartnerList: React.FC<PartnerListProps> = ({
                                                     partners,
                                                     onEdit,
                                                     onDelete,
                                                     onCheck
                                                 }) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {partners.map((partner) => (
                <PartnerItem
                    key={partner.partnerCode.value}
                    partner={partner}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface PartnerCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchPartnerCriteria: PartnerCriteriaType;
        setSearchPartnerCriteria: (value: PartnerCriteriaType) => void;
        handleOpenSearchModal: () => void;
    };
    headerItems: {
        handleOpenModal: (partner?: PartnerType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    };
    collectionItems: {
        partners: PartnerType[];
        handleOpenModal: (partner?: PartnerType) => void;
        handleDeletePartner: (partnerCode: string) => void;
        handleCheckPartner: (partner: PartnerType) => void;
    };
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: PartnerCriteriaType | null;
        fetchPartners: () => void;
    };
}

/**
 * PartnerCollectionView コンポーネント
 */
export const PartnerCollectionView: React.FC<PartnerCollectionViewProps> = ({
                                                                                error,
                                                                                message,
                                                                                searchItems: {
                                                                                    searchPartnerCriteria,
                                                                                    setSearchPartnerCriteria,
                                                                                    handleOpenSearchModal
                                                                                },
                                                                                headerItems: {
                                                                                    handleOpenModal,
                                                                                    handleCheckToggleCollection,
                                                                                    handleDeleteCheckedCollection
                                                                                },
                                                                                collectionItems: {
                                                                                    partners,
                                                                                    handleOpenModal: handleEditPartner,
                                                                                    handleDeletePartner,
                                                                                    handleCheckPartner
                                                                                },
                                                                                pageNationItems: {
                                                                                    pageNation,
                                                                                    criteria,
                                                                                    fetchPartners
                                                                                }
                                                                            }) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message} />
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">取引先</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchPartnerCriteria}
                    setSearchCriteria={setSearchPartnerCriteria}
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
                <PartnerList
                    partners={partners}
                    onEdit={handleEditPartner}
                    onDelete={handleDeletePartner}
                    onCheck={handleCheckPartner}
                />
                <PageNation
                    pageNation={pageNation}
                    callBack={fetchPartners}
                    criteria={criteria}
                />
            </div>
        </div>
    </div>
);