import React, { MouseEventHandler } from "react";
import {MiscellaneousEnumType, PartnerCriteriaType, TradeProhibitedFlagEnumType} from "../../../models/master/partner";
import {FormInput, FormSelect, SingleViewHeaderItem} from "../../Common.tsx";

interface PartnerSearchFormProps {
    criteria: PartnerCriteriaType;
    setCondition: (criteria: PartnerCriteriaType) => void;
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void;
    handleSelectPartnerGroup: () => void;
}

/**
 * PartnerSearchForm コンポーネント
 */
const PartnerSearchForm = ({
                               criteria,
                               setCondition,
                               handleClick,
                               handleClose,
                               handleSelectPartnerGroup,
                           }: PartnerSearchFormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                id="search-partner-code"
                type="text"
                className="single-view-content-item-form-item-input"
                label="取引先コード"
                value={criteria.partnerCode ?? ""}
                onChange={(e) =>
                    setCondition({ ...criteria, partnerCode: e.target.value })
                }
            />
            <FormInput
                id="search-partner-name"
                type="text"
                className="single-view-content-item-form-item-input"
                label="取引先名"
                value={criteria.partnerName ?? ""}
                onChange={(e) =>
                    setCondition({ ...criteria, partnerName: e.target.value })
                }
            />
            <FormInput
                id="search-partner-name-kana"
                type="text"
                className="single-view-content-item-form-item-input"
                label="取引先名（カナ）"
                value={criteria.partnerNameKana ?? ""}
                onChange={(e) =>
                    setCondition({ ...criteria, partnerNameKana: e.target.value })
                }
            />
            <FormInput
                id="search-postal-code"
                type="text"
                className="single-view-content-item-form-item-input"
                label="郵便番号"
                value={criteria.postalCode ?? ""}
                onChange={(e) =>
                    setCondition({ ...criteria, postalCode: e.target.value })
                }
            />
            <FormInput
                id="search-address1"
                type="text"
                className="single-view-content-item-form-item-input"
                label="住所1"
                value={criteria.address1 ?? ""}
                onChange={(e) =>
                    setCondition({ ...criteria, address1: e.target.value })
                }
            />
            <FormInput
                id="search-address2"
                type="text"
                className="single-view-content-item-form-item-input"
                label="住所2"
                value={criteria.address2 ?? ""}
                onChange={(e) =>
                    setCondition({ ...criteria, address2: e.target.value })
                }
            />
            {/* 取引禁止フラグ */}
            <FormSelect
                id="tradeProhibitedFlag"
                label="取引禁止フラグ"
                value={criteria.tradeProhibitedFlag}
                options={TradeProhibitedFlagEnumType}
                onChange={(e) => {
                    setCondition({ ...criteria, tradeProhibitedFlag: e });
                }}
            />
            {/* 雑区分 */}
            <FormSelect
                id="miscellaneousType"
                label="雑区分"
                value={criteria.miscellaneousType}
                options={MiscellaneousEnumType}
                onChange={(e) => {
                    setCondition({ ...criteria, miscellaneousType: e });
                }}
            />
            {/* 取引先グループコード */}
            <FormInput
                id="partner-group-code"
                type="text"
                className="single-view-content-item-form-item-input"
                label="取引先グループコード"
                value={criteria.partnerGroupCode}
                onChange={(e) =>
                    setCondition({ ...criteria, partnerGroupCode: e.target.value })
                }
                onClick={handleSelectPartnerGroup}
            />
            <div className="button-container">
                <button className="action-button" id="search-all" onClick={handleClick}>
                    検索
                </button>
                <button className="action-button" id="cancel" onClick={handleClose}>
                    キャンセル
                </button>
            </div>
        </div>
    );
};

interface PartnerSearchViewProps {
    criteria: PartnerCriteriaType;
    setCondition: (criteria: PartnerCriteriaType) => void;
    handleSelect: (criteria: PartnerCriteriaType) => Promise<void>;
    handleClose: () => void;
    handleSelectPartnerGroup: () => void;
}

/**
 * PartnerSearchView コンポーネント
 */
export const PartnerSearchView: React.FC<PartnerSearchViewProps> = ({
                                                                        criteria,
                                                                        setCondition,
                                                                        handleSelect,
                                                                        handleClose,
                                                                        handleSelectPartnerGroup
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
                <SingleViewHeaderItem title="取引先" subtitle="検索" />
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <PartnerSearchForm
                            criteria={criteria}
                            setCondition={setCondition}
                            handleClick={handleClick}
                            handleClose={handleCancel}
                            handleSelectPartnerGroup={handleSelectPartnerGroup}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};