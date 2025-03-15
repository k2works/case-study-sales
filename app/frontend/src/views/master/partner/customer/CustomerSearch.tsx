import React, { MouseEventHandler } from "react";
import {
    CustomerCriteriaType,
    CustomerBillingCategoryEnumType, CustomerEnumType
} from "../../../../models/master/partner";
import { FormInput, FormSelect, SingleViewHeaderItem } from "../../../Common.tsx";
import {PrefectureEnumType} from "../../../../models";

interface CustomerSearchFormProps {
    criteria: CustomerCriteriaType;
    setCondition: (criteria: CustomerCriteriaType) => void;
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const CustomerSearchForm = ({
                                criteria,
                                setCondition,
                                handleClick,
                                handleClose,
                            }: CustomerSearchFormProps) => {
    return (
        <div className="single-view-content-item-form">
            {/* 顧客コード */}
            <FormInput
                id="search-customer-code"
                type="text"
                className="single-view-content-item-form-item-input"
                label="顧客コード"
                value={criteria.customerCode}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        customerCode: e.target.value
                    })
                }
            />
            {/* 顧客名 */}
            <FormInput
                id="search-customer-name"
                type="text"
                className="single-view-content-item-form-item-input"
                label="顧客名"
                value={criteria.customerName}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        customerName: e.target.value
                    })
                }
            />
            {/* 顧客名カナ */}
            <FormInput
                id="search-customer-name-kana"
                type="text"
                className="single-view-content-item-form-item-input"
                label="顧客名カナ"
                value={criteria.customerNameKana}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        customerNameKana: e.target.value
                    })
                }
            />
            {/* 顧客区分 */}
            <FormSelect
                id="search-customer-type"
                label="顧客区分"
                value={criteria.customerType}
                options={CustomerEnumType}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        customerType: e as CustomerEnumType
                    })
                }
            />
            {/* 請求先コード */}
            <FormInput
                id="search-billing-code"
                type="text"
                className="single-view-content-item-form-item-input"
                label="請求先コード"
                value={criteria.billingCode}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        billingCode: e.target.value
                    })
                }
            />
            {/* 回収先コード */}
            <FormInput
                id="search-collection-code"
                type="text"
                className="single-view-content-item-form-item-input"
                label="回収先コード"
                value={criteria.collectionCode}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        collectionCode: e.target.value
                    })
                }
            />
            {/* 担当者情報 */}
            <FormInput
                id="search-company-representative-code"
                type="text"
                className="single-view-content-item-form-item-input"
                label="自社担当者コード"
                value={criteria.companyRepresentativeCode}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        companyRepresentativeCode: e.target.value
                    })
                }
            />
            <FormInput
                id="search-customer-representative-name"
                type="text"
                className="single-view-content-item-form-item-input"
                label="顧客担当者名"
                value={criteria.customerRepresentativeName}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        customerRepresentativeName: e.target.value
                    })
                }
            />
            <FormInput
                id="search-customer-department-name"
                type="text"
                className="single-view-content-item-form-item-input"
                label="顧客部門名"
                value={criteria.customerDepartmentName}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        customerDepartmentName: e.target.value
                    })
                }
            />
            {/* 住所 */}
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
                value={criteria.customerPhoneNumber}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        customerPhoneNumber: e.target.value
                    })
                }
            />
            {/* FAX番号 */}
            <FormInput
                id="search-fax-number"
                type="text"
                className="single-view-content-item-form-item-input"
                label="FAX番号"
                value={criteria.customerFaxNumber}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        customerFaxNumber: e.target.value
                    })
                }
            />
            {/* メールアドレス */}
            <FormInput
                id="search-email-address"
                type="text"
                className="single-view-content-item-form-item-input"
                label="メールアドレス"
                value={criteria.customerEmailAddress}
                onChange={(e) =>
                    setCondition({
                        ...criteria,
                        customerEmailAddress: e.target.value
                    })
                }
            />
            {/* 請求区分 */}
            <FormSelect
                id="customer-billing-category"
                label="請求区分"
                value={criteria.customerBillingCategory}
                options={CustomerBillingCategoryEnumType}
                onChange={(e) => {
                    setCondition({
                        ...criteria,
                        customerBillingCategory: e as CustomerBillingCategoryEnumType
                    });
                }}
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

interface CustomerSearchViewProps {
    criteria: CustomerCriteriaType;
    setCondition: (criteria: CustomerCriteriaType) => void;
    handleSelect: (criteria: CustomerCriteriaType) => Promise<void>;
    handleClose: () => void;
}

/**
 * `CustomerSearchView` コンポーネント
 */
export const CustomerSearchView: React.FC<CustomerSearchViewProps> = ({
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
                <SingleViewHeaderItem title="顧客" subtitle="検索" />
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <CustomerSearchForm
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