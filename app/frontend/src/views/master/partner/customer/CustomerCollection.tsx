import React, {useState} from "react";
import {CustomerCodeType, CustomerCriteriaType, CustomerType, ShippingType} from "../../../../models/master/partner";
import {PageNation, PageNationType} from "../../../application/PageNation.tsx";
import {Message} from "../../../../components/application/Message.tsx";
import {Search} from "../../../Common.tsx";

interface CustomerItemProps {
    customer: CustomerType;
    onEdit: (customer: CustomerType) => void;
    onDelete: (customerCode: CustomerCodeType) => void;
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
            <button className="action-button" onClick={() => onDelete(customer.customerCode)} id="delete">削除</button>
        </div>
    </li>
);

interface CustomerListProps {
    customers: CustomerType[];
    onEdit: (customer: CustomerType) => void;
    onDelete: (customerCode: CustomerCodeType) => void;
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
        handleDeleteCustomer: (customerCode: CustomerCodeType) => void;
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

interface CustomerShippingCollectionAddListViewProps {
    setNewShipping: any;
    shippings: ShippingType[];
    handleAddShipping: () => void;
    handleDeleteShipping: (shipping: ShippingType) => void;
    handleAddRegion: () => void;
}

export const CustomerShippingCollectionAddListView: React.FC<CustomerShippingCollectionAddListViewProps> = ({
                                                                                                                setNewShipping,
                                                                                                                shippings,
                                                                                                                handleAddShipping,
                                                                                                                handleDeleteShipping,
                                                                                                                handleAddRegion,
                                                                                                            }) => {
    const [editingFieldIndex, setEditingFieldIndex] = useState<{
        index: number | null;
        field: "destinationName" | "regionCode" | "postalCode" | "address1" | "address2" | null;
    }>({ index: null, field: null });
    const [currentValue, setCurrentValue] = useState<string>("");

    const handleRegionClick = (index: number) => {
        const updatedShipping = { ...shippings[index] };
        setNewShipping({
            ...updatedShipping
        })
        handleAddRegion();
    }

    const handleFieldClick = (index: number, field: "destinationName" | "regionCode" | "postalCode" | "address1" | "address2", value: string) => {
        setEditingFieldIndex({ index, field });
        setCurrentValue(value);
    };

    const handleFieldChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setCurrentValue(event.target.value);
    };

    const handleFieldBlur = () => {
        if (editingFieldIndex.index !== null && editingFieldIndex.field) {
            const index = editingFieldIndex.index;
            const field = editingFieldIndex.field;

            const updatedShipping = { ...shippings[index] };

            switch (field) {
                case "destinationName":
                    updatedShipping.destinationName = currentValue;
                    break;
                case "regionCode":
                    updatedShipping.regionCode.value = currentValue;
                    break;
                case "postalCode":
                    updatedShipping.shippingAddress.postalCode.value = currentValue;
                    break;
                case "address1":
                    updatedShipping.shippingAddress.address1 = currentValue;
                    break;
                case "address2":
                    updatedShipping.shippingAddress.address2 = currentValue;
                    break;
            }

            shippings[index] = updatedShipping;
        }
        setEditingFieldIndex({ index: null, field: null });
        setCurrentValue("");
    };

    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">出荷先</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={handleAddShipping} id="add-shipping">
                            出荷先追加
                        </button>
                    </div>
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {shippings.map((shipping, index) => (
                                <li className="collection-object-item" key={shipping.shippingCode.destinationNumber}>
                                    <div className="collection-object-item-content" data-id={shipping.shippingCode.customerCode.code}>
                                        <div className="collection-object-item-content-details">出荷先番号</div>
                                        <div className="collection-object-item-content-name">
                                            {shipping.shippingCode.destinationNumber}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={shipping.shippingCode.customerCode.code}>
                                        <div className="collection-object-item-content-details">出荷先名</div>
                                        <div className="collection-object-item-content-name">
                                            {editingFieldIndex.index === index && editingFieldIndex.field === "destinationName" ? (
                                                <input
                                                    type="text"
                                                    value={currentValue}
                                                    onChange={handleFieldChange}
                                                    onBlur={handleFieldBlur}
                                                    autoFocus
                                                />
                                            ) : (
                                                <span onClick={() => handleFieldClick(index, "destinationName", shipping.destinationName)}>
                                                    {shipping.destinationName}
                                                </span>
                                            )}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={shipping.shippingCode.customerCode.code}>
                                        <div className="collection-object-item-content-details">地域コード</div>
                                        <div className="collection-object-item-content-name">
                                            {editingFieldIndex.index === index && editingFieldIndex.field === "regionCode" ? (
                                                <input
                                                    type="text"
                                                    value={currentValue}
                                                    onChange={handleFieldChange}
                                                    onBlur={handleFieldBlur}
                                                    autoFocus
                                                />
                                            ) : (
                                                <span onClick={() => handleRegionClick(index)}>
                                                    {shipping.regionCode.value}
                                                </span>
                                            )}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={shipping.shippingCode.customerCode.code}>
                                        <div className="collection-object-item-content-details">郵便番号</div>
                                        <div className="collection-object-item-content-name">
                                            {editingFieldIndex.index === index && editingFieldIndex.field === "postalCode" ? (
                                                <input
                                                    type="text"
                                                    value={currentValue}
                                                    onChange={handleFieldChange}
                                                    onBlur={handleFieldBlur}
                                                    autoFocus
                                                />
                                            ) : (
                                                <span onClick={() => handleFieldClick(index, "postalCode", shipping.shippingAddress.postalCode.value)}>
                                                    {shipping.shippingAddress.postalCode.value}
                                                </span>
                                            )}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={shipping.shippingCode.customerCode.code}>
                                        <div className="collection-object-item-content-details">出荷先住所１</div>
                                        <div className="collection-object-item-content-name">
                                            {editingFieldIndex.index === index && editingFieldIndex.field === "address1" ? (
                                                <input
                                                    type="text"
                                                    value={currentValue}
                                                    onChange={handleFieldChange}
                                                    onBlur={handleFieldBlur}
                                                    autoFocus
                                                />
                                            ) : (
                                                <span onClick={() => handleFieldClick(index, "address1", shipping.shippingAddress.address1)}>
                                                    {shipping.shippingAddress.address1}
                                                </span>
                                            )}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={shipping.shippingCode.customerCode.code}>
                                        <div className="collection-object-item-content-details">出荷先住所２</div>
                                        <div className="collection-object-item-content-name">
                                            {editingFieldIndex.index === index && editingFieldIndex.field === "address2" ? (
                                                <input
                                                    type="text"
                                                    value={currentValue}
                                                    onChange={handleFieldChange}
                                                    onBlur={handleFieldBlur}
                                                    autoFocus
                                                />
                                            ) : (
                                                <span onClick={() => handleFieldClick(index, "address2", shipping.shippingAddress.address2)}>
                                                    {shipping.shippingAddress.address2}
                                                </span>
                                            )}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-actions" data-id={shipping.shippingCode.customerCode.code}>
                                        <button className="action-button" onClick={() => handleDeleteShipping(shipping)}>
                                            削除
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