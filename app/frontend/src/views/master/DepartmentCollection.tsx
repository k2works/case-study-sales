import React from "react";
import {DepartmentIdType, DepartmentType} from "../../models";
import {Message} from "../../components/application/Message.tsx";
import {PageNation} from "../application/PageNation.tsx";

interface SearchBarProps {
    searchValue: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    onSearch: () => void;
}

const SearchBar: React.FC<SearchBarProps> = ({searchValue, onChange, onSearch}) => (
    <div className="search-container">
        <input id="search-input"
               type="text"
               placeholder="部門コードで検索"
               value={searchValue}
               onChange={onChange}/>
        <button className="action-button" id="search-all" onClick={onSearch}>検索</button>
    </div>
);

interface DepartmentItemProps {
    department: DepartmentType;
    onEdit: (department: DepartmentType) => void;
    onDelete: (departmentId: DepartmentType['departmentId']) => void;
    onCheck: (department: DepartmentType) => void;
}

const DepartmentItem: React.FC<DepartmentItemProps> = ({department, onEdit, onDelete, onCheck}) => (
    <li className="collection-object-item" key={department.departmentId.deptCode.value}>
        <div className="collection-object-item-content" data-id={department.departmentId.deptCode.value}>
            <input type="checkbox" className="collection-object-item-checkbox" checked={department.checked}
                   onChange={() => onCheck(department)}/>
        </div>
        <div className="collection-object-item-content" data-id={department.departmentId.deptCode.value}>
            <div className="collection-object-item-content-details">部門コード</div>
            <div className="collection-object-item-content-name">{department.departmentId.deptCode.value}</div>
        </div>
        <div className="collection-object-item-content" data-id={department.departmentId.deptCode.value}>
            <div className="collection-object-item-content-details">部門名</div>
            <div className="collection-object-item-content-name">{department.departmentName}</div>
        </div>
        <div className="collection-object-item-actions" data-id={department.departmentId.deptCode.value}>
            <button className="action-button" onClick={() => onEdit(department)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={department.departmentId.deptCode.value}>
            <button className="action-button" onClick={() => onDelete(department.departmentId)} id="delete">削除
            </button>
        </div>
    </li>
);

interface DepartmentListProps {
    departments: DepartmentType[];
    onEdit: (department: DepartmentType) => void;
    onDelete: (departmentId: DepartmentType['departmentId']) => void;
    onCheck: (department: DepartmentType) => void;
}

const DepartmentList: React.FC<DepartmentListProps> = ({departments, onEdit, onDelete, onCheck}) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {departments.map(department => (
                <DepartmentItem
                    key={department.departmentId.deptCode.value}
                    department={department}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface DepartmentCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchDepartmentId: { deptCode: { value: string } };
        setSearchDepartmentId: React.Dispatch<React.SetStateAction<DepartmentIdType>>;
        handleSearchDepartment: () => void;
    }
    headerItems: {
        handleOpenModal: (department?: DepartmentType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    }
    collectionItems: {
        departments: DepartmentType[];
        handleOpenModal: (department?: DepartmentType) => void;
        handleDeleteDepartment: (departmentId: DepartmentType['departmentId']) => void;
        handleCheckDepartment: (department: DepartmentType) => void;
    }
    pageNationItems: {
        pageNation: any; // 適切な型を使用してください
        fetchDepartments: () => void;
    }
}

export const DepartmentCollectionView: React.FC<DepartmentCollectionViewProps> = ({
                                                                                      error,
                                                                                      message,
                                                                                      searchItems: {searchDepartmentId, setSearchDepartmentId, handleSearchDepartment},
                                                                                      headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
                                                                                      collectionItems: { departments, handleDeleteDepartment, handleCheckDepartment },
                                                                                      pageNationItems: { pageNation, fetchDepartments }
                                                                                  }) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">部門</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <SearchBar
                    searchValue={searchDepartmentId.deptCode.value}
                    onChange={(e) => setSearchDepartmentId({
                        ...searchDepartmentId,
                        deptCode: {value: e.target.value},
                        departmentStartDate: {value: ""}
                    })}
                    onSearch={handleSearchDepartment}
                />
                <div className="button-container">
                    <button className="action-button" onClick={() => handleOpenModal()} id="new">
                        新規
                    </button>
                    <button className="action-button" onClick={() => handleCheckToggleCollection()} id="checkAll">
                        一括選択
                    </button>
                    <button className="action-button" onClick={() => handleDeleteCheckedCollection()} id="deleteAll">
                        一括削除
                    </button>
                </div>
                <DepartmentList
                    departments={departments}
                    onEdit={handleOpenModal}
                    onDelete={handleDeleteDepartment}
                    onCheck={handleCheckDepartment}
                />
                <PageNation pageNation={pageNation} callBack={fetchDepartments}/>
            </div>
        </div>
    </div>
);

