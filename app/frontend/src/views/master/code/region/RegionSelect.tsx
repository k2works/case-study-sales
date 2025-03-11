import React from "react";
import {PageNation, PageNationType} from "../../../application/PageNation.tsx";
import {FaTimes} from "react-icons/fa";
import {RegionType} from "../../../../models/master/code";

interface RegionCodeCollectionSelectProps {
    regions: RegionType[];
    handleSelect: (region: RegionType) => void;
    handleClose: () => void;
    pageNation: PageNationType | null;
    fetchRegions: () => void;
}

export const RegionCodeCollectionSelectView: React.FC<RegionCodeCollectionSelectProps> = ({
                                                                                              regions,
                                                                                              handleSelect,
                                                                                              handleClose,
                                                                                              pageNation,
                                                                                              fetchRegions
                                                                                          }) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <button className="close-modal-button" onClick={handleClose}>
                    <FaTimes aria-hidden="true" />
                </button>
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">地域コード選択</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {regions.map((region) => (
                                <li className="collection-object-item" key={region.regionCode}>
                                    <div className="collection-object-item-content" data-id={region.regionCode}>
                                        <div className="collection-object-item-content-details">地域コード</div>
                                        <div
                                            className="collection-object-item-content-name">{region.regionCode}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={region.regionCode}>
                                        <div className="collection-object-item-content-details">地域名</div>
                                        <div
                                            className="collection-object-item-content-name">{region.regionName}</div>
                                    </div>
                                    <div className="collection-object-item-actions" data-id={region.regionCode}>
                                        <button className="action-button" onClick={() => handleSelect(region)}
                                                id="select-region">選択
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
                <PageNation pageNation={pageNation} callBack={fetchRegions} />
            </div>
        </div>
    );
};
