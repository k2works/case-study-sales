import React from 'react';
import {Message} from "../../components/application/Message.tsx";
import {DepartmentType, LowerType, SlitYnType} from "../../models";
import {convertToDateInputFormat} from "../../components/application/utils.ts";
import {SingleViewHeaderActions, SingleViewHeaderItem} from "../Common.tsx";

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

const Form = ({isEditing, newDepartment, setNewDepartment}: FormProps) => (
        <div className="single-view-content-item-form">

            {/* 部門コードフィールド */}
            <FormItem label="部門コード">
                <input
                    id="deptCode"
                    type="text"
                    className="single-view-content-item-form-item-input"
                    placeholder="部門コード"
                    value={newDepartment.departmentId.deptCode.value}
                    onChange={(e) => setNewDepartment({
                        ...newDepartment,
                        departmentId: {
                            ...newDepartment.departmentId,
                            deptCode: {value: e.target.value}
                        }
                    })}
                    disabled={isEditing}
                />
            </FormItem>

            {/* 部門名フィールド */}
            <FormItem label="部門名">
                <input
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
            </FormItem>

            {/* 開始日フィールド */}
            <FormItem label="開始日">
                <input
                    id="startDate"
                    type="date"
                    className="single-view-content-item-form-item-input"
                    value={convertToDateInputFormat(newDepartment.departmentId.departmentStartDate.value)}
                    onChange={(e) => setNewDepartment({
                        ...newDepartment,
                        departmentId: {
                            ...newDepartment.departmentId,
                            departmentStartDate: {value: e.target.value}
                        }
                    })}
                    disabled={isEditing}
                />
            </FormItem>

            {/* 終了日フィールド */}
            <FormItem label="終了日">
                <input
                    id="endDate"
                    type="date"
                    className="single-view-content-item-form-item-input"
                    value={convertToDateInputFormat(newDepartment.endDate.value)}
                    onChange={(e) => setNewDepartment({
                        ...newDepartment,
                        endDate: {value: e.target.value}
                    })}
                />
            </FormItem>

            {/* 組織階層フィールド */}
            <FormItem label="組織階層">
                <input
                    id="layer"
                    type="number"
                    className="single-view-content-item-form-item-input"
                    value={newDepartment.layer}
                    onChange={(e) => setNewDepartment({
                        ...newDepartment,
                        layer: +e.target.value
                    })}
                />
            </FormItem>

            {/* 部門パスフィールド */}
            <FormItem label="部門パス">
                <input
                    id="path"
                    type="text"
                    className="single-view-content-item-form-item-input"
                    placeholder="部門パス"
                    value={newDepartment.path.value}
                    onChange={(e) => setNewDepartment({
                        ...newDepartment,
                        path: {value: e.target.value}
                    })}
                />
            </FormItem>

            {/* 最下層区分フィールド */}
            <FormItem label="最下層区分">
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
            </FormItem>

            {/* 伝票入力可否フィールド */}
            <FormItem label="伝票入力可否">
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
            </FormItem>

        </div>
    )
;

interface FormItemProps {
    label: string;
    children: React.ReactNode;
}

const FormItem = ({label, children}: FormItemProps) => (
    <div className="single-view-content-item-form-item">
        <label className="single-view-content-item-form-item-label">{label}</label>
        {children}
    </div>
);

interface DepartmentSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    handleCreateOrUpdateDepartment: () => void;
    handleCloseModal: () => void;
    newDepartment: DepartmentType;
    setNewDepartment: React.Dispatch<React.SetStateAction<DepartmentType>>;
}


export const DepartmentSingleView = ({
                                         error,
                                         message,
                                         isEditing,
                                         handleCreateOrUpdateDepartment,
                                         handleCloseModal,
                                         newDepartment,
                                         setNewDepartment
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
