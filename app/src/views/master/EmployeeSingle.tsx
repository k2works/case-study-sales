import {Message} from "../../components/application/Message.tsx";
import React from "react";
import {convertToDateInputFormat} from "../../components/application/utils.ts";
import {EmployeeType} from "../../models";
import {SingleViewHeaderActions, SingleViewHeaderItem} from "../Common.tsx";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdate: () => void;
    handleCloseModal: () => void;
}

const Header: React.FC<HeaderProps> = ({
                                           title,
                                           subtitle,
                                           isEditing,
                                           handleCreateOrUpdate,
                                           handleCloseModal
                                       }) => {
    return (
        <div>
            <SingleViewHeaderItem title={title} subtitle={subtitle}/>
            <SingleViewHeaderActions
                isEditing={isEditing}
                handleCreateOrUpdateUser={handleCreateOrUpdate}
                handleCloseModal={handleCloseModal}
            />
        </div>
    );
};

interface EmployeeFormProps {
    newEmployee: any;  // 適切な型を設定してください
    setNewEmployee: (employee: EmployeeType) => void; // 適切な型を設定してください
    isEditing: boolean;
}

const Form: React.FC<EmployeeFormProps> = ({newEmployee, setNewEmployee, isEditing}) => {
    return (
        <div className="single-view-content-item-form">
            <InputField label="社員コード" value={newEmployee.empCode.value} placeholder="社員コード"
                        onChange={(value) => setNewEmployee({
                            ...newEmployee,
                            empCode: {value}
                        })} disabled={isEditing}/>
            <InputField label="姓" value={newEmployee.empName.firstName} placeholder="姓"
                        onChange={(value) => setNewEmployee({
                            ...newEmployee,
                            empName: {...newEmployee.empName, firstName: value}
                        })}/>
            <InputField label="名" value={newEmployee.empName.lastName} placeholder="名"
                        onChange={(value) => setNewEmployee({
                            ...newEmployee,
                            empName: {...newEmployee.empName, lastName: value}
                        })}/>
            <InputField label="姓カナ" value={newEmployee.empName.firstNameKana} placeholder="姓カナ"
                        onChange={(value) => setNewEmployee({
                            ...newEmployee,
                            empName: {...newEmployee.empName, firstNameKana: value}
                        })}/>
            <InputField label="名カナ" value={newEmployee.empName.lastNameKana} placeholder="名カナ"
                        onChange={(value) => setNewEmployee({
                            ...newEmployee,
                            empName: {...newEmployee.empName, lastNameKana: value}
                        })}/>
            <InputField label="電話番号" value={newEmployee.tel.value} placeholder="電話番号"
                        onChange={(value) => setNewEmployee({
                            ...newEmployee,
                            tel: {...newEmployee.tel, value}
                        })}/>
            <InputField label="FAX番号" value={newEmployee.fax.value} placeholder="FAX番号"
                        onChange={(value) => setNewEmployee({
                            ...newEmployee,
                            fax: {...newEmployee.fax, value}
                        })}/>
            <InputField label="職種コード" value={newEmployee.occuCode.value} placeholder="職種コード"
                        onChange={(value) => setNewEmployee({
                            ...newEmployee,
                            occuCode: {...newEmployee.occuCode, value}
                        })}/>
            <InputField label="部門コード" value={newEmployee.department?.departmentId.deptCode.value}
                        placeholder="部門コード" onChange={(value) => setNewEmployee({
                ...newEmployee,
                department: {
                    ...newEmployee.department,
                    departmentId: {
                        ...newEmployee.department?.departmentId,
                        deptCode: {value}
                    }
                }
            })}/>
            <InputField
                label="開始日"
                type="date"
                value={convertToDateInputFormat(newEmployee.department?.departmentId.departmentStartDate.value)}
                placeholder="開始日"
                onChange={(value) => setNewEmployee({
                    ...newEmployee,
                    department: {
                        ...newEmployee.department,
                        departmentId: {
                            ...newEmployee.department?.departmentId,
                            departmentStartDate: {value}
                        }
                    }
                })}
            />
            <InputField label="ユーザーID" value={newEmployee.user?.userId.value} placeholder="ユーザーID"
                        onChange={(value) => setNewEmployee({
                            ...newEmployee,
                            user: {...newEmployee.user, userId: {value}}
                        })}/>
        </div>
    );
};

interface InputFieldProps {
    label: string;
    value: string;
    placeholder: string;
    onChange: (value: string) => void;
    disabled?: boolean;
    type?: string;
}

const InputField: React.FC<InputFieldProps> = ({label, value, placeholder, onChange, disabled, type = "text"}) => {
    return (
        <div className="single-view-content-item-form-item">
            <label className="single-view-content-item-form-item-label">{label}</label>
            <input
                type={type}
                className="single-view-content-item-form-item-input"
                placeholder={placeholder}
                value={value}
                onChange={(e) => onChange(e.target.value)}
                disabled={disabled}
            />
        </div>
    );
};

interface EmployeeSingleViewProps {
    newEmployee: any; // 適切な型を設定してください
    setNewEmployee: (employee: any) => void; // 適切な型を設定してください
    isEditing: boolean;
    error: string | null;
    message: string | null;
    handleCreateOrUpdateEmployee: () => void;
    handleCloseModal: () => void;
}

export const EmployeeSingleView: React.FC<EmployeeSingleViewProps> = ({
                                                                          newEmployee,
                                                                          setNewEmployee,
                                                                          isEditing,
                                                                          error,
                                                                          message,
                                                                          handleCreateOrUpdateEmployee,
                                                                          handleCloseModal
                                                                      }) => (
    <div className="single-view-object-container">
        <Message error={error} message={message}/>
        <div className="single-view-header">
            <Header
                title="社員"
                subtitle={isEditing ? "編集" : "新規作成"}
                isEditing={isEditing}
                handleCreateOrUpdate={handleCreateOrUpdateEmployee}
                handleCloseModal={handleCloseModal}
            />
        </div>
        <div className="single-view-container">
            <div className="single-view-content">
                <div className="single-view-content-item">
                    <Form
                        isEditing={isEditing}
                        newEmployee={newEmployee}
                        setNewEmployee={setNewEmployee}
                    />
                </div>
            </div>
        </div>
    </div>
);
