import React, {MouseEventHandler} from "react";
import {FormInput, SingleViewHeaderItem} from "../../Common.tsx";
import {DepartmentCriteriaType} from "../../../models";

interface FormProps {
    criteria: DepartmentCriteriaType,
    setCondition:(criteria: DepartmentCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void
}
const Form = ({criteria, setCondition, handleClick, handleClose}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                id={"search-department-code"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"部門コード"}
                value={criteria.departmentCode}
                onChange={(e) => setCondition(
                    {...criteria, departmentCode: e.target.value}
                )}/>
            <FormInput
                id={"search-department-name"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"部門名"}
                value={criteria.departmentName}
                onChange={(e) => setCondition(
                    {...criteria, departmentName: e.target.value}
                )}/>
            <FormInput
                label="開始日"
                id="startDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={criteria.startDate}
                onChange={(e) => setCondition(
                    {...criteria, startDate: e.target.value}
                )}
            />
            <FormInput
                label="終了日"
                id="endDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={criteria.endDate}
                onChange={(e) => setCondition(
                    {...criteria, endDate: e.target.value}
                )}
            />

            <div className="button-container">
                <button className="action-button" id="search-all" onClick={handleClick}>
                    検索
                </button>
                <button className="action-button" onClick={handleClose} id="cancel">
                    キャンセル
                </button>
            </div>
        </div>
    )
};

interface DepartmentSearchSingleViewProps {
    criteria: DepartmentCriteriaType,
    setCondition:(criteria: DepartmentCriteriaType) => void,
    handleSelect: (criteria: DepartmentCriteriaType) => Promise<void>,
    handleClose: () => void
}

export const DepartmentSearchSingleView: React.FC<DepartmentSearchSingleViewProps> = ({
                                                                                  criteria,
                                                                                  setCondition,
                                                                                  handleSelect,
                                                                                  handleClose,
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
                    <SingleViewHeaderItem title={"部門"} subtitle={"検索"}/>
                </div>
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <Form criteria={criteria} setCondition={setCondition} handleClick={handleClick} handleClose={handleCancel}/>
                    </div>
                </div>
            </div>
        </div>
    );
};
