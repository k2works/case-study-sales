import {Message} from "../../../components/application/Message.tsx";
import React from "react";
import {convertToDateInputFormat} from "../../../components/application/utils.ts";
import {EmployeeType} from "../../../models";
import {FormInput, SingleViewHeaderActions, SingleViewHeaderItem} from "../../Common.tsx";

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
    newEmployee: EmployeeType;
    setNewEmployee: (employee: EmployeeType) => void;
    isEditing: boolean;
}

const Form: React.FC<EmployeeFormProps> = ({newEmployee, setNewEmployee, isEditing}) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="社員コード"
                id="empCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="社員コード"
                value={newEmployee.empCode.value}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    empCode: { value: e.target.value }
                })}
                disabled={isEditing}
            />
            <FormInput
                label="姓"
                id="firstName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="姓"
                value={newEmployee.empName.firstName}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    empName: { ...newEmployee.empName, firstName: e.target.value }
                })}
            />
            <FormInput
                label="名"
                id="lastName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="名"
                value={newEmployee.empName.lastName}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    empName: { ...newEmployee.empName, lastName: e.target.value }
                })}
            />
            <FormInput
                label="姓カナ"
                id="firstNameKana"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="姓カナ"
                value={newEmployee.empName.firstNameKana}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    empName: { ...newEmployee.empName, firstNameKana: e.target.value }
                })}
            />
            <FormInput
                label="名カナ"
                id="lastNameKana"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="名カナ"
                value={newEmployee.empName.lastNameKana}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    empName: { ...newEmployee.empName, lastNameKana: e.target.value }
                })}
            />
            <FormInput
                label="電話番号"
                id="tel"
                type="tel"
                className="single-view-content-item-form-item-input"
                placeholder="電話番号"
                value={newEmployee.tel.value}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    tel: { ...newEmployee.tel, value: e.target.value }
                })}
            />
            <FormInput
                label="FAX番号"
                id="fax"
                type="tel"
                className="single-view-content-item-form-item-input"
                placeholder="FAX番号"
                value={newEmployee.fax.value}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    fax: { ...newEmployee.fax, value: e.target.value }
                })}
            />
            <FormInput
                label="職種コード"
                id="occuCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="職種コード"
                value={newEmployee.occuCode.value}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    occuCode: { ...newEmployee.occuCode, value: e.target.value }
                })}
            />
            <FormInput
                label="部門コード"
                id="deptCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="部門コード"
                value={newEmployee.department?.departmentId.deptCode.value}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    department: {
                        ...newEmployee.department,
                        departmentId: {
                            ...newEmployee.department?.departmentId,
                            deptCode: { value: e.target.value }
                        }
                    }
                })}
            />
            <FormInput
                label="開始日"
                id="departmentStartDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newEmployee.department?.departmentId.departmentStartDate.value)}
                placeholder="開始日"
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    department: {
                        ...newEmployee.department,
                        departmentId: {
                            ...newEmployee.department?.departmentId,
                            departmentStartDate: { value: e.target.value }
                        }
                    }
                })}
            />
            <FormInput
                label="ユーザーID"
                id="userId"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="ユーザーID"
                value={newEmployee.user?.userId.value}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    user: { ...newEmployee.user, userId: { value: e.target.value } }
                })}
            />
        </div>
    );
};

interface EmployeeSingleViewProps {
    newEmployee: EmployeeType
    setNewEmployee: (employee: EmployeeType) => void;
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
