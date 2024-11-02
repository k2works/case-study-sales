import {Message} from "../../components/application/Message.tsx";
import {PageNation} from "../application/PageNation.tsx";
import React from "react";
import {convertToDateInputFormat} from "../../components/application/utils.ts";
import {EmployeeType} from "../../types";

interface SearchBarProps {
    searchValue: string;
    onSearchChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    onSearchClick: () => void;
}

const SearchBar: React.FC<SearchBarProps> = ({searchValue, onSearchChange, onSearchClick}) => {
    return (
        <div className="search-container">
            <input
                type="text"
                placeholder="社員コードで検索"
                value={searchValue}
                onChange={onSearchChange}
            />
            <button className="action-button" onClick={onSearchClick}>検索</button>
        </div>
    );
};

interface EmployeeListItemProps {
    employee: EmployeeType;
    onEdit: (employee: EmployeeType) => void;
    onDelete: (empCode: string) => void;
}

const EmployeeListItem: React.FC<EmployeeListItemProps> = ({employee, onEdit, onDelete}) => {
    return (
        <li className="collection-object-item" key={employee.empCode.value}>
            {['社員コード', '名', '姓', '電話番号', 'FAX番号', '部門'].map((label, idx) => (
                <div className="collection-object-item-content" data-id={employee.empCode.value} key={idx}>
                    <div className="collection-object-item-content-details">{label}</div>
                    <div className="collection-object-item-content-name">
                        {label === '社員コード' ? employee.empCode.value :
                            label === '名' ? employee.empName.firstName :
                                label === '姓' ? employee.empName.lastName :
                                    label === '電話番号' ? employee.tel.value :
                                        label === 'FAX番号' ? employee.fax.value :
                                            employee.department?.departmentName}
                    </div>
                </div>
            ))}
            <div className="collection-object-item-actions" data-id={employee.empCode.value}>
                <button className="action-button" onClick={() => onEdit(employee)}>編集</button>
            </div>
            <div className="collection-object-item-actions" data-id={employee.empCode.value}>
                <button className="action-button" onClick={() => onDelete(employee.empCode.value)}>削除</button>
            </div>
        </li>
    );
};

interface EmployeeListProps {
    employees: EmployeeType[];
    onEdit: (employee: EmployeeType) => void;
    onDelete: (empCode: string) => void;
}

const EmployeeList: React.FC<EmployeeListProps> = ({employees, onEdit, onDelete}) => {
    return (
        <ul className="collection-object-list">
            {employees.map((employee) => (
                <EmployeeListItem
                    employee={employee}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    key={employee.empCode.value}
                />
            ))}
        </ul>
    );
};

interface EmployeeSingleViewProps {
    error: string | null;
    message: string | null;
    searchEmployeeCode: string;
    setSearchEmployeeCode: (value: string) => void;
    handleSearchEmployee: () => void;
    handleOpenModal: () => void;
    employees: any[];
    handleDeleteEmployee: (empCode: string) => void;
    pageNation: any;
    fetchEmployees: (page: number) => void;
}

export const EmployeeCollectionView: React.FC<EmployeeSingleViewProps> = ({
                                                                              error,
                                                                              message,
                                                                              searchEmployeeCode,
                                                                              setSearchEmployeeCode,
                                                                              handleSearchEmployee,
                                                                              handleOpenModal,
                                                                              employees,
                                                                              handleDeleteEmployee,
                                                                              pageNation,
                                                                              fetchEmployees
                                                                          }) => {
    return (
        <div className="collection-view-object-container">
            <div className="view-message-container" id="message">
                <Message error={error} message={message}/>
            </div>
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h1 className="single-view-title">社員</h1>
                    </div>
                </div>
                <div className="collection-view-content">
                    <SearchBar
                        searchValue={searchEmployeeCode}
                        onSearchChange={(e) => setSearchEmployeeCode(e.target.value)}
                        onSearchClick={handleSearchEmployee}
                    />
                    <div className="button-container">
                        <button className="action-button" onClick={() => handleOpenModal()}>新規</button>
                    </div>
                    <EmployeeList
                        employees={employees}
                        onEdit={handleOpenModal}
                        onDelete={handleDeleteEmployee}
                    />
                    <PageNation pageNation={pageNation} callBack={fetchEmployees}/>
                </div>
            </div>
        </div>
    );
};

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    onCreateOrUpdate: () => void;
    onClose: () => void;
}

const Header: React.FC<HeaderProps> = ({title, subtitle, isEditing, onCreateOrUpdate, onClose}) => {
    return (
        <div className="single-view-header">
            <div className="single-view-header-item">
                <h1 className="single-view-title">{title}</h1>
                <p className="single-view-subtitle">{subtitle}</p>
            </div>
            <div className="collection-object-item-actions">
                <div className="button-container">
                    <button className="action-button" onClick={onCreateOrUpdate}>
                        {isEditing ? "更新" : "作成"}
                    </button>
                    <button className="action-button" onClick={onClose}>キャンセル</button>
                </div>
            </div>
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
        <div className="single-view-content-item">
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


interface SingleEmployeeViewProps {
    newEmployee: any; // 適切な型を設定してください
    setNewEmployee: (employee: any) => void; // 適切な型を設定してください
    isEditing: boolean;
    error: string | null;
    message: string | null;
    handleCreateOrUpdateEmployee: () => void;
    handleCloseModal: () => void;
}

export const SingleEmployeeView: React.FC<SingleEmployeeViewProps> = ({
                                                                          newEmployee,
                                                                          setNewEmployee,
                                                                          isEditing,
                                                                          error,
                                                                          message,
                                                                          handleCreateOrUpdateEmployee,
                                                                          handleCloseModal
                                                                      }) => {
    return (
        <div className="single-view-object-container">
            <div className="view-message-container" id="message">
                <Message error={error} message={message}/>
            </div>
            <div className="single-view-container">
                <Header
                    title="社員"
                    subtitle={isEditing ? "編集" : "新規作成"}
                    isEditing={isEditing}
                    onCreateOrUpdate={handleCreateOrUpdateEmployee}
                    onClose={handleCloseModal}
                />
                <div className="single-view-content">
                    <Form newEmployee={newEmployee} setNewEmployee={setNewEmployee} isEditing={isEditing}/>
                </div>
            </div>
        </div>
    );
};
