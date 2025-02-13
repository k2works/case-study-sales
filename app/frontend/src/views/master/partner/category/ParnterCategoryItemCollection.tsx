import React from 'react';
import {PartnerCategoryItemType, PartnerCategoryType} from "../../../../models/master/partner";

interface PartnerCategoryItemCollectionAddListProps {
    partnerCategory: PartnerCategoryType;
    partnerCategoryItems: PartnerCategoryItemType[];
    handleNew: (partnerCategoryItem: PartnerCategoryItemType) => void;
    handleAdd: (code: string) => void;
    handleDelete: (partnerCategoryItem: PartnerCategoryItemType) => void;
}

export const PartnerCategoryItemCollectionAddListView: React.FC<PartnerCategoryItemCollectionAddListProps> = ({
                                                                                                                  partnerCategory,
                                                                                                                  partnerCategoryItems,
                                                                                                                  handleNew,
                                                                                                                  handleAdd,
                                                                                                                  handleDelete
                                                                                                              }) => {
    const [editingPartnerCategoryItemCode, setEditingPartnerCategoryItemCode] = React.useState<number | null>(null);
    const [currentPartnerCategoryItemCode, setCurrentPartnerCategoryItemCode] = React.useState<string>("");
    const [editingPartnerCategoryItemName, setEditingPartnerCategoryItemName] = React.useState<number | null>(null);
    const [currentPartnerCategoryItemName, setCurrentPartnerCategoryItemName] = React.useState<string>("");

    const handlePartnerCategoryItemCodeClick = (index: number, code: string) => {
        setEditingPartnerCategoryItemCode(index);
        setCurrentPartnerCategoryItemCode(code);
    };

    const handlePartnerCategoryItemCodeChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setCurrentPartnerCategoryItemCode(event.target.value);
        partnerCategoryItems[editingPartnerCategoryItemCode!].partnerCategoryItemCode = event.target.value;
    };

    const handlePartnerCategoryItemCodeBlur = () => {
        setEditingPartnerCategoryItemCode(null);
    };

    const handlePartnerCategoryItemNameClick = (index: number, name: string) => {
        setEditingPartnerCategoryItemName(index);
        setCurrentPartnerCategoryItemName(name);
    };

    const handlePartnerCategoryItemNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setCurrentPartnerCategoryItemName(event.target.value);
        partnerCategoryItems[editingPartnerCategoryItemName!].partnerCategoryItemName = event.target.value;
    };

    const handlePartnerCategoryItemNameBlur = () => {
        setEditingPartnerCategoryItemName(null);
    };

    const addNew = () => {
        handleNew({
            partnerCategoryTypeCode: partnerCategory.partnerCategoryTypeCode,
            partnerCategoryItemCode: partnerCategory.partnerCategoryItems.filter(item => item.partnerCategoryItemCode).length > 0
                ? (Number(partnerCategory.partnerCategoryItems.filter(item => item.partnerCategoryItemCode).pop()!.partnerCategoryItemCode) + 1).toString().padStart(3, "0")
                : "001",
            partnerCategoryItemName: "名前",
            partnerCategoryAffiliations: [],
        });
    }
    const addItem = (e: React.MouseEvent<HTMLButtonElement>) => {
        const id = e.currentTarget.getAttribute("data-id");
        if (id === null) {
            return;
        }
        handleAdd(id);
    }

    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">取引先分類</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={addNew} id="add">追加</button>
                    </div>
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {partnerCategoryItems.map((partnerCategoryItem,index) => (
                                <li
                                    className="collection-object-item"
                                    key={`${partnerCategoryItem.partnerCategoryTypeCode}-${partnerCategoryItem.partnerCategoryItemCode}`}
                                >
                                    <div
                                        className="collection-object-item-content"
                                        data-id={`${partnerCategoryItem.partnerCategoryTypeCode}-${partnerCategoryItem.partnerCategoryItemCode}`}
                                    >
                                        <div className="collection-object-item-content-details">分類種別コード</div>
                                        <div
                                            className="collection-object-item-content-name">{partnerCategoryItem.partnerCategoryTypeCode}</div>
                                    </div>
                                    <div
                                        className="collection-object-item-content"
                                        data-id={`${partnerCategoryItem.partnerCategoryTypeCode}-${partnerCategoryItem.partnerCategoryItemCode}`}
                                    >
                                        <div className="collection-object-item-content-details">分類コード</div>
                                        <div className="collection-object-item-content-name">
                                            {editingPartnerCategoryItemCode === index ? (
                                                <input
                                                    type="text"
                                                    value={currentPartnerCategoryItemCode}
                                                    onChange={handlePartnerCategoryItemCodeChange}
                                                    onBlur={() => handlePartnerCategoryItemCodeBlur()}
                                                    autoFocus
                                                />
                                            ) : (
                                                <span
                                                    onClick={() => handlePartnerCategoryItemCodeClick(index, partnerCategoryItem.partnerCategoryItemCode)}>{partnerCategoryItem.partnerCategoryItemCode}</span>
                                            )}
                                        </div>
                                    </div>
                                    <div
                                        className="collection-object-item-content"
                                        data-id={`${partnerCategoryItem.partnerCategoryTypeCode}-${partnerCategoryItem.partnerCategoryItemCode}`}
                                    >
                                        <div className="collection-object-item-content-details">分類名</div>
                                        <div className="collection-object-item-content-name">
                                            {editingPartnerCategoryItemName === index ? (
                                                <input
                                                    type="text"
                                                    value={currentPartnerCategoryItemName}
                                                    onChange={handlePartnerCategoryItemNameChange}
                                                    onBlur={() => handlePartnerCategoryItemNameBlur()}
                                                    autoFocus
                                                />
                                            ) : (
                                                <span
                                                    onClick={() => handlePartnerCategoryItemNameClick(index, partnerCategoryItem.partnerCategoryItemName)}>{partnerCategoryItem.partnerCategoryItemName}</span>
                                            )}
                                        </div>
                                    </div>
                                    <div
                                        className="collection-object-item-actions"
                                        data-id={`${partnerCategoryItem.partnerCategoryTypeCode}-${partnerCategoryItem.partnerCategoryItemCode}`}>
                                        <button className="action-button" onClick={addItem} id="add"
                                                data-id={partnerCategoryItem.partnerCategoryItemCode}>追加
                                        </button>
                                    </div>
                                    <div
                                        className="collection-object-item-actions"
                                        data-id={`${partnerCategoryItem.partnerCategoryTypeCode}-${partnerCategoryItem.partnerCategoryItemCode}`}>
                                        <button className="action-button"
                                                onClick={() => handleDelete(partnerCategoryItem)}>削除
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