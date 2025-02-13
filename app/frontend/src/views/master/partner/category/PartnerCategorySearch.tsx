import React, {MouseEventHandler} from "react";
import {PartnerCategoryCriteriaType} from "../../../../models/master/partner";
import {FormInput, SingleViewHeaderItem} from "../../../Common.tsx";

interface PartnerCategoryFormProps {
    criteria: PartnerCategoryCriteriaType;
    setCondition: (criteria: PartnerCategoryCriteriaType) => void;
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const PartnerCategoryForm = ({criteria, setCondition, handleClick, handleClose}: PartnerCategoryFormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                id="search-partner-category-type-code"
                type="text"
                className="single-view-content-item-form-item-input"
                label="取引先分類種別コード"
                value={criteria.partnerCategoryTypeCode ?? ""}
                onChange={(e) =>
                    setCondition({...criteria, partnerCategoryTypeCode: e.target.value})
                }
            />
            <FormInput
                id="search-partner-category-type-name"
                type="text"
                className="single-view-content-item-form-item-input"
                label="取引先分類種別名"
                value={criteria.partnerCategoryTypeName ?? ""}
                onChange={(e) =>
                    setCondition({...criteria, partnerCategoryTypeName: e.target.value})
                }
            />
            <FormInput
                id="search-partner-category-item-code"
                type="text"
                className="single-view-content-item-form-item-input"
                label="取引先分類コード"
                value={criteria.partnerCategoryItemCode ?? ""}
                onChange={(e) =>
                    setCondition({...criteria, partnerCategoryItemCode: e.target.value})
                }
            />
            <FormInput
                id="search-partner-category-item-name"
                type="text"
                className="single-view-content-item-form-item-input"
                label="取引先分類名"
                value={criteria.partnerCategoryItemName ?? ""}
                onChange={(e) =>
                    setCondition({...criteria, partnerCategoryItemName: e.target.value})
                }
            />
            <FormInput
                id="search-partner-code"
                type="text"
                className="single-view-content-item-form-item-input"
                label="取引先コード"
                value={criteria.partnerCode ?? ""}
                onChange={(e) =>
                    setCondition({...criteria, partnerCode: e.target.value})
                }
            />
            <div className="button-container">
                <button className="action-button" id="search-all" onClick={handleClick}>
                    検索
                </button>
                <button className="action-button" onClick={handleClose} id="cancel">
                    キャンセル
                </button>
            </div>
        </div>
    );
};

interface PartnerCategorySearchSingleViewProps {
    criteria: PartnerCategoryCriteriaType;
    setCondition: (criteria: PartnerCategoryCriteriaType) => void;
    handleSelect: (criteria: PartnerCategoryCriteriaType) => Promise<void>;
    handleClose: () => void;
}

export const PartnerCategorySearchView: React.FC<PartnerCategorySearchSingleViewProps> = ({
                                                                                                    criteria,
                                                                                                    setCondition,
                                                                                                    handleSelect,
                                                                                                    handleClose,
                                                                                                }) => {
    const handleClick: MouseEventHandler<HTMLButtonElement> = async (e) => {
        e.preventDefault();
        await handleSelect(criteria);
        handleClose();
    };

    const handleCancel: MouseEventHandler<HTMLButtonElement> = (e) => {
        e.preventDefault();
        handleClose();
    };

    return (
        <div className="single-view-object-container">
            <div className="single-view-header">
                <div>
                    <SingleViewHeaderItem title="取引先分類" subtitle="検索" />
                </div>
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <PartnerCategoryForm
                            criteria={criteria}
                            setCondition={setCondition}
                            handleClick={handleClick}
                            handleClose={handleCancel}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};