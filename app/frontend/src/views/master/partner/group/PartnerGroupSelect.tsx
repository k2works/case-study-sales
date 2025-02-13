import React from 'react';
import {FaTimes} from 'react-icons/fa';
import {PageNation} from "../../../application/PageNation.tsx";
import {PartnerGroupType} from "../../../../models/master/partner";

interface PartnerGroupSelectProps {
    handleSelect: () => void;
}

export const PartnerGroupSelectView: React.FC<PartnerGroupSelectProps> = ({handleSelect}) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">取引先グループ</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={handleSelect} id="select-partner-group">選択</button>
                    </div>
                </div>
            </div>
        </div>
    )
}

interface PartnerGroupCollectionSelectProps {
    partnerGroups: PartnerGroupType[];
    handleSelect: (partnerGroup: PartnerGroupType) => void;
    handleClose: () => void;
    pageNation: any; // 適切な型を使用してください
    fetchPartnerGroups: () => void;
}

export const PartnerGroupCollectionSelectView: React.FC<PartnerGroupCollectionSelectProps> = ({
                                                                                                  partnerGroups,
                                                                                                  handleSelect,
                                                                                                  handleClose,
                                                                                                  pageNation,
                                                                                                  fetchPartnerGroups,
                                                                                              }) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <button className="close-modal-button" onClick={handleClose}>
                    <FaTimes aria-hidden="true" />
                </button>
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">取引先グループ一覧</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {partnerGroups.map(partnerGroup => (
                                <li className="collection-object-item"
                                    key={partnerGroup.partnerGroupCode.value}>
                                    <div className="collection-object-item-content"
                                         data-id={partnerGroup.partnerGroupCode.value}>
                                        <div
                                            className="collection-object-item-content-details">取引先グループコード
                                        </div>
                                        <div
                                            className="collection-object-item-content-name">{partnerGroup.partnerGroupCode.value}</div>
                                    </div>
                                    <div className="collection-object-item-content"
                                         data-id={partnerGroup.partnerGroupCode.value}>
                                        <div
                                            className="collection-object-item-content-details">取引先グループ名
                                        </div>
                                        <div
                                            className="collection-object-item-content-name">{partnerGroup.partnerGroupName}</div>
                                    </div>
                                    <div className="collection-object-item-actions"
                                         data-id={partnerGroup.partnerGroupCode.value}>
                                        <button className="action-button"
                                                onClick={() => handleSelect(partnerGroup)}>選択
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
                <PageNation pageNation={pageNation} callBack={fetchPartnerGroups} />
            </div>
        </div>
    );
};