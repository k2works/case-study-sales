import React from 'react';
import {Message} from "../../components/application/Message.tsx";
import {PageNation} from "../application/PageNation.tsx";
import {DepartmentIdType, DepartmentType, LowerType, SlitYnType} from "../../types";
import {convertToDateInputFormat} from "../../components/application/utils.ts";
import {SingleViewHeaderActions, SingleViewHeaderItem} from "../Common.tsx";

interface SearchBarProps {
    searchValue: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    onSearch: () => void;
}

const SearchBar: React.FC<SearchBarProps> = ({searchValue, onChange, onSearch}) => (
    <div className="search-container">
        <input type="text" id="search-input" placeholder="部門コードで検索"
               value={searchValue}
               onChange={onChange}/>
        <button className="action-button" id="search-all" onClick={onSearch}>検索</button>
    </div>
);

interface DepartmentItemProps {
    department: DepartmentType;
    onEdit: (department: DepartmentType) => void;
    onDelete: (departmentId: DepartmentType['departmentId']) => void;
}

const DepartmentItem: React.FC<DepartmentItemProps> = ({department, onEdit, onDelete}) => (
    <li className="collection-object-item" key={department.departmentId.deptCode.value}>
        <div className="collection-object-item-content" data-id={department.departmentId.deptCode.value}>
            <div className="collection-object-item-content-details">部門コード</div>
            <div className="collection-object-item-content-name">{department.departmentId.deptCode.value}</div>
        </div>
        <div className="collection-object-item-content" data-id={department.departmentId.deptCode.value}>
            <div className="collection-object-item-content-details">部門名</div>
            <div className="collection-object-item-content-name">{department.departmentName}</div>
        </div>
        <div className="collection-object-item-actions" data-id={department.departmentId.deptCode.value}>
            <button className="action-button" onClick={() => onEdit(department)}>編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={department.departmentId.deptCode.value}>
            <button className="action-button" onClick={() => onDelete(department.departmentId)}>削除</button>
        </div>
    </li>
);

interface DepartmentListProps {
    departments: DepartmentType[];
    onEdit: (department: DepartmentType) => void;
    onDelete: (departmentId: DepartmentType['departmentId']) => void;
}

const DepartmentList: React.FC<DepartmentListProps> = ({departments, onEdit, onDelete}) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {departments.map(department => (
                <DepartmentItem
                    key={department.departmentId.deptCode.value}
                    department={department}
                    onEdit={onEdit}
                    onDelete={onDelete}
                />
            ))}
        </ul>
    </div>
);

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

interface DepartmentCollectionViewProps {
    error: string | null;
    message: string | null;
    searchDepartmentId: { deptCode: { value: string } };
    setSearchDepartmentId: React.Dispatch<React.SetStateAction<DepartmentIdType>>;
    handleSearchDepartment: () => void;
    handleOpenModal: (department?: DepartmentType) => void;
    departments: DepartmentType[];
    handleDeleteDepartment: (departmentId: DepartmentType['departmentId']) => void;
    pageNation: any; // 適切な型を使用してください
    fetchDepartments: () => void;
}

export const DepartmentCollectionView: React.FC<DepartmentCollectionViewProps> = ({
                                                                                      error,
                                                                                      message,
                                                                                      searchDepartmentId,
                                                                                      setSearchDepartmentId,
                                                                                      handleSearchDepartment,
                                                                                      handleOpenModal,
                                                                                      departments,
                                                                                      handleDeleteDepartment,
                                                                                      pageNation,
                                                                                      fetchDepartments
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
                    <button className="action-button" onClick={() => handleOpenModal()}>
                        新規
                    </button>
                </div>
                <DepartmentList
                    departments={departments}
                    onEdit={handleOpenModal}
                    onDelete={handleDeleteDepartment}
                />
                <PageNation pageNation={pageNation} callBack={fetchDepartments}/>
            </div>
        </div>
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
