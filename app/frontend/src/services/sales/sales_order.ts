import Config from "../config";
import Utils from "../utils";
import {
    SalesOrderCriteriaType,
    SalesOrderFetchType,
    SalesOrderType,
    mapToSalesOrderCriteriaResource,
    mapToSalesOrderResource
} from "../../models/sales/sales_order";

export interface UploadResultType {
    message: string;
    details: Array<{ [key: string]: string }>;
}

export interface RuleCheckResultType {
    message: string;
    details: Array<{ [key: string]: string }>;
}

export interface SalesOrderServiceType {
    select: (page?: number, pageSize?: number) => Promise<SalesOrderFetchType>;
    find: (orderNumber: string) => Promise<SalesOrderType>;
    create: (salesOrder: SalesOrderType) => Promise<void>;
    update: (salesOrder: SalesOrderType) => Promise<void>;
    destroy: (orderNumber: string) => Promise<void>;
    search: (criteria: SalesOrderCriteriaType, page?: number, pageSize?: number) => Promise<SalesOrderFetchType>;
    upload: (file: File) => Promise<UploadResultType[]>;
    check: () => Promise<RuleCheckResultType[]>;
}

export const SalesOrderService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/sales-orders`;

    const select = async (page?: number, pageSize?: number): Promise<SalesOrderFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet<SalesOrderFetchType>(url);
    };

    const find = async (orderNumber: string): Promise<SalesOrderType> => {
        const url = `${endPoint}/${orderNumber}`;
        return await apiUtils.fetchGet<SalesOrderType>(url);
    };

    const create = async (salesOrder: SalesOrderType): Promise<void> => {
        await apiUtils.fetchPost<void>(endPoint, mapToSalesOrderResource(salesOrder));
    };

    const update = async (salesOrder: SalesOrderType): Promise<void> => {
        const url = `${endPoint}/${salesOrder.orderNumber}`;
        await apiUtils.fetchPut<void>(url, mapToSalesOrderResource(salesOrder));
    };

    const search = async (criteria: SalesOrderCriteriaType, page?: number, pageSize?: number): Promise<SalesOrderFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<SalesOrderFetchType>(url, mapToSalesOrderCriteriaResource(criteria));
    };

    const destroy = async (orderNumber: string): Promise<void> => {
        const url = `${endPoint}/${orderNumber}`;
        await apiUtils.fetchDelete<void>(url);
    };

    const upload = async (file: File): Promise<UploadResultType[]> => {
        const formData = new FormData();
        formData.append('file', file);
        const url = `${endPoint}/upload`;
        const response = await apiUtils.fetchPostFormData<UploadResultType>(url, formData);
        return Array.isArray(response) ? response : [response];
    };

    const check = async (): Promise<RuleCheckResultType[]> => {
        const url = `${endPoint}/check`;
        const response = await apiUtils.fetchPost<RuleCheckResultType>(url, {});
        return Array.isArray(response) ? response : [response];
    };

    return {
        select,
        find,
        create,
        update,
        destroy,
        search,
        upload,
        check
    };
}
