import React, {MouseEventHandler} from "react";
import {FormInput, SingleViewHeaderItem} from "../../Common.tsx";
import {AccountCriteriaType, PaymentAccountEnumType} from "../../../models/master/account.ts";

interface FormProps {
    criteria: AccountCriteriaType,
    setCondition:(criteria: AccountCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void
}
const Form = ({criteria, setCondition, handleClick, handleClose}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                id={"search-account-code"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"口座コード"}
                value={criteria.accountCode}
                onChange={(e) => setCondition(
                    {...criteria, accountCode: e.target.value}
                )}/>
            <FormInput
                id={"search-account-name"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"口座名"}
                value={criteria.accountName}
                onChange={(e) => setCondition(
                    {...criteria, accountName: e.target.value}
                )}/>
            <div className="form-item">
                <label>口座区分</label>
                <div className="single-view-content-item-form-radios">
                    <label>
                        <input
                            type="radio"
                            name="accountType"
                            value={PaymentAccountEnumType.銀行}
                            checked={criteria.accountType === PaymentAccountEnumType.銀行}
                            onChange={(e) => setCondition({
                                ...criteria,
                                accountType: e.target.value
                            })}
                        />
                        銀行口座
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="accountType"
                            value={PaymentAccountEnumType.銀行}
                            checked={criteria.accountType === PaymentAccountEnumType.銀行}
                            onChange={(e) => setCondition({
                                ...criteria,
                                accountType: e.target.value
                            })}
                        />
                        現金
                    </label>
                </div>
            </div>
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
            <FormInput
                id={"search-department-code"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"部門コード"}
                value={criteria.departmentCode}
                onChange={(e) => setCondition(
                    {...criteria, departmentCode: e.target.value}
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

interface AccountSearchSingleViewProps {
    criteria: AccountCriteriaType,
    setCondition:(criteria: AccountCriteriaType) => void,
    handleSelect: (criteria: AccountCriteriaType) => Promise<void>,
    handleClose: () => void
}

export const AccountSearchSingleView: React.FC<AccountSearchSingleViewProps> = ({
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
                    <SingleViewHeaderItem title={"口座"} subtitle={"検索"}/>
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