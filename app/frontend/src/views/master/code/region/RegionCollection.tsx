import React from "react";
import {RegionCriteriaType, RegionType} from "../../../../models/master/code";
import {Message} from "../../../../components/application/Message.tsx";
import {PageNation, PageNationType} from "../../../application/PageNation.tsx";
import {Search} from "../../../Common.tsx";

interface RegionItemProps {
    region: RegionType;
    onEdit: (region: RegionType) => void;
    onDelete: (regionId: RegionType['regionCode']) => void;
    onCheck: (region: RegionType) => void;
}

const RegionItem: React.FC<RegionItemProps> = ({region, onEdit, onDelete, onCheck}) => (
    <li className="collection-object-item" key={region.regionCode}>
        <div className="collection-object-item-content" data-id={region.regionCode}>
            <input type="checkbox" className="collection-object-item-checkbox" checked={region.checked}
                   onChange={() => onCheck(region)}/>
        </div>
        <div className="collection-object-item-content" data-id={region.regionCode}>
            <div className="collection-object-item-content-details">地域コード</div>
            <div className="collection-object-item-content-name">{region.regionCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={region.regionCode}>
            <div className="collection-object-item-content-details">地域名</div>
            <div className="collection-object-item-content-name">{region.regionName}</div>
        </div>
        <div className="collection-object-item-actions" data-id={region.regionCode}>
            <button className="action-button" onClick={() => onEdit(region)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={region.regionCode}>
            <button className="action-button" onClick={() => onDelete(region.regionCode)} id="delete">削除</button>
        </div>
    </li>
);

interface RegionListProps {
    regions: RegionType[];
    onEdit: (region: RegionType) => void;
    onDelete: (regionId: RegionType['regionCode']) => void;
    onCheck: (region: RegionType) => void;
}

const RegionList: React.FC<RegionListProps> = ({regions, onEdit, onDelete, onCheck}) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {regions.map(region => (
                <RegionItem
                    key={region.regionCode}
                    region={region}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface RegionCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchRegionCriteria: RegionCriteriaType;
        setSearchRegionCriteria: (value: RegionCriteriaType) => void;
        handleOpenSearchModal: () => void;
    }
    headerItems: {
        handleOpenModal: (region?: RegionType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    }
    collectionItems: {
        regions: RegionType[];
        handleOpenModal: (region?: RegionType) => void;
        handleDeleteRegion: (regionId: RegionType['regionCode']) => void;
        handleCheckRegion: (region: RegionType) => void;
    }
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: RegionCriteriaType | null;
        fetchRegions: () => void;
    }
}

export const RegionCollectionView: React.FC<RegionCollectionViewProps> = ({
                                                                              error,
                                                                              message,
                                                                              searchItems: {searchRegionCriteria, setSearchRegionCriteria, handleOpenSearchModal},
                                                                              headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
                                                                              collectionItems: {regions, handleDeleteRegion, handleCheckRegion},
                                                                              pageNationItems: {pageNation, criteria, fetchRegions}
                                                                          }) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">地域</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchRegionCriteria}
                    setSearchCriteria={setSearchRegionCriteria}
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
                <RegionList
                    regions={regions}
                    onEdit={handleOpenModal}
                    onDelete={handleDeleteRegion}
                    onCheck={handleCheckRegion}
                />
                <PageNation pageNation={pageNation} callBack={fetchRegions} criteria={criteria}/>
            </div>
        </div>
    </div>
);
