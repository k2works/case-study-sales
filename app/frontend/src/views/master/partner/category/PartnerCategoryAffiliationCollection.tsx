import React from 'react';
import {PartnerCategoryAffiliationType} from '../../../../models/master/partner';
import {FaTimes} from "react-icons/fa";

interface PartnerCategoryAffiliationCollectionAddListProps {
    partnerCategoryAffiliations: PartnerCategoryAffiliationType[];
    handleAdd: () => void;
    handleDelete: (partnerCategoryAffiliation: PartnerCategoryAffiliationType) => void;
    handleClose: () => void;
}

export const PartnerCategoryAffiliationCollectionAddListView: React.FC<PartnerCategoryAffiliationCollectionAddListProps> = ({
                                                                                                                                partnerCategoryAffiliations,
                                                                                                                                handleAdd,
                                                                                                                                handleDelete,
                                                                                                                                handleClose
                                                                                                                            }) => {
    const [editingPartnerCode, setEditingPartnerCode] = React.useState<number | null>(null);
    const [currentPartnerCode, setCurrentPartnerCode] = React.useState<string>("");

    const handlePartnerCodeClick = (index: number, code: string) => {
        setEditingPartnerCode(index);
        setCurrentPartnerCode(code);
    };
    const handlePartnerCodeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setCurrentPartnerCode(e.target.value);
        partnerCategoryAffiliations[editingPartnerCode!].partnerCode = e.target.value;
    };
    const handlePartnerCodeBlur = () => {
        setEditingPartnerCode(null);
    };

    const addNew = () => {
        handleAdd();
    }

    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <button className="close-modal-button" onClick={handleClose}>
                    <FaTimes aria-hidden="true"/>
                </button>
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">取引先分類所属</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={addNew} id="add-partner">
                            追加
                        </button>
                    </div>
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {partnerCategoryAffiliations.map((affiliation, index) => (
                                <li
                                    className="collection-object-item"
                                    key={`${affiliation.partnerCategoryTypeCode}-${affiliation.partnerCategoryItemCode}-${affiliation.partnerCode}`}
                                >
                                    <div
                                        className="collection-object-item-content"
                                        data-id={`${affiliation.partnerCategoryTypeCode}-${affiliation.partnerCategoryItemCode}-${affiliation.partnerCode}`}
                                    >
                                        <div className="collection-object-item-content-details">分類種別コード</div>
                                        <div className="collection-object-item-content-name">
                                            {affiliation.partnerCategoryTypeCode}
                                        </div>
                                    </div>
                                    <div
                                        className="collection-object-item-content"
                                        data-id={`${affiliation.partnerCategoryTypeCode}-${affiliation.partnerCategoryItemCode}-${affiliation.partnerCode}`}
                                    >
                                        <div className="collection-object-item-content-details">取引先コード</div>
                                        <div className="collection-object-item-content-name">
                                            {editingPartnerCode === index ? (
                                                <input
                                                    type="text"
                                                    value={currentPartnerCode}
                                                    onChange={handlePartnerCodeChange}
                                                    onBlur={() => handlePartnerCodeBlur()}
                                                    autoFocus
                                                />
                                            ) : (
                                                <span
                                                    onClick={() => handlePartnerCodeClick(index, affiliation.partnerCode)}>{affiliation.partnerCode}</span>
                                            )}
                                        </div>
                                    </div>
                                    <div
                                        className="collection-object-item-content"
                                        data-id={`${affiliation.partnerCategoryTypeCode}-${affiliation.partnerCategoryItemCode}-${affiliation.partnerCode}`}
                                    >
                                        <div className="collection-object-item-content-details">分類コード</div>
                                        <div className="collection-object-item-content-name">
                                            {affiliation.partnerCategoryItemCode}
                                        </div>
                                    </div>
                                    <div
                                        className="collection-object-item-actions"
                                        data-id={`${affiliation.partnerCategoryTypeCode}-${affiliation.partnerCategoryItemCode}-${affiliation.partnerCode}`}
                                    >
                                        <button
                                            className="action-button"
                                            onClick={() => handleDelete(affiliation)}
                                        >
                                            削除
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