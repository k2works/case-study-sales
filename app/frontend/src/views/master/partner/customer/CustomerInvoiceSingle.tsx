import React from "react";
import {FormSelect, SingleViewHeaderActions, SingleViewHeaderItem} from "../../../Common.tsx";
import {Message} from "../../../../components/application/Message.tsx";
import {CustomerType} from "../../../../models/master/partner";
import { ClosingDateEnumType, PaymentDayEnumType, PaymentMethodEnumType, PaymentMonthEnumType } from "../../../../models/index.ts";

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
                value={newCustomer.customerClosingDay1}
                options={ClosingDateEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        customerClosingDay1: e,
                    });
                }}
            />
            <FormSelect
                id="paymentMonth1"
                label="締請求1 支払月"
                value={newCustomer.customerPaymentMonth1}
                options={PaymentMonthEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        customerPaymentMonth1: e,
                    });
                }}
            />
            <FormSelect
                id="paymentDay1"
                label="締請求1 支払日"
                value={newCustomer.customerPaymentDay1}
                options={PaymentDayEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        customerPaymentDay1: e,
                    });
                }}
            />
            <FormSelect
                id="paymentMethod1"
                label="締請求1 支払方法"
                value={newCustomer.customerPaymentMethod1}
                options={PaymentMethodEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        customerPaymentMethod1: e,
                    });
                }}
            />
            <FormSelect
                id="closingDay2"
                label="締請求2 締日"
                value={newCustomer.customerClosingDay2}
                options={ClosingDateEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        customerClosingDay2: e,
                    });
                }}
            />
            <FormSelect
                id="paymentMonth2"
                label="締請求2 支払月"
                value={newCustomer.customerPaymentMonth2}
                options={PaymentMonthEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        customerPaymentMonth2: e,
                    });
                }}
            />
            <FormSelect
                id="paymentDay2"
                label="締請求2 支払日"
                value={newCustomer.customerPaymentDay2}
                options={PaymentDayEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        customerPaymentDay2: e,
                    });
                }}
            />
            <FormSelect
                id="paymentMethod2"
                label="締請求2 支払方法"
                value={newCustomer.customerPaymentMethod2}
                options={PaymentMethodEnumType}
                onChange={(e) => {
                    setNewCustomer({
                        ...newCustomer,
                        customerPaymentMethod2: e,
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
