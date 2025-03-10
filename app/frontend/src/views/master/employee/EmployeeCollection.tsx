import React from "react";
import {EmployeeCriteriaType, EmployeeType} from "../../../models";
import {Message} from "../../../components/application/Message.tsx";
import {PageNation, PageNationType} from "../../application/PageNation.tsx";
import {Search} from "../../Common.tsx";

interface EmployeeListItemProps {
    employee: EmployeeType;
    onEdit: (employee: EmployeeType) => void;
    onDelete: (empCode: string) => void;
    onCheck: (employee: EmployeeType) => void;
}

const EmployeeListItem: React.FC<EmployeeListItemProps> = ({employee, onEdit, onDelete, onCheck}) => {
    return (
        <li className="collection-object-item" key={employee.empCode}>
            <div className="collection-object-item-actions" data-id={employee.empCode}>
                <input type="checkbox" className="collection-object-item-checkbox" checked={employee.checked}
                       onChange={() => onCheck(employee)}/>
            </div>
            {['社員コード', '社員名', '電話番号', 'FAX番号', '部門'].map((label, idx) => (
                <div className="collection-object-item-content" data-id={employee.empCode} key={idx}>
                    <div className="collection-object-item-content-details">{label}</div>
                    <div className="collection-object-item-content-name">
                        {label === '社員コード' ? employee.empCode :
                            label === '社員名' ? employee.empName :
                                label === '電話番号' ? employee.tel :
                                    label === 'FAX番号' ? employee.fax :
                                        employee.department?.departmentName}
                    </div>
                </div>
            ))}
            <div className="collection-object-item-actions" data-id={employee.empCode}>
                <button className="action-button" onClick={() => onEdit(employee)} id="edit">編集</button>
            </div>
            <div className="collection-object-item-actions" data-id={employee.empCode}>
                <button className="action-button" onClick={() => onDelete(employee.empCode)} id="delete">削除
                </button>
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
                    key={employee.empCode}
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
    searchItems: {
        searchEmployeeCriteria: EmployeeCriteriaType;
        setSearchEmployeeCriteria: (value: EmployeeCriteriaType) => void;
        handleOpenSearchModal: () => void;
    }
    headerItems: {
        handleOpenModal: () => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    }
    collectionItems: {
        employees: EmployeeType[];
        handleDeleteEmployee: (empCode: string) => void;
        handleCheckEmployee: (employee: EmployeeType) => void;
    }
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: EmployeeCriteriaType | null;
        fetchEmployees: () => void;
    }
}

export const EmployeeCollectionView: React.FC<EmployeeCollectionViewProps> = ({
                                                                                  error,
                                                                                  message,
                                                                                  searchItems: {searchEmployeeCriteria, setSearchEmployeeCriteria, handleOpenSearchModal},
                                                                                  headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
                                                                                  collectionItems: {employees, handleDeleteEmployee, handleCheckEmployee},
                                                                                  pageNationItems: {pageNation, fetchEmployees, criteria}
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
                    <Search
                        searchCriteria={searchEmployeeCriteria}
                        setSearchCriteria={setSearchEmployeeCriteria}
                        handleSearchAudit={handleOpenSearchModal}
                    />
                    <div className="button-container">
                        <button className="action-button" onClick={() => handleOpenModal()} id="new">新規</button>
                        <button className="action-button" onClick={handleCheckToggleCollection}
                                id="selectAll">一括選択
                        </button>
                        <button className="action-button" onClick={handleDeleteCheckedCollection}
                                id="deleteAll">一括削除
                        </button>
                    </div>
                    <EmployeeList
                        employees={employees}
                        onEdit={handleOpenModal}
                        onDelete={handleDeleteEmployee}
                        onCheck={handleCheckEmployee}
                    />
                    <PageNation pageNation={pageNation} callBack={fetchEmployees} criteria={criteria}/>
                </div>
            </div>
        </div>
    );
};

interface EmployeeCollectionAddListProps {
    employees: EmployeeType[];
    handleAdd: () => void;
    handleDelete: (employee: EmployeeType) => void;
}

export const EmployeeCollectionAddListView: React.FC<EmployeeCollectionAddListProps> = ({employees, handleAdd, handleDelete}) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">社員</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={handleAdd} id="add">追加</button>
                    </div>
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {employees.map(employee => (
                                <li className="collection-object-item" key={employee.empCode}>
                                    <div className="collection-object-item-content" data-id={employee.empCode}>
                                        <div className="collection-object-item-content-details">社員コード</div>
                                        <div
                                            className="collection-object-item-content-name">{employee.empCode}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={employee.empCode}>
                                        <div className="collection-object-item-content-details">名前</div>
                                        <div className="collection-object-item-content-name">
                                            {employee.empNameKana}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-actions" data-id={employee.empCode}>
                                        <button className="action-button" onClick={() => handleDelete(employee)}>削除
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    );
};
