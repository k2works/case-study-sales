import React from "react";
import {PageNation, PageNationType} from "../../application/PageNation.tsx";
import {FaTimes} from "react-icons/fa";
import {PartnerType} from "../../../models/master/partner";

interface PartnerCollectionSelectProps {
    partners: PartnerType[];
    handleSelect: (partner: PartnerType) => void;
    handleClose: () => void;
    pageNation: PageNationType | null;
    fetchPartners: () => void;
}

export const PartnerCollectionSelectView: React.FC<PartnerCollectionSelectProps> = ({
                                                                                        partners,
                                                                                        handleSelect,
                                                                                        handleClose,
                                                                                        pageNation,
                                                                                        fetchPartners
                                                                                    }) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <button className="close-modal-button" onClick={handleClose}>
                    <FaTimes aria-hidden="true"/>
                </button>
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">取引先</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {partners.map(partner => (
                                <li className="collection-object-item" key={partner.partnerCode}>
                                    <div className="collection-object-item-content" data-id={partner.partnerCode}>
                                        <div className="collection-object-item-content-details">取引先コード</div>
                                        <div
                                            className="collection-object-item-content-name">{partner.partnerCode}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={partner.partnerCode}>
                                        <div className="collection-object-item-content-details">取引先名</div>
                                        <div
                                            className="collection-object-item-content-name">{partner.partnerName}</div>
                                    </div>
                                    <div className="collection-object-item-actions" data-id={partner.partnerCode}>
                                        <button className="action-button" onClick={() => handleSelect(partner)}
                                                id="select-partner">選択
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
                <PageNation pageNation={pageNation} callBack={fetchPartners}/>
            </div>
        </div>
    );
};