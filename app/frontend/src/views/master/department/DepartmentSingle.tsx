import React from 'react';
import {Message} from "../../../components/application/Message.tsx";
import {DepartmentType, LowerType, SlitYnType} from "../../../models";
import {convertToDateInputFormat} from "../../../components/application/utils.ts";
import {FormInput, SingleViewHeaderActions, SingleViewHeaderItem} from "../../Common.tsx";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdateDepartment: () => void;
    handleCloseModal: () => void;
}

const Header = ({
                    title,
                    subtitle,
                    isEditing,
                    handleCreateOrUpdateDepartment,
                    handleCloseModal
                }: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdateDepartment}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newDepartment: DepartmentType;
    setNewDepartment: React.Dispatch<React.SetStateAction<DepartmentType>>;
}

const Form = ({isEditing, newDepartment, setNewDepartment}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="部門コード"
                id="deptCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="部門コード"
                value={newDepartment.departmentCode}
                onChange={(e) => setNewDepartment({
                    ...newDepartment,
                    departmentCode: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="部門名"
                id="deptName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="部門名"
                value={newDepartment.departmentName}
                onChange={(e) => setNewDepartment({
                    ...newDepartment,
                    departmentName: e.target.value
                })}
            />
            <FormInput
                label="開始日"
                id="startDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newDepartment.startDate)}
                onChange={(e) => setNewDepartment({
                    ...newDepartment,
                    startDate: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="終了日"
                id="endDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newDepartment.endDate)}
                onChange={(e) => setNewDepartment({
                    ...newDepartment,
                    endDate: e.target.value
                })}
            />
            <FormInput
                label="組織階層"
                id="layer"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newDepartment.layer}
                onChange={(e) => setNewDepartment({
                    ...newDepartment,
                    layer: +e.target.value
                })}
            />
            <FormInput
                label="部門パス"
                id="path"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="部門パス"
                value={newDepartment.path}
                onChange={(e) => setNewDepartment({
                    ...newDepartment,
                    path: e.target.value
                })}
            />
            {/* 最下層区分フィールド */}
            <div className="form-item">
                <label>最下層区分</label>
                <div className="single-view-content-item-form-radios">
                    <label>
                        <input
                            type="radio"
                            name="lowerType"
                            value="LOWER"
                            checked={newDepartment.lowerType === LowerType.YES}
                            onChange={(e) => setNewDepartment({
                                ...newDepartment,
                                lowerType: e.target.value
                            })}
                        />
                        YES
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="lowerType"
                            value="NOT_LOWER"
                            checked={newDepartment.lowerType === LowerType.NO}
                            onChange={(e) => setNewDepartment({
                                ...newDepartment,
                                lowerType: e.target.value
                            })}
                        />
                        NO
                    </label>
                </div>
            </div>
            {/* 伝票入力可否フィールド */}
            <div className="form-item">
                <label>伝票入力可否</label>
                <div className="single-view-content-item-form-radios">
                    <label>
                        <input
                            type="radio"
                            name="slitType"
                            value="SLIT"
                            checked={newDepartment.slitYn === SlitYnType.YES}
                            onChange={(e) => setNewDepartment({
                                ...newDepartment,
                                slitYn: e.target.value
                            })}
                        />
                        YES
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="slitType"
                            value="NOT_SLIT"
                            checked={newDepartment.slitYn === SlitYnType.NO}
                            onChange={(e) => setNewDepartment({
                                ...newDepartment,
                                slitYn: e.target.value
                            })}
                        />
                        NO
                    </label>
                </div>
            </div>
        </div>
    );
};

interface DepartmentSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    headerItems: {
        handleCreateOrUpdateDepartment: () => void;
        handleCloseModal: () => void;
    }
    formItems: {
        newDepartment: DepartmentType;
        setNewDepartment: React.Dispatch<React.SetStateAction<DepartmentType>>;
    }
}


export const DepartmentSingleView = ({
                                         error,
                                         message,
                                         isEditing,
                                         headerItems: {
                                             handleCreateOrUpdateDepartment,
                                             handleCloseModal,
                                         },
                                         formItems: {
                                             newDepartment,
                                             setNewDepartment
                                         }
                                     }: DepartmentSingleViewProps) => (
    <div className="single-view-object-container">
        <Message error={error} message={message}/>
        <div className="single-view-header">
            <Header
                title="部門"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCreateOrUpdateDepartment={handleCreateOrUpdateDepartment}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newDepartment={newDepartment}
                        setNewDepartment={setNewDepartment}
                    />
                </div>
            </div>
        </div>
    </div>
);
