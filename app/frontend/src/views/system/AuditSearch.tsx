import React, {MouseEventHandler} from "react";
import {
    ApplicationExecutionHistoryType,
    ApplicationExecutionProcessFlag,
    ApplicationExecutionProcessType,
    searchAuditCondition
} from "../../models/audit.ts";
import {FormSelect, SingleViewHeaderItem} from "../Common.tsx";


interface AuditSearchSingleViewProps {
    condition: searchAuditCondition,
    setCondition:(condition: searchAuditCondition) => void,
    handleSelect: (condition: searchAuditCondition) => Promise<void>,
    handleClose: () => void
}

export const AuditSearchSingleView: React.FC<AuditSearchSingleViewProps> = ({
                                                                                  condition,
                                                                                  setCondition,
                                                                                  handleSelect,
                                                                                  handleClose,
                                                                              }) => {
    const handleClick: MouseEventHandler<HTMLButtonElement> = async(e) => {
        e.preventDefault();
        await handleSelect(condition);
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
                    <div className="single-view-content-item-form">
                        <FormSelect
                            id={"search-process-type"}
                            label={"処理"}
                            value={condition.processType}
                            options={ApplicationExecutionProcessType}
                            onChange={(e) => setCondition(
                                {...condition, processType: e}
                            )}/>
                        <FormSelect
                            id={"search-process-type"}
                            label={"状態"}
                            value={condition.processFlag}
                            options={ApplicationExecutionProcessFlag}
                            onChange={(e) => setCondition(
                                {...condition, processFlag: e}
                            )}/>
                        <FormSelect
                            id={"search-type"}
                            label={"タイプ"}
                            value={condition.type}
                            options={ApplicationExecutionHistoryType}
                            onChange={(e) => setCondition(
                                {...condition, type: e}
                            )}/>

                        <div className="button-container">
                            <button className="action-button" id="search-all" onClick={handleClick}>
                                検索
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};
