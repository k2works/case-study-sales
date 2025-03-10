import React from 'react';
import {FaTimes} from 'react-icons/fa';
import {DepartmentType} from "../../../models";
import {convertToDateInputFormat} from "../../../components/application/utils.ts";
import {PageNation, PageNationType} from "../../application/PageNation.tsx";

interface DepartmentSelectProps {
    handleSelect: () => void;
}

export const DepartmentSelectView: React.FC<DepartmentSelectProps> = ({handleSelect}) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">部門</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={handleSelect} id="select-department">選択
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}

interface DepartmentCollectionSelectProps {
    departments: DepartmentType[];
    handleSelect: (department: DepartmentType) => void;
    handleClose: () => void;
    pageNation: PageNationType | null;
    fetchDepartments: () => void;
}

export const DepartmentCollectionSelectView: React.FC<DepartmentCollectionSelectProps> = ({
                                                                                              departments,
                                                                                              handleSelect,
                                                                                              handleClose,
                                                                                              pageNation,
                                                                                              fetchDepartments
                                                                                          }) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <button className="close-modal-button" onClick={handleClose}>
                    <FaTimes aria-hidden="true"/>
                </button>
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">部門一覧</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {departments.map(department => (
                                <li className="collection-object-item"
                                    key={department.departmentCode + "-" + department.startDate}>
                                    <div className="collection-object-item-content"
                                         data-id={department.departmentCode}>
                                        <div
                                            className="collection-object-item-content-details">部門コード
                                        </div>
                                        <div
                                            className="collection-object-item-content-name">{department.departmentCode}</div>
                                    </div>
                                    <div className="collection-object-item-content"
                                         data-id={department.startDate}>
                                        <div
                                            className="collection-object-item-content-details">開始日
                                        </div>
                                        <div
                                            className="collection-object-item-content-name">{convertToDateInputFormat(department.startDate)}</div>
                                    </div>
                                    <div className="collection-object-item-content"
                                         data-id={department.departmentCode}>
                                        <div
                                            className="collection-object-item-content-details">部門名
                                        </div>
                                        <div
                                            className="collection-object-item-content-name">{department.departmentName}</div>
                                    </div>
                                    <div className="collection-object-item-actions"
                                         data-id={department.departmentCode}>
                                        <button className="action-button"
                                                onClick={() => handleSelect(department)}>選択
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
                <PageNation pageNation={pageNation} callBack={fetchDepartments}/>
            </div>
        </div>
    );
};
