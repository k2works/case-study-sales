import {RegionCriteriaType} from "../../../../models";
import {FormInput, SingleViewHeaderItem} from "../../../Common.tsx";
import {MouseEventHandler} from "react";

interface RegionFormProps {
    criteria: RegionCriteriaType,
    setCondition: (criteria: RegionCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void
}

const RegionForm = ({criteria, setCondition, handleClick, handleClose}: RegionFormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                id={"search-region-code"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"地域コード"}
                value={criteria.regionCode}
                onChange={(e) => setCondition(
                    {...criteria, regionCode: e.target.value}
                )}/>
            <FormInput
                id={"search-region-name"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"地域名"}
                value={criteria.regionName}
                onChange={(e) => setCondition(
                    {...criteria, regionName: e.target.value}
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

interface RegionSearchSingleViewProps {
    criteria: RegionCriteriaType,
    setCondition: (criteria: RegionCriteriaType) => void,
    handleSelect: (criteria: RegionCriteriaType) => Promise<void>,
    handleClose: () => void
}

export const RegionSearchSingleView: React.FC<RegionSearchSingleViewProps> = ({
                                                                                  criteria,
                                                                                  setCondition,
                                                                                  handleSelect,
                                                                                  handleClose,
                                                                              }) => {
    const handleClick: MouseEventHandler<HTMLButtonElement> = async (e) => {
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
                    <SingleViewHeaderItem title={"地域"} subtitle={"検索"}/>
                </div>
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <RegionForm
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