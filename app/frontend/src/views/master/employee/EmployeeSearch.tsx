import React, { MouseEventHandler } from "react";
import { FormInput, SingleViewHeaderItem } from "../../Common.tsx";
import {EmployeeCriteriaType} from "../../../models";

interface FormProps {
    criteria: EmployeeCriteriaType,
    setCondition:(criteria: EmployeeCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleDepartmentSelect: () => void
}

const Form = ({criteria, setCondition, handleClick, handleClose, handleDepartmentSelect}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="社員コード"
                id="empCode"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="社員コード"
                value={criteria.empCode}
                onChange={(e) => setCondition(
                    {...criteria, empCode: e.target.value}
                )}
            />
            <FormInput
                label="姓"
                id="firstName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="姓"
                value={criteria.empNameFirst}
                onChange={(e) => setCondition(
                    {...criteria, empNameFirst: e.target.value}
                )}
            />
            <FormInput
                label="名"
                id="lastName"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="名"
                value={criteria.empNameLast}
                onChange={(e) => setCondition(
                    {...criteria, empNameLast: e.target.value}
                )}
            />
            <FormInput
                label="姓(カナ)"
                id="firstNameKana"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="姓(カナ)"
                value={criteria.empNameFirstKana}
                onChange={(e) => setCondition(
                    {...criteria, empNameFirstKana: e.target.value}
                )}
            />
            <FormInput
                label="名(カナ)"
                id="lastNameKana"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="名(カナ)"
                value={criteria.empNameLastKana}
                onChange={(e) => setCondition(
                    {...criteria, empNameLastKana: e.target.value}
                )}
            />
            <FormInput
                label="電話番号"
                id="tel"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="電話番号"
                value={criteria.phoneNumber}
                onChange={(e) => setCondition(
                    {...criteria, phoneNumber: e.target.value}
                )}
            />
            <FormInput
                label="FAX"
                id="fax"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="メールアドレス"
                value={criteria.faxNumber}
                onChange={(e) => setCondition(
                    {...criteria, faxNumber: e.target.value}
                )}
            />
            <FormInput
                label="部門コード"
                id="department"
                type="text"
                className="single-view-content-item-form-item-input"
                placeholder="部門コード"
                value={criteria.departmentCode}
                onChange={(e) => setCondition(
                    {...criteria, departmentCode: e.target.value}
                )}
                onClick={handleDepartmentSelect}
            />
            {/* 他のフォーム入力項目をここに追加 */}

            <div className="button-container">
                <button className="action-button" id="search-all" onClick={handleClick}>
                    検索
                </button>
                <button className="action-button" onClick={handleClose} id="cancel">
                    キャンセル
                </button>
            </div>
        </div>
    );
};

interface EmployeeSearchSingleViewProps {
    criteria: EmployeeCriteriaType,
    setCondition: (criteria: EmployeeCriteriaType) => void,
    handleSelect: (criteria: EmployeeCriteriaType) => Promise<void>,
    handleClose: () => void,
    handleDepartmentSelect: () => void
}

export const EmployeeSearchSingleView: React.FC<EmployeeSearchSingleViewProps> = ({
                                                                                      criteria,
                                                                                      setCondition,
                                                                                      handleSelect,
                                                                                      handleClose,
                                                                                      handleDepartmentSelect
                                                                                  }) => {
    const handleClick: MouseEventHandler<HTMLButtonElement> = async(e) => {
        e.preventDefault();
        await handleSelect(criteria);
        handleClose();
    }

    const handleCancel: MouseEventHandler<HTMLButtonElement> = (e) => {
        e.preventDefault();
        handleClose();
    }

    return (
        <div className="single-view-object-container">
            <div className="single-view-header">
                <div>
                    <SingleViewHeaderItem title={"社員"} subtitle={"検索"}/>
                </div>
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <Form criteria={criteria} setCondition={setCondition} handleClick={handleClick} handleClose={handleCancel} handleDepartmentSelect={handleDepartmentSelect}/>
                    </div>
                </div>
            </div>
        </div>
    );
};