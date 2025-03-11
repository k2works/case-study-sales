import React, {useState} from "react";
import {BomType} from "../../../../models/master/product";

interface BomCollectionProps {
    boms: BomType[];
    handleAdd: () => void;
    handleDelete: (product: BomType) => void;
}

export const BomCollectionAddListView: React.FC<BomCollectionProps> = ({
                                                                    boms,
                                                                    handleAdd,
                                                                    handleDelete,
                                                                }) => {
    const [editingQuantity, setEditingQuantity] = useState<number | null>(null);
    const [currentQuantity, setCurrentQuantity] = useState<string>("");

    const handleQuantityClick = (index: number, quantity: string) => {
        setEditingQuantity(index);
        setCurrentQuantity(quantity);
    };

    const handleQuantityChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setCurrentQuantity(event.target.value);
        boms[editingQuantity!].componentQuantity.amount = parseInt(event.target.value);
    };

    const handleQuantityBlur = () => {
        setEditingQuantity(null);
    };

    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">部品表</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={handleAdd} id="add">追加</button>
                    </div>
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {boms.map((bom, index) => (
                                <li className="collection-object-item" key={bom.componentCode}>
                                    <div className="collection-object-item-content"
                                         data-id={bom.componentCode}>
                                        <div className="collection-object-item-content-details">部品コード</div>
                                        <div
                                            className="collection-object-item-content-name">{bom.componentCode}</div>
                                    </div>
                                    <div className="collection-object-item-content"
                                         data-id={bom.componentCode}>
                                        <div className="collection-object-item-content-details">部品数量</div>
                                        <div className="collection-object-item-content-name">
                                            {editingQuantity === index ? (
                                                <input
                                                    type="text"
                                                    value={currentQuantity}
                                                    onChange={handleQuantityChange}
                                                    onBlur={() => handleQuantityBlur()}
                                                    autoFocus
                                                />
                                            ) : (
                                                <span
                                                    onClick={() => handleQuantityClick(index, bom.componentQuantity.amount.toString())}>{bom.componentQuantity.amount}</span>
                                            )}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-actions"
                                         data-id={bom.componentCode}>
                                        <button className="action-button" onClick={() => handleDelete(bom)}>削除
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
