import React from 'react';
import {Message} from "../../../components/application/Message.tsx";
import {LocationNumberType} from "../../../models/master/locationnumber.ts";
import {FormInput, SingleViewHeaderActions, SingleViewHeaderItem} from "../../Common.tsx";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdateLocationNumber: () => void;
    handleCloseModal: () => void;
}

const Header = ({
                    title,
                    subtitle,
                    isEditing,
                    handleCreateOrUpdateLocationNumber,
                    handleCloseModal
                }: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdateLocationNumber}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newLocationNumber: LocationNumberType;
    setNewLocationNumber: React.Dispatch<React.SetStateAction<LocationNumberType>>;
}

const Form = ({isEditing, newLocationNumber, setNewLocationNumber}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="倉庫コード"
                id="warehouseCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="倉庫コード"
                value={newLocationNumber.warehouseCode}
                onChange={(e) => setNewLocationNumber({
                    ...newLocationNumber,
                    warehouseCode: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="棚番コード"
                id="locationNumberCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="棚番コード"
                value={newLocationNumber.locationNumberCode}
                onChange={(e) => setNewLocationNumber({
                    ...newLocationNumber,
                    locationNumberCode: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="商品コード"
                id="productCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="商品コード"
                value={newLocationNumber.productCode}
                onChange={(e) => setNewLocationNumber({
                    ...newLocationNumber,
                    productCode: e.target.value
                })}
                disabled={isEditing}
            />
        </div>
    );
};

interface LocationNumberSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    headerItems: {
        handleCreateOrUpdateLocationNumber: () => void;
        handleCloseModal: () => void;
    }
    formItems: {
        newLocationNumber: LocationNumberType;
        setNewLocationNumber: React.Dispatch<React.SetStateAction<LocationNumberType>>;
    }
}

export const LocationNumberSingleView = ({
                                         error,
                                         message,
                                         isEditing,
                                         headerItems: {
                                             handleCreateOrUpdateLocationNumber,
                                             handleCloseModal,
                                         },
                                         formItems: {
                                             newLocationNumber,
                                             setNewLocationNumber
                                         }
                                     }: LocationNumberSingleViewProps) => (
    <div className="single-view-object-container">
        <Message error={error} message={message}/>
        <div className="single-view-header">
            <Header
                title="棚番"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCreateOrUpdateLocationNumber={handleCreateOrUpdateLocationNumber}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newLocationNumber={newLocationNumber}
                        setNewLocationNumber={setNewLocationNumber}
                    />
                </div>
            </div>
        </div>
    </div>
);