import React, {useState} from "react";
import {SubstituteProductType} from "../../../../models/master/product";

interface SubstituteProductCollectionProps {
    substituteProducts: SubstituteProductType[];
    handleAdd: () => void;
    handleDelete: (product: SubstituteProductType) => void;
}

export const SubstituteProductCollectionAddListView: React.FC<SubstituteProductCollectionProps> = ({
                                                                                                substituteProducts,
                                                                                                handleAdd,
                                                                                                handleDelete,
                                                                                            }) => {
    const [editingPriority, setEditingPriority] = useState<number | null>(null);
    const [currentPriority, setCurrentPriority] = useState<string>("");

    const handlePriorityClick = (index: number, priority: string) => {
        setEditingPriority(index);
        setCurrentPriority(priority);
    };

    const handlePriorityChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setCurrentPriority(event.target.value);
        substituteProducts[editingPriority!].priority = parseInt(event.target.value);
    };

    const handlePriorityBlur = () => {
        setEditingPriority(null);
    };

    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">代替商品</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={handleAdd} id="add">追加</button>
                    </div>
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {substituteProducts.map((product, index) => (
                                <li className="collection-object-item" key={product.substituteProductCode.value}>
                                    <div className="collection-object-item-content"
                                         data-id={product.substituteProductCode.value}>
                                        <div className="collection-object-item-content-details">代替商品コード</div>
                                        <div
                                            className="collection-object-item-content-name">{product.substituteProductCode.value}</div>
                                    </div>
                                    <div className="collection-object-item-content"
                                         data-id={product.substituteProductCode.value}>
                                        <div className="collection-object-item-content-details">優先度</div>
                                        <div className="collection-object-item-content-name">
                                            {editingPriority === index ? (
                                                <input
                                                    type="text"
                                                    value={currentPriority}
                                                    onChange={handlePriorityChange}
                                                    onBlur={() => handlePriorityBlur()}
                                                    autoFocus
                                                />
                                            ) : (
                                                <span
                                                    onClick={() => handlePriorityClick(index, product.priority.toString())}>{product.priority}</span>
                                            )}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-actions"
                                         data-id={product.substituteProductCode.value}>
                                        <button className="action-button" onClick={() => handleDelete(product)}>削除
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
