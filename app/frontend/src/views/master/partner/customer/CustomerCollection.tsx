import React from "react";
import { CustomerType, CustomerCriteriaType } from "../../../../models/master/partner";
import { PageNation, PageNationType } from "../../../application/PageNation.tsx";
import { Message } from "../../../../components/application/Message.tsx";
import { Search } from "../../../Common.tsx";

interface CustomerItemProps {
    customer: CustomerType;
    onEdit: (customer: CustomerType) => void;
    onDelete: (customerCode: string) => void;
    onCheck: (customer: CustomerType) => void;
}

const CustomerItem: React.FC<CustomerItemProps> = ({
                                                       customer,
                                                       onEdit,
                                                       onDelete,
                                                       onCheck,
                                                   }) => (
    <li className="collection-object-item" key={customer.customerCode.code.value}>
        <div className="collection-object-item-content" data-id={customer.customerCode.code.value}>
            <input
                type="checkbox"
                className="collection-object-item-checkbox"
                checked={customer.checked || false}
                onChange={() => onCheck(customer)}
            />
        </div>
        <div className="collection-object-item-content" data-id={customer.customerCode.code.value}>
            <div className="collection-object-item-content-details">顧客コード</div>
            <div className="collection-object-item-content-name">{customer.customerCode.code.value}</div>
        </div>
        <div className="collection-object-item-content" data-id={customer.customerCode.branchNumber}>
            <div className="collection-object-item-content-details">顧客コード枝番</div>
            <div className="collection-object-item-content-name">{customer.customerCode.branchNumber}</div>
        </div>
        <div className="collection-object-item-content" data-id={customer.customerCode.code.value}>
            <div className="collection-object-item-content-details">顧客名</div>
            <div className="collection-object-item-content-name">{customer.customerName.value.name}</div>
        </div>
        <div className="collection-object-item-actions" data-id={customer.customerCode.code.value}>
            <button className="action-button" onClick={() => onEdit(customer)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={customer.customerCode.code.value}>
            <button className="action-button" onClick={() => onDelete(customer.customerCode.code.value)} id="delete">削除</button>
        </div>
    </li>
);

interface CustomerListProps {
    customers: CustomerType[];
    onEdit: (customer: CustomerType) => void;
    onDelete: (customerCode: string) => void;
    onCheck: (customer: CustomerType) => void;
}

const CustomerList: React.FC<CustomerListProps> = ({
                                                       customers,
                                                       onEdit,
                                                       onDelete,
                                                       onCheck,
                                                   }) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {customers.map((customer) => (
                <CustomerItem
                    key={customer.customerCode.code.value}
                    customer={customer}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface CustomerCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchCustomerCriteria: CustomerCriteriaType;
        setSearchCustomerCriteria: (value: CustomerCriteriaType) => void;
        handleOpenSearchModal: () => void;
    };
    headerItems: {
        handleOpenModal: (customer?: CustomerType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    };
    collectionItems: {
        customers: CustomerType[];
        handleOpenModal: (customer?: CustomerType) => void;
        handleDeleteCustomer: (customerCode: string) => void;
        handleCheckCustomer: (customer: CustomerType) => void;
    };
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: CustomerCriteriaType | null;
        fetchCustomers: () => void;
    };
}

/**
 * CustomerCollectionView コンポーネント
 */
export const CustomerCollectionView: React.FC<CustomerCollectionViewProps> = ({
                                                                                  error,
                                                                                  message,
                                                                                  searchItems: {
                                                                                      searchCustomerCriteria,
                                                                                      setSearchCustomerCriteria,
                                                                                      handleOpenSearchModal,
                                                                                  },
                                                                                  headerItems: {
                                                                                      handleOpenModal,
                                                                                      handleCheckToggleCollection,
                                                                                      handleDeleteCheckedCollection,
                                                                                  },
                                                                                  collectionItems: {
                                                                                      customers,
                                                                                      handleOpenModal: handleEditCustomer,
                                                                                      handleDeleteCustomer,
                                                                                      handleCheckCustomer,
                                                                                  },
                                                                                  pageNationItems: {
                                                                                      pageNation,
                                                                                      criteria,
                                                                                      fetchCustomers,
                                                                                  },
                                                                              }) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message} />
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">顧客</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchCustomerCriteria}
                    setSearchCriteria={setSearchCustomerCriteria}
                    handleSearchAudit={handleOpenSearchModal}
                />
                <div className="button-container">
                    <button className="action-button" onClick={() => handleOpenModal()} id="new">
                        新規
                    </button>
                    <button className="action-button" onClick={() => handleCheckToggleCollection()} id="checkAll">
                        一括選択
                    </button>
                    <button className="action-button" onClick={() => handleDeleteCheckedCollection()} id="deleteAll">
                        一括削除
                    </button>
                </div>
                <CustomerList
                    customers={customers}
                    onEdit={handleEditCustomer}
                    onDelete={handleDeleteCustomer}
                    onCheck={handleCheckCustomer}
                />
                <PageNation
                    pageNation={pageNation}
                    callBack={fetchCustomers}
                    criteria={criteria}
                />
            </div>
        </div>
    </div>
);