import Config from "../config";
import Utils from "../utils";
import {
    InvoiceCriteriaType,
    InvoicePageInfoType,
    InvoiceType,
    mapToInvoiceCriteriaResource,
    mapToInvoiceResource
} from "../../models/sales/invoice";

export interface InvoiceServiceType {
    select: (page?: number, pageSize?: number) => Promise<InvoicePageInfoType>;
    find: (invoiceNumber: string) => Promise<InvoiceType>;
    create: (invoice: InvoiceType) => Promise<void>;
    update: (invoice: InvoiceType) => Promise<void>;
    destroy: (invoiceNumber: string) => Promise<void>;
    search: (criteria: InvoiceCriteriaType, page?: number, pageSize?: number) => Promise<InvoicePageInfoType>;
}

export const InvoiceService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/invoices`;

    const select = async (page?: number, pageSize?: number): Promise<InvoicePageInfoType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet<InvoicePageInfoType>(url);
    };

    const find = async (invoiceNumber: string): Promise<InvoiceType> => {
        const url = `${endPoint}/${invoiceNumber}`;
        return await apiUtils.fetchGet<InvoiceType>(url);
    };

    const create = async (invoice: InvoiceType): Promise<void> => {
        await apiUtils.fetchPost<void>(endPoint, mapToInvoiceResource(invoice));
    };

    const update = async (invoice: InvoiceType): Promise<void> => {
        const url = `${endPoint}/${invoice.invoiceNumber}`;
        await apiUtils.fetchPut<void>(url, mapToInvoiceResource(invoice));
    };

    const search = async (criteria: InvoiceCriteriaType, page?: number, pageSize?: number): Promise<InvoicePageInfoType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<InvoicePageInfoType>(url, mapToInvoiceCriteriaResource(criteria));
    };

    const destroy = async (invoiceNumber: string): Promise<void> => {
        const url = `${endPoint}/${invoiceNumber}`;
        await apiUtils.fetchDelete<void>(url);
    };

    return {
        select,
        find,
        create,
        update,
        destroy,
        search
    };
}