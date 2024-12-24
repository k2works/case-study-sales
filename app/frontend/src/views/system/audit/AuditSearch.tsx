import React, {MouseEventHandler} from "react";
import {
    ApplicationExecutionHistoryType,
    ApplicationExecutionProcessFlag,
    ApplicationExecutionProcessType,
    AuditCriteriaType
} from "../../../models/audit.ts";
import {FormSelect, SingleViewHeaderItem} from "../../Common.tsx";

interface FormProps {
    criteria: AuditCriteriaType,
    setCondition:(criteria: AuditCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void
}
const Form = ({criteria, setCondition, handleClick, handleClose}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormSelect
                id={"search-process-type"}
                label={"処理"}
                value={criteria.processType}
                options={ApplicationExecutionProcessType}
                onChange={(e) => setCondition(
                    {...criteria, processType: e}
                )}/>
            <FormSelect
                id={"search-process-type"}
                label={"状態"}
                value={criteria.processFlag}
                options={ApplicationExecutionProcessFlag}
                onChange={(e) => setCondition(
                    {...criteria, processFlag: e}
                )}/>
            <FormSelect
                id={"search-type"}
                label={"タイプ"}
                value={criteria.type}
                options={ApplicationExecutionHistoryType}
                onChange={(e) => setCondition(
                    {...criteria, type: e}
                )}/>

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

interface AuditSearchSingleViewProps {
    criteria: AuditCriteriaType,
    setCondition:(criteria: AuditCriteriaType) => void,
    handleSelect: (criteria: AuditCriteriaType) => Promise<void>,
    handleClose: () => void
}

export const AuditSearchSingleView: React.FC<AuditSearchSingleViewProps> = ({
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
                    <SingleViewHeaderItem title={"アプリケーション実行履歴"} subtitle={"検索"}/>
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
