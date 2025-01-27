import React from "react";
import { FormInput, FormSelect, SingleViewHeaderActions, SingleViewHeaderItem } from "../../../Common.tsx";
import {
    ClosingDateEnumType,
    PaymentMonthEnumType,
    PaymentMethodEnumType,
    PaymentDayEnumType,
} from "../../../../models/master/partner";
import { VendorType } from "../../../../models/master/partner";
import { Message } from "../../../../components/application/Message.tsx";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdateVendor: () => void;
    handleCloseModal: () => void;
}

const Header = ({
                    title,
                    subtitle,
                    isEditing,
                    handleCreateOrUpdateVendor,
                    handleCloseModal,
                }: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle} />
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdateVendor}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newVendor: VendorType;
    setNewVendor: React.Dispatch<React.SetStateAction<VendorType>>;
}

const Form = ({ isEditing, newVendor, setNewVendor }: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            {/* 仕入先コード */}
            <FormInput
                label="仕入先コード"
                id="vendorCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="仕入先コード"
                value={newVendor.vendorCode.code.value}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorCode: {
                            ...newVendor.vendorCode,
                            code: { value: e.target.value },
                        },
                    })
                }
                disabled={isEditing}
            />

            {/* 仕入先コード枝番 */}
            <FormInput
                label="仕入先コード枝番"
                id="branchNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="仕入先コード枝番"
                value={newVendor.vendorCode.branchNumber}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorCode: {
                            ...newVendor.vendorCode,
                            branchNumber: parseInt(e.target.value),
                        },
                    })
                }
                disabled={isEditing}
            />

            {/* 仕入先名 */}
            <FormInput
                label="仕入先名"
                id="vendorName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="仕入先名"
                value={newVendor.vendorName.value.name}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorName: {
                            ...newVendor.vendorName,
                            value: {
                                ...newVendor.vendorName?.value,
                                name: e.target.value,
                            },
                        },
                    })
                }
            />

            {/* 担当者名 */}
            <FormInput
                label="担当者名"
                id="vendorContactName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="担当者名"
                value={newVendor.vendorContactName}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorContactName: e.target.value,
                    })
                }
            />

            {/* 部門名 */}
            <FormInput
                label="部門名"
                id="vendorDepartmentName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="部門名"
                value={newVendor.vendorDepartmentName}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorDepartmentName: e.target.value,
                    })
                }
            />
            {/* 郵便番号 */}
            <FormInput
                label="郵便番号"
                id="postalCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="郵便番号"
                value={newVendor.vendorAddress.postalCode.value}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorAddress: {
                            ...newVendor.vendorAddress,
                            postalCode: {
                                ...newVendor.vendorAddress?.postalCode,
                                value: e.target.value,
                            },
                        },
                    })
                }
            />

            {/* 住所 */}
            <FormInput
                label="住所"
                id="address"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="住所"
                value={newVendor.vendorAddress?.address1 ?? ""}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorAddress: {
                            ...newVendor.vendorAddress,
                            address1: e.target.value,
                        },
                    })
                }
            />

            {/* 電話番号 */}
            <FormInput
                label="電話番号"
                id="phoneNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="電話番号"
                value={newVendor.vendorPhoneNumber?.value ?? ""}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorPhoneNumber: {
                            ...newVendor.vendorPhoneNumber,
                            value: e.target.value,
                        },
                    })
                }
            />

            {/* FAX番号 */}
            <FormInput
                label="FAX番号"
                id="faxNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="FAX番号"
                value={newVendor.vendorFaxNumber?.value ?? ""}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorFaxNumber: {
                            ...newVendor.vendorFaxNumber,
                            value: e.target.value,
                        },
                    })
                }
            />

            {/* メールアドレス */}
            <FormInput
                label="メールアドレス"
                id="emailAddress"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="メールアドレス"
                value={newVendor.vendorEmailAddress?.value ?? ""}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorEmailAddress: {
                            ...newVendor.vendorEmailAddress,
                            value: e.target.value,
                        },
                    })
                }
            />

            {/* 支払締請求 */}
            <FormSelect
                id="closingDate"
                label="締日"
                value={newVendor.vendorClosingInvoice.closingDay}
                options={ClosingDateEnumType}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorClosingInvoice: {
                            ...newVendor.vendorClosingInvoice,
                            closingDay: e,
                        },
                    })
                }
            />
            <FormSelect
                id="paymentMonth"
                label="支払月"
                value={newVendor.vendorClosingInvoice.paymentMonth}
                options={PaymentMonthEnumType}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorClosingInvoice: {
                            ...newVendor.vendorClosingInvoice,
                            paymentMonth: e,
                        },
                    })
                }
            />
            <FormSelect
                id="paymentDay"
                label="支払日"
                value={newVendor.vendorClosingInvoice.paymentDay}
                options={PaymentDayEnumType}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorClosingInvoice: {
                            ...newVendor.vendorClosingInvoice,
                            paymentDay: e,
                        },
                    })
                }
            />
            <FormSelect
                id="paymentMethod"
                label="支払方法"
                value={newVendor.vendorClosingInvoice.paymentMethod}
                options={PaymentMethodEnumType}
                onChange={(e) =>
                    setNewVendor({
                        ...newVendor,
                        vendorClosingInvoice: {
                            ...newVendor.vendorClosingInvoice,
                            paymentMethod: e,
                        },
                    })
                }
            />
        </div>
    );
};

interface VendorSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    headerItems: {
        handleCreateOrUpdateVendor: () => void;
        handleCloseModal: () => void;
    };
    formItems: {
        newVendor: VendorType;
        setNewVendor: React.Dispatch<React.SetStateAction<VendorType>>;
    };
}

export const VendorSingleView: React.FC<VendorSingleViewProps> = ({
                                                                      error,
                                                                      message,
                                                                      isEditing,
                                                                      headerItems: {
                                                                          handleCreateOrUpdateVendor,
                                                                          handleCloseModal,
                                                                      },
                                                                      formItems: {
                                                                          newVendor,
                                                                          setNewVendor,
                                                                      },
                                                                  }) => (
    <div className="single-view-object-container">
        <Message error={error} message={message} />
        <div className="single-view-header">
            <Header
                title="仕入先"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCreateOrUpdateVendor={handleCreateOrUpdateVendor}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newVendor={newVendor}
                        setNewVendor={setNewVendor}
                    />
                </div>
            </div>
        </div>
    </div>
);