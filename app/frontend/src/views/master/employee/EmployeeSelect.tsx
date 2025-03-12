import {EmployeeType} from "../../../models";
import React from "react";
import {PageNation, PageNationType} from "../../application/PageNation.tsx";
import {FaTimes} from "react-icons/fa";

interface EmployeeCollectionSelectProps {
    employees: EmployeeType[];
    handleSelect: (employee: EmployeeType) => void;
    handleClose: () => void;
    pageNation: PageNationType | null;
    fetchEmployees: () => void;
}

export const EmployeeCollectionSelectView: React.FC<EmployeeCollectionSelectProps> = ({
                                                                                          employees,
                                                                                          handleSelect,
                                                                                          handleClose,
                                                                                          pageNation,
                                                                                          fetchEmployees
                                                                                      }) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <button className="close-modal-button" onClick={handleClose}>
                    <FaTimes aria-hidden="true"/>
                </button>
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">社員</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {employees.map(employee => (
                                <li className="collection-object-item" key={employee.empCode}>
                                    <div className="collection-object-item-content" data-id={employee.empCode}>
                                        <div className="collection-object-item-content-details">所属部門</div>
                                        <div
                                            className="collection-object-item-content-name">{employee.departmentCode}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={employee.empCode}>
                                        <div className="collection-object-item-content-details">社員コード</div>
                                        <div
                                            className="collection-object-item-content-name">{employee.empCode}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={employee.empCode}>
                                        <div className="collection-object-item-content-details">名前</div>
                                        <div className="collection-object-item-content-name">
                                            {employee.empFirstName + ' ' + employee.empLastName}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-actions" data-id={employee.empCode}>
                                        <button className="action-button" onClick={() => handleSelect(employee)}
                                                id="select-employee">選択
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
