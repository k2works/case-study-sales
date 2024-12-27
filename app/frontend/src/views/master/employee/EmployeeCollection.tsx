import React from "react";
import {EmployeeType} from "../../../models";
import {Message} from "../../../components/application/Message.tsx";
import {PageNation} from "../../application/PageNation.tsx";

interface SearchBarProps {
    searchValue: string;
    onSearchChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    onSearchClick: () => void;
}

const SearchBar: React.FC<SearchBarProps> = ({searchValue, onSearchChange, onSearchClick}) => {
    return (
        <div className="search-container">
            <input
                id="search-input"
                type="text"
                placeholder="社員コードで検索"
                value={searchValue}
                onChange={onSearchChange}
            />
            <button className="action-button" id="search-all" onClick={onSearchClick}>検索</button>
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
            {['社員コード', '姓', '名', '電話番号', 'FAX番号', '部門'].map((label, idx) => (
                <div className="collection-object-item-content" data-id={employee.empCode.value} key={idx}>
                    <div className="collection-object-item-content-details">{label}</div>
                    <div className="collection-object-item-content-name">
                        {label === '社員コード' ? employee.empCode.value :
                            label === '姓' ? employee.empName.firstName :
                                label === '名' ? employee.empName.lastName :
                                    label === '電話番号' ? employee.tel.value :
                                        label === 'FAX番号' ? employee.fax.value :
                                            employee.department?.departmentName}
                    </div>
                </div>
            ))}
            <div className="collection-object-item-actions" data-id={employee.empCode.value}>
                <button className="action-button" onClick={() => onEdit(employee)} id="edit">編集</button>
            </div>
            <div className="collection-object-item-actions" data-id={employee.empCode.value}>
                <button className="action-button" onClick={() => onDelete(employee.empCode.value)} id="delete">削除
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
    searchItems: {
        searchEmployeeCode: string;
        setSearchEmployeeCode: React.Dispatch<React.SetStateAction<string>>;
        handleSearchEmployee: () => void;
    }
    headerItems: {
        handleOpenModal: () => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    }
    collectionItems: {
        employees: any[];
        handleDeleteEmployee: (empCode: string) => void;
        handleCheckEmployee: (employee: EmployeeType) => void;
    }
    pageNationItems: {
        pageNation: any;
        fetchEmployees: (page: number) => void;
    }
}

export const EmployeeCollectionView: React.FC<EmployeeCollectionViewProps> = ({
                                                                                  error,
                                                                                  message,
                                                                                  searchItems: {searchEmployeeCode, setSearchEmployeeCode, handleSearchEmployee},
                                                                                  headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
                                                                                  collectionItems: {employees, handleDeleteEmployee, handleCheckEmployee},
                                                                                  pageNationItems: {pageNation, fetchEmployees}
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
                    <PageNation pageNation={pageNation} callBack={fetchEmployees}/>
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
                                <li className="collection-object-item" key={employee.empCode.value}>
                                    <div className="collection-object-item-content" data-id={employee.empCode.value}>
                                        <div className="collection-object-item-content-details">社員コード</div>
                                        <div
                                            className="collection-object-item-content-name">{employee.empCode.value}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={employee.empCode.value}>
                                        <div className="collection-object-item-content-details">名前</div>
                                        <div className="collection-object-item-content-name">
                                            {employee.empName.firstNameKana} {employee.empName.lastNameKana}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-actions" data-id={employee.empCode.value}>
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

