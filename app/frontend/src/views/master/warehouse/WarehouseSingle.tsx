import React from 'react';
import {Message} from "../../../components/application/Message.tsx";
import {WarehouseType} from "../../../models/master/warehouse.ts";
import {FormInput, SingleViewHeaderActions, SingleViewHeaderItem} from "../../Common.tsx";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdateWarehouse: () => void;
    handleCloseModal: () => void;
}

const Header = ({
                    title,
                    subtitle,
                    isEditing,
                    handleCreateOrUpdateWarehouse,
                    handleCloseModal
                }: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdateWarehouse}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newWarehouse: WarehouseType;
    setNewWarehouse: React.Dispatch<React.SetStateAction<WarehouseType>>;
}

const Form = ({isEditing, newWarehouse, setNewWarehouse}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="倉庫コード"
                id="warehouseCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="倉庫コード"
                value={newWarehouse.warehouseCode}
                onChange={(e) => setNewWarehouse({
                    ...newWarehouse,
                    warehouseCode: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="倉庫名"
                id="warehouseName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="倉庫名"
                value={newWarehouse.warehouseName}
                onChange={(e) => setNewWarehouse({
                    ...newWarehouse,
                    warehouseName: e.target.value
                })}
            />
        </div>
    );
};

interface WarehouseSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    headerItems: {
        handleCreateOrUpdateWarehouse: () => void;
        handleCloseModal: () => void;
    }
    formItems: {
        newWarehouse: WarehouseType;
        setNewWarehouse: React.Dispatch<React.SetStateAction<WarehouseType>>;
    }
}

export const WarehouseSingleView = ({
                                         error,
                                         message,
                                         isEditing,
                                         headerItems: {
                                             handleCreateOrUpdateWarehouse,
                                             handleCloseModal,
                                         },
                                         formItems: {
                                             newWarehouse,
                                             setNewWarehouse
                                         }
                                     }: WarehouseSingleViewProps) => (
    <div className="single-view-object-container">
        <Message error={error} message={message}/>
        <div className="single-view-header">
            <Header
                title="倉庫"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCreateOrUpdateWarehouse={handleCreateOrUpdateWarehouse}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newWarehouse={newWarehouse}
                        setNewWarehouse={setNewWarehouse}
                    />
                </div>
            </div>
        </div>
    </div>
);