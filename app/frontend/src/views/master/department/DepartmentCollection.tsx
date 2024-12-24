import React from "react";
import {DepartmentCriteriaType, DepartmentIdType, DepartmentType} from "../../../models";
import {Message} from "../../../components/application/Message.tsx";
import {PageNation, PageNationType} from "../../application/PageNation.tsx";
import {AuditCriteriaType} from "../../../models/audit.ts";

interface SearchProps {
    searchCriteria: DepartmentCriteriaType;
    setSearchCriteria: (value: DepartmentCriteriaType) => void;
    handleSearchAudit: () => void;
}

const Search: React.FC<SearchProps> = ({handleSearchAudit}) => {
    return (
        <div className="search-container">
            <div className="single-view-content-item-form">
                <div className="button-container">
                    <button className="action-button" id="search-all" onClick={handleSearchAudit}>
                        検索
                    </button>
                </div>
            </div>
        </div>
    );
};

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
        searchDepartmentCriteria: DepartmentCriteriaType;
        setSearchDepartmentCriteria: (value: DepartmentCriteriaType) => void;
        handleOpenSearchModal: () => void;
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
        pageNation: PageNationType | null;
        criteria: DepartmentCriteriaType | null;
        fetchDepartments: () => void;
    }
}

export const DepartmentCollectionView: React.FC<DepartmentCollectionViewProps> = ({
                                                                                      error,
                                                                                      message,
                                                                                      searchItems: {searchDepartmentCriteria, setSearchDepartmentCriteria, handleOpenSearchModal},
                                                                                      headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
                                                                                      collectionItems: { departments, handleDeleteDepartment, handleCheckDepartment },
                                                                                      pageNationItems: { pageNation, criteria, fetchDepartments }
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
                <Search
                    searchCriteria={searchDepartmentCriteria}
                    setSearchCriteria={setSearchDepartmentCriteria}
                    handleSearchAudit={handleOpenSearchModal}
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
                <PageNation pageNation={pageNation} callBack={fetchDepartments} criteria={criteria}/>
            </div>
        </div>
    </div>
);

