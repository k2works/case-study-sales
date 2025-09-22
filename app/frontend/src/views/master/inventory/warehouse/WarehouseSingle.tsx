import React from 'react';
import {Message} from "../../../../components/application/Message.tsx";
import {WarehouseType} from "../../../../models/master/warehouse.ts";
import {FormInput, FormSelect, SingleViewHeaderActions, SingleViewHeaderItem} from "../../../Common.tsx";
import {PrefectureEnumType, WarehouseCategoryEnumType} from "../../../../models/master/shared.ts";

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

            {/* 倉庫区分 */}
            <FormSelect
                id="warehouseCategory"
                label="倉庫区分"
                value={newWarehouse.warehouseCategory}
                options={WarehouseCategoryEnumType}
                onChange={(e) => {
                    setNewWarehouse({
                        ...newWarehouse,
                        warehouseCategory: e,
                    });
                }}
            />

            {/* 住所 */}
            <FormInput
                label="郵便番号"
                id="postalCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="郵便番号"
                value={newWarehouse.postalCode ?? ""}
                onChange={(e) =>
                    setNewWarehouse({
                        ...newWarehouse,
                        postalCode: e.target.value,
                    })
                }
            />
            <FormSelect
                id="prefecture"
                label="都道府県"
                value={newWarehouse.prefecture ?? ""}
                options={PrefectureEnumType}
                onChange={(e) => {
                    setNewWarehouse({
                        ...newWarehouse,
                        prefecture: e,
                    });
                }}
            />
            <FormInput
                label="住所1"
                id="address1"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="住所1"
                value={newWarehouse.address1 ?? ""}
                onChange={(e) =>
                    setNewWarehouse({
                        ...newWarehouse,
                        address1: e.target.value,
                    })
                }
            />
            <FormInput
                label="住所2"
                id="address2"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="住所2"
                value={newWarehouse.address2 ?? ""}
                onChange={(e) =>
                    setNewWarehouse({
                        ...newWarehouse,
                        address2: e.target.value,
                    })
                }
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