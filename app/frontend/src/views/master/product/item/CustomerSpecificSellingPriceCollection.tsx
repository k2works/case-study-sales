import React, {useState} from "react";
import {CustomerSpecificSellingPriceType} from "../../../../models/master/product";

interface CustomerSpecificSellingPriceCollectionProps {
    prices: CustomerSpecificSellingPriceType[];
    handleAdd: () => void;
    handleDelete: (price: CustomerSpecificSellingPriceType) => void;
}

export const CustomerSpecificSellingPriceCollectionAddListView: React.FC<CustomerSpecificSellingPriceCollectionProps> = ({
                                                                                                                      prices,
                                                                                                                      handleAdd,
                                                                                                                      handleDelete,
                                                                                                                  }) => {
    const [editingPriceIndex, setEditingPriceIndex] = useState<number | null>(null);
    const [currentPrice, setCurrentPrice] = useState<string>("");
    const [editingCustomerCodeIndex, setEditingCustomerCodeIndex] = useState<number | null>(null);
    const [currentCustomerCode, setCurrentCustomerCode] = useState<string>("");

    const handlePriceClick = (index: number, price: string) => {
        setEditingPriceIndex(index);
        setCurrentPrice(price);
    };

    const handleCustomerCodeClick = (index: number, customerCode: string) => {
        setEditingCustomerCodeIndex(index);
        setCurrentCustomerCode(customerCode);
    };

    const handlePriceChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setCurrentPrice(event.target.value);
        prices[editingPriceIndex!].sellingPrice.amount = parseFloat(event.target.value);
    };

    const handleCustomerCodeChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setCurrentCustomerCode(event.target.value);
        prices[editingCustomerCodeIndex!].customerCode = event.target.value;
    };

    const handleBlur = () => {
        setEditingPriceIndex(null);
        setEditingCustomerCodeIndex(null);
    };

    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">顧客別販売単価</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={handleAdd} id="add">追加</button>
                    </div>
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {prices.map((price, index) => (
                                <li className="collection-object-item" key={price.productCode}>
                                    <div className="collection-object-item-content" data-id={price.customerCode}>
                                        <div className="collection-object-item-content-details">顧客コード</div>
                                        <div className="collection-object-item-content-name">
                                            {editingCustomerCodeIndex === index ? (
                                                <input
                                                    type="text"
                                                    value={currentCustomerCode}
                                                    onChange={handleCustomerCodeChange}
                                                    onBlur={handleBlur}
                                                    autoFocus
                                                />
                                            ) : (
                                                <span
                                                    onClick={() => handleCustomerCodeClick(index, price.customerCode)}>
                                                    {price.customerCode}
                                                </span>
                                            )}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={price.productCode}>
                                        <div className="collection-object-item-content-details">販売価格</div>
                                        <div className="collection-object-item-content-name">
                                            {editingPriceIndex === index ? (
                                                <input
                                                    type="text"
                                                    value={currentPrice}
                                                    onChange={handlePriceChange}
                                                    onBlur={handleBlur}
                                                    autoFocus
                                                />
                                            ) : (
                                                <span
                                                    onClick={() => handlePriceClick(index, price.sellingPrice.amount.toString())}>
                                                    {price.sellingPrice.amount}
                                                </span>
                                            )}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-actions" data-id={price.productCode}>
                                        <button className="action-button" onClick={() => handleDelete(price)}>削除
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
