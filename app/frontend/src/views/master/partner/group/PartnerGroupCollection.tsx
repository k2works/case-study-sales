import React from "react";
import { Message } from "../../../../components/application/Message.tsx";
import { PageNation, PageNationType } from "../../../application/PageNation.tsx";
import { Search } from "../../../Common.tsx";
import {PartnerGroupCriteriaType, PartnerGroupType} from "../../../../models";

interface PartnerGroupItemProps {
    groupItem: PartnerGroupType;
    onEdit: (group: PartnerGroupType) => void;
    onDelete: (groupId: PartnerGroupType["partnerGroupCode"]["value"]) => void;
    onCheck: (group: PartnerGroupType) => void;
}

const PartnerGroupItem: React.FC<PartnerGroupItemProps> = ({
                                                               groupItem,
                                                               onEdit,
                                                               onDelete,
                                                               onCheck,
                                                           }) => (
    <li className="collection-object-item" key={groupItem.partnerGroupCode.value}>
        <div className="collection-object-item-content" data-id={groupItem.partnerGroupCode.value}>
            <input
                type="checkbox"
                className="collection-object-item-checkbox"
                checked={groupItem.checked}
                onChange={() => onCheck(groupItem)}
            />
        </div>
        <div className="collection-object-item-content" data-id={groupItem.partnerGroupCode.value}>
            <div className="collection-object-item-content-details">取引先グループコード</div>
            <div className="collection-object-item-content-name">{groupItem.partnerGroupCode.value}</div>
        </div>
        <div className="collection-object-item-content" data-id={groupItem.partnerGroupCode.value}>
            <div className="collection-object-item-content-details">取引先グループ名</div>
            <div className="collection-object-item-content-name">{groupItem.partnerGroupName}</div>
        </div>
        <div className="collection-object-item-actions" data-id={groupItem.partnerGroupCode.value}>
            <button className="action-button" onClick={() => onEdit(groupItem)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={groupItem.partnerGroupCode.value}>
            <button className="action-button" onClick={() => onDelete(groupItem.partnerGroupCode.value)} id="delete">削除</button>
        </div>
    </li>
);

interface PartnerGroupListProps {
    groups: PartnerGroupType[];
    onEdit: (group: PartnerGroupType) => void;
    onDelete: (groupId: PartnerGroupType["partnerGroupCode"]["value"]) => void;
    onCheck: (group: PartnerGroupType) => void;
}

const PartnerGroupList: React.FC<PartnerGroupListProps> = ({
                                                               groups,
                                                               onEdit,
                                                               onDelete,
                                                               onCheck,
                                                           }) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {groups.map(group => (
                <PartnerGroupItem
                    key={group.partnerGroupCode.value}
                    groupItem={group}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface PartnerGroupCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchPartnerGroupCriteria: PartnerGroupCriteriaType;
        setSearchPartnerGroupCriteria: (value: PartnerGroupCriteriaType) => void;
        handleOpenSearchModal: () => void;
    };
    headerItems: {
        handleOpenModal: (group?: PartnerGroupType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    };
    collectionItems: {
        partnerGroups: PartnerGroupType[];
        handleOpenModal: (group?: PartnerGroupType) => void;
        handleDeletePartnerGroup: (groupId: PartnerGroupType["partnerGroupCode"]["value"]) => void;
        handleCheckPartnerGroup: (group: PartnerGroupType) => void;
    };
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: PartnerGroupCriteriaType | null;
        fetchGroups: () => void;
    };
}

export const PartnerGroupCollectionView: React.FC<PartnerGroupCollectionViewProps> = ({
                                                                                          error,
                                                                                          message,
                                                                                          searchItems: {
                                                                                              searchPartnerGroupCriteria,
                                                                                              setSearchPartnerGroupCriteria,
                                                                                              handleOpenSearchModal,
                                                                                          },
                                                                                          headerItems: {
                                                                                              handleOpenModal,
                                                                                              handleCheckToggleCollection,
                                                                                              handleDeleteCheckedCollection,
                                                                                          },
                                                                                          collectionItems: {
                                                                                              partnerGroups,
                                                                                              handleOpenModal: handleEditGroup,
                                                                                              handleDeletePartnerGroup,
                                                                                              handleCheckPartnerGroup,
                                                                                          },
                                                                                          pageNationItems: {
                                                                                              pageNation,
                                                                                              criteria,
                                                                                              fetchGroups,
                                                                                          },
                                                                                      }) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message} />
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">取引先グループ</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchPartnerGroupCriteria}
                    setSearchCriteria={setSearchPartnerGroupCriteria}
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
                <PartnerGroupList
                    groups={partnerGroups}
                    onEdit={handleEditGroup}
                    onDelete={handleDeletePartnerGroup}
                    onCheck={handleCheckPartnerGroup}
                />
                <PageNation
                    pageNation={pageNation}
                    callBack={fetchGroups}
                    criteria={criteria}
                />
            </div>
        </div>
    </div>
);