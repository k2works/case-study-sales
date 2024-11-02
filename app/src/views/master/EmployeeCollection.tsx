import React from "react";
import {EmployeeType} from "../../models";
import {Message} from "../../components/application/Message.tsx";
import {PageNation} from "../application/PageNation.tsx";

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
    onCheck: (employee: EmployeeType) => void;
}

const EmployeeListItem: React.FC<EmployeeListItemProps> = ({employee, onEdit, onDelete, onCheck}) => {
    return (
        <li className="collection-object-item" key={employee.empCode.value}>
            <div className="collection-object-item-actions" data-id={employee.empCode.value}>
                <input type="checkbox" className="collection-object-item-checkbox" checked={employee.checked}
                       onChange={() => onCheck(employee)}/>
            </div>
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
    onCheck: (employee: EmployeeType) => void;
}

const EmployeeList: React.FC<EmployeeListProps> = ({employees, onEdit, onDelete, onCheck}) => {
    return (
        <ul className="collection-object-list">
            {employees.map((employee) => (
                <EmployeeListItem
                    key={employee.empCode.value}
                    employee={employee}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    );
};

interface EmployeeCollectionViewProps {
    error: string | null;
    message: string | null;
    searchEmployeeCode: string;
    setSearchEmployeeCode: (value: string) => void;
    handleSearchEmployee: () => void;
    handleOpenModal: () => void;
    employees: any[];
    handleDeleteEmployee: (empCode: string) => void;
    handleCheckEmployee: (employee: EmployeeType) => void;
    handleCheckToggleCollection: () => void;
    handleDeleteCheckedCollection: () => void;
    pageNation: any;
    fetchEmployees: (page: number) => void;
}

export const EmployeeCollectionView: React.FC<EmployeeCollectionViewProps> = ({
                                                                                  error,
                                                                                  message,
                                                                                  searchEmployeeCode,
                                                                                  setSearchEmployeeCode,
                                                                                  handleSearchEmployee,
                                                                                  handleOpenModal,
                                                                                  employees,
                                                                                  handleDeleteEmployee,
                                                                                  handleCheckEmployee,
                                                                                  handleCheckToggleCollection,
                                                                                  handleDeleteCheckedCollection,
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
                        <button className="action-button" onClick={handleCheckToggleCollection}>一括選択</button>
                        <button className="action-button" onClick={handleDeleteCheckedCollection}>一括削除</button>
                    </div>
                    <EmployeeList
                        employees={employees}
                        onEdit={handleOpenModal}
                        onDelete={handleDeleteEmployee}
                        onCheck={handleCheckEmployee}
                    />
                    <PageNation pageNation={pageNation} callBack={fetchEmployees}/>
                </div>
            </div>
        </div>
    );
};
