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

const Form = ({ newVendor, setNewVendor }: FormProps) => {
    return (
        <div className="single-view-content-item-form">
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

interface VendorInvoiceSingleViewProps {
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

export const VendorInvoiceSingleView: React.FC<VendorInvoiceSingleViewProps> = ({
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