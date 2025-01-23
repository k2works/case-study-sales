import React, { MouseEventHandler } from "react";
import {PartnerGroupCriteriaType } from "../../../../models";
import { FormInput, SingleViewHeaderItem } from "../../../Common.tsx";

interface PartnerGroupFormProps {
    criteria: PartnerGroupCriteriaType;
    setCondition: (criteria: PartnerGroupCriteriaType) => void;
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const PartnerGroupForm = ({
                              criteria,
                              setCondition,
                              handleClick,
                              handleClose,
                          }: PartnerGroupFormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                id="search-partner-group-code"
                type="text"
                className="single-view-content-item-form-item-input"
                label="取引先グループコード"
                value={criteria.partnerGroupCode?? ""}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        partnerGroupCode: e.target.value
                    })
                }
            />
            <FormInput
                id="search-partner-group-name"
                type="text"
                className="single-view-content-item-form-item-input"
                label="取引先グループ名"
                value={criteria.partnerGroupName ?? ""}
                onChange={(e) =>
                    setCondition({ ...criteria, partnerGroupName: e.target.value })
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

interface PartnerGroupSearchSingleViewProps {
    criteria: PartnerGroupCriteriaType;
    setCondition: (criteria: PartnerGroupCriteriaType) => void;
    handleSelect: (criteria: PartnerGroupCriteriaType) => Promise<void>;
    handleClose: () => void;
}

export const PartnerGroupSearchView: React.FC<PartnerGroupSearchSingleViewProps> = ({
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
                    <SingleViewHeaderItem title="取引先グループ" subtitle="検索" />
                </div>
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <PartnerGroupForm
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