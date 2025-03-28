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
    handleDepartmentSelect: () => void;
    handleUserSelect: () => void;
}

const Form: React.FC<EmployeeFormProps> = ({newEmployee, setNewEmployee, isEditing, handleUserSelect, handleDepartmentSelect}) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="社員コード"
                id="empCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="社員コード"
                value={newEmployee.empCode}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    empCode: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="姓"
                id="firstName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="社員名"
                value={newEmployee.empFirstName}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    empFirstName: e.target.value
                })}
            />
            <FormInput
                label="名"
                id="lastName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="社員名"
                value={newEmployee.empLastName}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    empLastName: e.target.value
                })}
            />
            <FormInput
                label="姓カナ"
                id="firstNameKana"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="社員名カナ"
                value={newEmployee.empFirstNameKana}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    empFirstNameKana: e.target.value
                })}
            />
            <FormInput
                label="名カナ"
                id="lastNameKana"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="社員名カナ"
                value={newEmployee.empLastNameKana}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    empLastNameKana: e.target.value
                })}
            />
            <FormInput
                label="電話番号"
                id="tel"
                type="tel"
                className="single-view-content-item-form-item-input"
                placeholder="電話番号"
                value={newEmployee.tel}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    tel: e.target.value
                })}
            />
            <FormInput
                label="FAX番号"
                id="fax"
                type="tel"
                className="single-view-content-item-form-item-input"
                placeholder="FAX番号"
                value={newEmployee.fax}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    fax: e.target.value
                })}
            />
            <FormInput
                label="職種コード"
                id="occuCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="職種コード"
                value={newEmployee.occuCode}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    occuCode: e.target.value
                })}
            />
            <FormInput
                label="部門コード"
                id="deptCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="部門コード"
                value={newEmployee.departmentCode}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    departmentCode: e.target.value
                })}
                onClick={handleDepartmentSelect}
            />
            <FormInput
                label="開始日"
                id="departmentStartDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newEmployee.departmentStartDate)}
                placeholder="開始日"
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    departmentStartDate: e.target.value
                })}
            />
            <FormInput
                label="ユーザーID"
                id="userId"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="ユーザーID"
                value={newEmployee.userId}
                onChange={(e) => setNewEmployee({
                    ...newEmployee,
                    userId: e.target.value
                })}
                onClick={handleUserSelect}
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
    handleDepartmentSelect: () => void;
    handleUserSelect: () => void;
}

export const EmployeeSingleView: React.FC<EmployeeSingleViewProps> = ({
                                                                          newEmployee,
                                                                          setNewEmployee,
                                                                          isEditing,
                                                                          error,
                                                                          message,
                                                                          handleCreateOrUpdateEmployee,
                                                                          handleCloseModal,
                                                                          handleDepartmentSelect,
                                                                          handleUserSelect
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
                        handleDepartmentSelect={handleDepartmentSelect}
                        handleUserSelect={handleUserSelect}
                    />
                </div>
            </div>
        </div>
    </div>
);
