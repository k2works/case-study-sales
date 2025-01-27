import React from "react";
import {FormInput, FormSelect, SingleViewHeaderActions, SingleViewHeaderItem} from "../../../Common.tsx";
import {
    CustomerEnumType,
    ClosingDateEnumType,
    CustomerType,
    CustomerBillingCategoryEnumType, PrefectureEnumType,
    PaymentMonthEnumType,
    PaymentMethodEnumType, PaymentDayEnumType
} from "../../../../models/master/partner";
import {Message} from "../../../../components/application/Message.tsx";

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
                value={newCustomer.customerCode?.code?.value ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerCode: {
                            ...newCustomer.customerCode,
                            code: { value: e.target.value },
                        },
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
                value={newCustomer.customerCode?.branchNumber ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerCode: {
                            ...newCustomer.customerCode,
                            branchNumber: parseInt(e.target.value),
                        },
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
                value={newCustomer.billingCode?.code?.value ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        billingCode: {
                            ...newCustomer.billingCode, // billingCode全体を保持
                            code: {
                                ...newCustomer.billingCode?.code, // codeオブジェクト全体を保持
                                value: e.target.value, // valueに新しい入力値を適用
                            },
                        },
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
                value={newCustomer.billingCode?.branchNumber ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        billingCode: {
                            ...newCustomer.billingCode,
                            branchNumber: parseInt(e.target.value),
                        },
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
                value={newCustomer.collectionCode?.code?.value ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        collectionCode: {
                            ...newCustomer.collectionCode,
                            code: {
                                ...newCustomer.collectionCode?.code,
                                value: e.target.value,
                            },
                        },
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
                value={newCustomer.collectionCode?.branchNumber ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        collectionCode: {
                            ...newCustomer.collectionCode,
                            branchNumber: parseInt(e.target.value),
                        },
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
                value={newCustomer.customerName?.value?.name ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerName: {
                            ...newCustomer.customerName,
                            value: {
                                ...newCustomer.customerName?.value,
                                name: e.target.value,
                            },
                        },
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
                value={newCustomer.customerAddress?.postalCode?.value ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerAddress: {
                            ...newCustomer.customerAddress,
                            postalCode: {
                                ...newCustomer.customerAddress?.postalCode,
                                value: e.target.value,
                            },
                        },
                    })
                }
            />
            <FormSelect
                id="prefecture"
                label="都道府県"
                value={newCustomer.customerAddress?.prefecture ?? ""}
                options={PrefectureEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        customerAddress: {
                            ...newCustomer.customerAddress,
                            prefecture: e,
                        },
                    });
                }}
            />
            <FormInput
                label="住所1"
                id="address1"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="住所1"
                value={newCustomer.customerAddress?.address1 ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerAddress: {
                            ...newCustomer.customerAddress,
                            address1: e.target.value,
                        },
                    })
                }
            />
            <FormInput
                label="住所2"
                id="address2"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="住所2"
                value={newCustomer.customerAddress?.address2 ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerAddress: {
                            ...newCustomer.customerAddress,
                            address2: e.target.value,
                        },
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
                value={newCustomer.customerPhoneNumber?.value ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerPhoneNumber: {
                            ...newCustomer.customerPhoneNumber,
                            value: e.target.value,
                        },
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
                value={newCustomer.customerFaxNumber?.value ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerFaxNumber: {
                            ...newCustomer.customerFaxNumber,
                            value: e.target.value,
                        },
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
                value={newCustomer.customerEmailAddress?.value ?? ""}
                onChange={(e) =>
                    setNewCustomer({
                        ...newCustomer,
                        customerEmailAddress: {
                            ...newCustomer.customerEmailAddress,
                            value: e.target.value,
                        },
                    })
                }
            />

            {/* 請求情報 */}
            <FormSelect
                id="customerBillingCategory"
                label="顧客請求区分"
                value={newCustomer.invoice.customerBillingCategory}
                options={CustomerBillingCategoryEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        invoice: {
                            ...newCustomer.invoice,
                            customerBillingCategory: e,
                        },
                    });
                }}
            />
            <FormSelect
                id="closingDay1"
                label="締請求1 締日"
                value={newCustomer.invoice.closingInvoice1.closingDay}
                options={ClosingDateEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        invoice: {
                            ...newCustomer.invoice,
                            closingInvoice1: {
                                ...newCustomer.invoice.closingInvoice1,
                                closingDay: e,
                            },
                        },
                    });
                }}
            />
            <FormSelect
                id="paymentMonth1"
                label="締請求1 支払月"
                value={newCustomer.invoice.closingInvoice1.paymentMonth}
                options={PaymentMonthEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        invoice: {
                            ...newCustomer.invoice,
                            closingInvoice1: {
                                ...newCustomer.invoice.closingInvoice1,
                                paymentMonth: e,
                            },
                        },
                    });
                }}
            />
            <FormSelect
                id="paymentDay1"
                label="締請求1 支払日"
                value={newCustomer.invoice.closingInvoice1.paymentDay}
                options={PaymentDayEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        invoice: {
                            ...newCustomer.invoice,
                            closingInvoice1: {
                                ...newCustomer.invoice.closingInvoice1,
                                paymentDay: e,
                            },
                        },
                    });
                }}
            />
            <FormSelect
                id="paymentMethod1"
                label="締請求1 支払方法"
                value={newCustomer.invoice.closingInvoice1.paymentMethod}
                options={PaymentMethodEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        invoice: {
                            ...newCustomer.invoice,
                            closingInvoice1: {
                                ...newCustomer.invoice.closingInvoice1,
                                paymentMethod: e,
                            },
                        },
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