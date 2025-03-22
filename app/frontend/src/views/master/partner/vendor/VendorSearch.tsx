import React, { MouseEventHandler } from "react";
import { VendorCriteriaType } from "../../../../models/master/partner";
import {FormInput, FormSelect, SingleViewHeaderItem} from "../../../Common.tsx";
import {PrefectureEnumType} from "../../../../models";

interface VendorSearchFormProps {
    criteria: VendorCriteriaType;
    setCondition: (criteria: VendorCriteriaType) => void;
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const VendorSearchForm = ({
                              criteria,
                              setCondition,
                              handleClick,
                              handleClose,
                          }: VendorSearchFormProps) => {
    return (
        <div className="single-view-content-item-form">
            {/* 仕入先コード */}
            <FormInput
                id="search-vendor-code"
                type="text"
                className="single-view-content-item-form-item-input"
                label="仕入先コード"
                value={criteria.vendorCode}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        vendorCode: e.target.value
                    })
                }
            />
            {/* 仕入先名 */}
            <FormInput
                id="search-vendor-name"
                type="text"
                className="single-view-content-item-form-item-input"
                label="仕入先名"
                value={criteria.vendorName}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        vendorName: e.target.value
                    })
                }
            />
            {/* 仕入先担当者名 */}
            <FormInput
                id="search-vendor-contact-name"
                type="text"
                className="single-view-content-item-form-item-input"
                label="仕入先担当者名"
                value={criteria.vendorContactName}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        vendorContactName: e.target.value
                    })
                }
            />
            {/* 仕入先部門名 */}
            <FormInput
                id="search-vendor-department-name"
                type="text"
                className="single-view-content-item-form-item-input"
                label="仕入先部門名"
                value={criteria.vendorDepartmentName}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        vendorDepartmentName: e.target.value
                    })
                }
            />
            {/* 郵便番号 */}
            <FormInput
                id="search-postal-code"
                type="text"
                className="single-view-content-item-form-item-input"
                label="郵便番号"
                value={criteria.postalCode}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        postalCode: e.target.value
                    })
                }
            />
            {/* 都道府県 */}
            <FormSelect
                id="search-prefecture"
                label="都道府県"
                value={criteria.prefecture}
                options={PrefectureEnumType}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        prefecture: e
                    })
                }
            />
            {/* 住所1 */}
            <FormInput
                id="search-address1"
                type="text"
                className="single-view-content-item-form-item-input"
                label="住所1"
                value={criteria.address1}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        address1: e.target.value
                    })
                }
            />
            {/* 住所2 */}
            <FormInput
                id="search-address2"
                type="text"
                className="single-view-content-item-form-item-input"
                label="住所2"
                value={criteria.address2}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        address2: e.target.value
                    })
                }
            />
            {/* 電話番号 */}
            <FormInput
                id="search-phone-number"
                type="text"
                className="single-view-content-item-form-item-input"
                label="電話番号"
                value={criteria.vendorPhoneNumber}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        vendorPhoneNumber: e.target.value
                    })
                }
            />
            {/* FAX番号 */}
            <FormInput
                id="search-fax-number"
                type="text"
                className="single-view-content-item-form-item-input"
                label="FAX番号"
                value={criteria.vendorFaxNumber}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        vendorFaxNumber: e.target.value
                    })
                }
            />
            {/* メールアドレス */}
            <FormInput
                id="search-email-address"
                type="text"
                className="single-view-content-item-form-item-input"
                label="メールアドレス"
                value={criteria.vendorEmailAddress}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        vendorEmailAddress: e.target.value
                    })
                }
            />
            {/* ボタン */}
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

interface VendorSearchViewProps {
    criteria: VendorCriteriaType;
    setCondition: (criteria: VendorCriteriaType) => void;
    handleSelect: (criteria: VendorCriteriaType) => Promise<void>;
    handleClose: () => void;
}

/**
 * `VendorSearchView` コンポーネント
 */
export const VendorSearchView: React.FC<VendorSearchViewProps> = ({
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
                <SingleViewHeaderItem title="仕入先" subtitle="検索" />
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <VendorSearchForm
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