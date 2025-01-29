import React from "react";
import {FormSelect, SingleViewHeaderActions, SingleViewHeaderItem} from "../../../Common.tsx";
import {
    ClosingDateEnumType,
    CustomerType,
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

const Form = ({ newCustomer, setNewCustomer }: FormProps) => {
    return (
        <div className="single-view-content-item-form">
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
            <FormSelect
                id="closingDay2"
                label="締請求2 締日"
                value={newCustomer.invoice.closingInvoice2.closingDay}
                options={ClosingDateEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        invoice: {
                            ...newCustomer.invoice,
                            closingInvoice1: {
                                ...newCustomer.invoice.closingInvoice2,
                                closingDay: e,
                            },
                        },
                    });
                }}
            />
            <FormSelect
                id="paymentMonth2"
                label="締請求2 支払月"
                value={newCustomer.invoice.closingInvoice2.paymentMonth}
                options={PaymentMonthEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        invoice: {
                            ...newCustomer.invoice,
                            closingInvoice2: {
                                ...newCustomer.invoice.closingInvoice2,
                                paymentMonth: e,
                            },
                        },
                    });
                }}
            />
            <FormSelect
                id="paymentDay2"
                label="締請求2 支払日"
                value={newCustomer.invoice.closingInvoice2.paymentDay}
                options={PaymentDayEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        invoice: {
                            ...newCustomer.invoice,
                            closingInvoice2: {
                                ...newCustomer.invoice.closingInvoice2,
                                paymentDay: e,
                            },
                        },
                    });
                }}
            />
            <FormSelect
                id="paymentMethod2"
                label="締請求2 支払方法"
                value={newCustomer.invoice.closingInvoice1.paymentMethod}
                options={PaymentMethodEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        invoice: {
                            ...newCustomer.invoice,
                            closingInvoice2: {
                                ...newCustomer.invoice.closingInvoice2,
                                paymentMethod: e,
                            },
                        },
                    });
                }}
            />
        </div>
    );
};

interface CustomerInvoiceSingleViewProps {
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

export const CustomerInvoiceSingleView: React.FC<CustomerInvoiceSingleViewProps> = ({
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