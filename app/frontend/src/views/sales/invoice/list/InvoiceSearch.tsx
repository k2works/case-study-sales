import React, {MouseEventHandler} from "react";
import { FormInput, SingleViewHeaderItem } from "../../../Common.tsx";
import { InvoiceCriteriaType } from "../../../../models/sales/invoice";

interface FormProps {
    criteria: InvoiceCriteriaType,
    setCondition: (criteria: InvoiceCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void,
}

const Form = ({criteria, setCondition, handleClick, handleClose}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                id={"search-invoice-number"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"請求番号"}
                value={criteria.invoiceNumber ?? ""}
                onChange={(e) => setCondition({
                    ...criteria, 
                    invoiceNumber: e.target.value
                })}
            />
            <FormInput
                id={"search-invoice-date"}
                type="date"
                className="single-view-content-item-form-item-input"
                label={"請求日"}
                value={criteria.invoiceDate ?? ""}
                onChange={(e) => setCondition({
                    ...criteria, 
                    invoiceDate: e.target.value
                })}
            />
            <FormInput
                id={"search-customer-code"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"顧客コード"}
                value={criteria.customerCode ?? ""}
                onChange={(e) => setCondition({
                    ...criteria, 
                    customerCode: e.target.value
                })}
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

type InvoiceSearchSingleViewProps = {
    criteria: InvoiceCriteriaType;
    setCondition: (criteria: InvoiceCriteriaType) => void,
    handleSelect: (criteria: InvoiceCriteriaType) => Promise<void>,
    handleClose: () => void
};

export const InvoiceSearchSingleView: React.FC<InvoiceSearchSingleViewProps> = ({
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
                    <SingleViewHeaderItem title={"請求"} subtitle={"検索"}/>
                </div>
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <Form
                            criteria={criteria}
                            setCondition={setCondition}
                            handleClick={handleClick}
                            handleClose={handleCancel}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};
