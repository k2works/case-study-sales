import {EmployeeType} from "../../types";
import React from "react";
import {PageNation} from "../application/PageNation.tsx";

interface EmployeeCollectionProps {
    employees: EmployeeType[];
    handleAdd: () => void;
    handleDelete: (employee: EmployeeType) => void;
}

export const EmployeeCollectionListView: React.FC<EmployeeCollectionProps> = ({employees, handleAdd, handleDelete}) => {
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
                        <button className="action-button" onClick={handleAdd}>追加</button>
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

interface EmployeeCollectionSelectProps {
    employees: EmployeeType[];
    handleSelect: (employee: EmployeeType) => void;
    pageNation: any; // 適切な型を使用してください
    fetchEmployees: () => void;
}

export const EmployeeCollectionSelectView: React.FC<EmployeeCollectionSelectProps> = ({
                                                                                          employees,
                                                                                          handleSelect,
                                                                                          pageNation,
                                                                                          fetchEmployees
                                                                                      }) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">社員</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {employees.map(employee => (
                                <li className="collection-object-item" key={employee.empCode.value}>
                                    <div className="collection-object-item-content" data-id={employee.empCode.value}>
                                        <div className="collection-object-item-content-details">所属部門</div>
                                        <div
                                            className="collection-object-item-content-name">{employee.department?.departmentName}</div>
                                    </div>
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
                                        <button className="action-button" onClick={() => handleSelect(employee)}>選択
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
                <PageNation pageNation={pageNation} callBack={fetchEmployees}/>
            </div>
        </div>
    );
};
