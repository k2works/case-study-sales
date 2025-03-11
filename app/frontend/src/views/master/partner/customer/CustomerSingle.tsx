import React from "react";
import {FormInput, FormSelect, SingleViewHeaderActions, SingleViewHeaderItem} from "../../../Common.tsx";
import {
    CustomerEnumType,
    CustomerType,
    CustomerBillingCategoryEnumType
} from "../../../../models/master/partner";
import {Message} from "../../../../components/application/Message.tsx";
import { PrefectureEnumType } from "../../../../models";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdateCustomer: () => void;
    handleCloseModal: () => void;
}

const Header = ({
                    title,
                    subtitle,
                    isEditing,
                    handleCreateOrUpdateCustomer,
                    handleCloseModal,
                }: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle} />
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdateCustomer}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newCustomer: CustomerType;
    setNewCustomer: React.Dispatch<React.SetStateAction<CustomerType>>;
}

const Form = ({ isEditing, newCustomer, setNewCustomer }: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            {/* 顧客コード */}
            <FormInput
                label="顧客コード"
                id="customerCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="顧客コード"
                value={newCustomer.customerCode ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerCode: e.target.value,
                    })
                }
                disabled={isEditing}
            />
            {/* 顧客コード枝番 */}
            <FormInput
                label="顧客コード枝番"
                id="customerBranchNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="顧客コード枝番"
                value={newCustomer.customerBranchNumber ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerBranchNumber: parseInt(e.target.value),
                    })
                }
                disabled={isEditing}
            />

            {/* 顧客区分 */}
            <FormSelect
                id="customerType"
                label="顧客区分"
                value={newCustomer.customerType}
                options={CustomerEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        customerType: e,
                    });
                }}
            />

            {/* 請求先コード */}
            <FormInput
                label="請求先コード"
                id="billingCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="請求先コード"
                value={newCustomer.billingCode ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        billingCode: e.target.value,
                    })
                }
            />

            {/* 請求先コード枝番 */}
            <FormInput
                label="請求先コード枝番"
                id="billingBranchNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="請求先コード枝番"
                value={newCustomer.billingBranchNumber ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        billingBranchNumber: parseInt(e.target.value),
                    })
                }
            />

            {/* 回収先コード */}
            <FormInput
                label="回収先コード"
                id="collectionCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="回収先コード"
                value={newCustomer.collectionCode ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        collectionCode: e.target.value,
                    })
                }
            />
            {/* 回収先コード枝番 */}
            <FormInput
                label="回収先コード枝番"
                id="collectionBranchNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="回収先コード枝番"
                value={newCustomer.collectionBranchNumber ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        collectionBranchNumber: parseInt(e.target.value),
                    })
                }
            />

            {/* 顧客名 */}
            <FormInput
                label="顧客名"
                id="customerName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="顧客名"
                value={newCustomer.customerName ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerName: e.target.value,
                    })
                }
            />

            {/* 担当者情報 */}
            <FormInput
                label="自社担当者コード"
                id="companyRepresentativeCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="自社担当者コード"
                value={newCustomer.companyRepresentativeCode ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        companyRepresentativeCode: e.target.value,
                    })
                }
            />
            <FormInput
                label="顧客担当者名"
                id="customerRepresentativeName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="顧客担当者名"
                value={newCustomer.customerRepresentativeName ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerRepresentativeName: e.target.value,
                    })
                }
            />
            <FormInput
                label="顧客部門名"
                id="customerDepartmentName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="顧客部門名"
                value={newCustomer.customerDepartmentName ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerDepartmentName: e.target.value,
                    })
                }
            />

            {/* 住所 */}
            <FormInput
                label="郵便番号"
                id="postalCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="郵便番号"
                value={newCustomer.customerPostalCode ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerPostalCode: e.target.value,
                    })
                }
            />
            <FormSelect
                id="prefecture"
                label="都道府県"
                value={newCustomer.customerPrefecture ?? ""}
                options={PrefectureEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        customerPrefecture: e,
                    });
                }}
            />
            <FormInput
                label="住所1"
                id="address1"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="住所1"
                value={newCustomer.customerAddress1 ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerAddress1: e.target.value,
                    })
                }
            />
            <FormInput
                label="住所2"
                id="address2"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="住所2"
                value={newCustomer.customerAddress2 ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerAddress2: e.target.value,
                    })
                }
            />

            {/* 電話番号 */}
            <FormInput
                label="電話番号"
                id="customerPhoneNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="電話番号"
                value={newCustomer.customerPhoneNumber ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerPhoneNumber: e.target.value,
                    })
                }
            />

            {/* FAX番号 */}
            <FormInput
                label="FAX番号"
                id="customerFaxNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="FAX番号"
                value={newCustomer.customerFaxNumber ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerFaxNumber: e.target.value,
                    })
                }
            />

            {/* メールアドレス */}
            <FormInput
                label="メールアドレス"
                id="customerEmailAddress"
                type="email"
                className="single-view-content-item-form-item-input"
                placeholder="メールアドレス"
                value={newCustomer.customerEmailAddress ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerEmailAddress: e.target.value,
                    })
                }
            />

            {/* 請求情報 */}
            <FormSelect
                id="customerBillingCategory"
                label="顧客請求区分"
                value={newCustomer.customerBillingType}
                options={CustomerBillingCategoryEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        customerBillingType: e,
                    });
                }}
            />
        </div>
    );
};

interface CustomerSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    headerItems: {
        handleCreateOrUpdateCustomer: () => void;
        handleCloseModal: () => void;
    };
    formItems: {
        newCustomer: CustomerType;
        setNewCustomer: React.Dispatch<React.SetStateAction<CustomerType>>;
    };
}

export const CustomerSingleView: React.FC<CustomerSingleViewProps> = ({
                                                                          error,
                                                                          message,
                                                                          isEditing,
                                                                          headerItems: {
                                                                              handleCreateOrUpdateCustomer,
                                                                              handleCloseModal,
                                                                          },
                                                                          formItems: {
                                                                              newCustomer,
                                                                              setNewCustomer,
                                                                          },
                                                                      }) => (
    <div className="single-view-object-container">
        <Message error={error} message={message} />
        <div className="single-view-header">
            <Header
                title="顧客"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCreateOrUpdateCustomer={handleCreateOrUpdateCustomer}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newCustomer={newCustomer}
                        setNewCustomer={setNewCustomer}
                    />
                </div>
            </div>
        </div>
    </div>
);
