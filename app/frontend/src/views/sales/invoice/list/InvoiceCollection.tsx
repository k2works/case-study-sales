import React from "react";
import { InvoiceType, InvoiceCriteriaType } from "../../../../models/sales/invoice";
import { PageNation, PageNationType } from "../../../application/PageNation";
import { Message } from "../../../../components/application/Message.tsx";
import {Search} from "../../../Common.tsx";

type SearchItemsProps = {
    searchInvoiceCriteria: InvoiceCriteriaType;
    setSearchInvoiceCriteria: React.Dispatch<React.SetStateAction<InvoiceCriteriaType>>;
    handleOpenSearchModal: () => void;
};

type HeaderItemsProps = {
    handleOpenModal: (invoice?: InvoiceType) => void;
    handleCheckToggleCollection: () => void;
    handleDeleteCheckedCollection: () => void;
};

type CollectionItemsProps = {
    invoices: InvoiceType[];
    handleOpenModal: (invoice: InvoiceType) => void;
    handleDeleteInvoice: (invoiceNumber: string) => void;
    handleCheckInvoice: (invoice: InvoiceType) => void;
};

type PageNationItemsProps = {
    pageNation: PageNationType | null;
    fetchInvoices: (page?: number, criteria?: InvoiceCriteriaType) => Promise<void>;
    criteria: InvoiceCriteriaType | null;
};

type Props = {
    error: string | null;
    message: string | null;
    searchItems: SearchItemsProps;
    headerItems: HeaderItemsProps;
    collectionItems: CollectionItemsProps;
    pageNationItems: PageNationItemsProps;
};

export const InvoiceCollectionView: React.FC<Props> = ({
    error,
    message,
    searchItems,
    headerItems,
    collectionItems,
    pageNationItems
}) => {
    const { handleOpenSearchModal } = searchItems;
    const { handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection } = headerItems;
    const { invoices, handleDeleteInvoice, handleCheckInvoice } = collectionItems;
    const { pageNation, fetchInvoices, criteria } = pageNationItems;

    return (
        <div className="collection-view-object-container">
            <Message error={error} message={message}/>
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h1 className="single-view-title">請求一覧</h1>
                    </div>
                </div>
                <div className="collection-view-content">
                    <Search
                        searchCriteria={searchItems.searchInvoiceCriteria}
                        setSearchCriteria={searchItems.setSearchInvoiceCriteria}
                        handleSearchAudit={handleOpenSearchModal}
                    />

                    <div className="button-container">
                        <button className="action-button" onClick={() => handleOpenModal()} id="new">
                            新規登録
                        </button>
                        <button className="action-button" onClick={handleCheckToggleCollection} id="checkAll">
                            一括選択
                        </button>
                        <button className="action-button" onClick={handleDeleteCheckedCollection} id="deleteAll">
                            一括削除
                        </button>
                    </div>
                    <div className="collection-object-container">
                        <ul className="collection-object-list">
                            {invoices.map((invoice) => (
                                <li className="collection-object-item" key={invoice.invoiceNumber}>
                                    <div className="collection-object-item-content" data-id={invoice.invoiceNumber}>
                                        <input type="checkbox" className="collection-object-item-checkbox" 
                                            checked={invoice.checked || false}
                                            onChange={() => handleCheckInvoice(invoice)}/>
                                    </div>
                                    <div className="collection-object-item-content" data-id={invoice.invoiceNumber}>
                                        <div className="collection-object-item-content-details">請求番号</div>
                                        <div className="collection-object-item-content-name">{invoice.invoiceNumber}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={invoice.invoiceNumber}>
                                        <div className="collection-object-item-content-details">請求日</div>
                                        <div className="collection-object-item-content-name">{invoice.invoiceDate}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={invoice.invoiceNumber}>
                                        <div className="collection-object-item-content-details">顧客コード</div>
                                        <div className="collection-object-item-content-name">{invoice.customerCode}</div>
                                    </div>
                                    <div className="collection-object-item-content" data-id={invoice.invoiceNumber}>
                                        <div className="collection-object-item-content-details">当月請求額</div>
                                        <div className="collection-object-item-content-name">{invoice.currentMonthInvoiceAmount}</div>
                                    </div>
                                    <div className="collection-object-item-actions" data-id={invoice.invoiceNumber}>
                                        <button className="action-button" onClick={() => handleOpenModal(invoice)} id="edit">編集</button>
                                    </div>
                                    <div className="collection-object-item-actions" data-id={invoice.invoiceNumber}>
                                        <button className="action-button" onClick={() => handleDeleteInvoice(invoice.invoiceNumber)} id="delete">削除</button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                    {pageNation && (
                        <PageNation
                            pageNation={pageNation}
                            callBack={(page) => fetchInvoices(page, criteria || undefined)}
                        />
                    )}
                </div>
            </div>
        </div>
    );
};
