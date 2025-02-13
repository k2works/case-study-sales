import React from "react";
import { FaTimes } from "react-icons/fa";
import {PageNation, PageNationType} from "../../../application/PageNation.tsx";
import {CustomerType} from "../../../../models/master/partner";

interface CustomerCollectionSelectProps {
    customers: CustomerType[];
    handleSelect: (customer: CustomerType) => void;
    handleClose: () => void;
    pageNation: PageNationType | null;
    fetchCustomers: () => void;
}

export const CustomerCollectionSelectView: React.FC<CustomerCollectionSelectProps> = ({
                                                                                          customers,
                                                                                          handleSelect,
                                                                                          handleClose,
                                                                                          pageNation,
                                                                                          fetchCustomers
                                                                                      }) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <button className="close-modal-button" onClick={handleClose}>
                    <FaTimes aria-hidden="true"/>
                </button>
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">顧客</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {customers.map(customer => (
                                <li className="collection-object-item" key={customer.customerCode.code.value}>
                                    <div className="collection-object-item-content"
                                         data-id={customer.customerCode.code.value}>
                                        <div className="collection-object-item-content-details">顧客コード</div>
                                        <div
                                            className="collection-object-item-content-name">{customer.customerCode.code.value}</div>
                                    </div>
                                    <div className="collection-object-item-content"
                                         data-id={customer.customerCode.code.value}>
                                        <div className="collection-object-item-content-details">顧客コード枝番</div>
                                        <div
                                            className="collection-object-item-content-name">{customer.customerCode.branchNumber}</div>
                                    </div>
                                    <div className="collection-object-item-content"
                                         data-id={customer.customerCode.code.value}>
                                        <div className="collection-object-item-content-details">顧客名</div>
                                        <div
                                            className="collection-object-item-content-name">{customer.customerName.value.name}</div>
                                    </div>
                                    <div className="collection-object-item-actions"
                                         data-id={customer.customerCode.code.value}>
                                        <button className="action-button" onClick={() => handleSelect(customer)}
                                                id="select-customer">選択
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
                <PageNation pageNation={pageNation} callBack={fetchCustomers}/>
            </div>
        </div>
    );
};