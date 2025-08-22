import Config from "../config";
import Utils from "../utils";
import {
    PurchaseOrderCriteriaType,
    PurchaseOrderFetchType,
    PurchaseOrderType,
    PurchaseOrderSearchCriteriaType,
    mapToPurchaseOrderCriteriaResource,
    mapToPurchaseOrderResource,
    mapToPurchaseOrderSearchResource
} from "../../models/sales/purchaseOrder.ts";

export interface UploadResultType {
    message: string;
    details: Array<{ [key: string]: string }>;
}

export interface RuleCheckResultType {
    message: string;
    details: Array<{ [key: string]: string }>;
}

export interface PurchaseOrderServiceType {
    select: (page?: number, pageSize?: number) => Promise<PurchaseOrderFetchType>;
    find: (purchaseOrderNumber: string) => Promise<PurchaseOrderType>;
    create: (purchaseOrder: PurchaseOrderType) => Promise<void>;
    update: (purchaseOrder: PurchaseOrderType) => Promise<void>;
    destroy: (purchaseOrderNumber: string) => Promise<void>;
    search: (criteria: PurchaseOrderCriteriaType, page?: number, pageSize?: number) => Promise<PurchaseOrderFetchType>;
    upload: (file: File) => Promise<UploadResultType[]>;
    check: () => Promise<RuleCheckResultType[]>;
}

export const PurchaseOrderService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/purchase-orders`;

    const select = async (page?: number, pageSize?: number): Promise<PurchaseOrderFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet<PurchaseOrderFetchType>(url);
    };

    const find = async (purchaseOrderNumber: string): Promise<PurchaseOrderType> => {
        const url = `${endPoint}/${purchaseOrderNumber}`;
        return await apiUtils.fetchGet<PurchaseOrderType>(url);
    };

    const create = async (purchaseOrder: PurchaseOrderType): Promise<void> => {
        await apiUtils.fetchPost<void>(endPoint, mapToPurchaseOrderResource(purchaseOrder));
    };

    const update = async (purchaseOrder: PurchaseOrderType): Promise<void> => {
        const url = `${endPoint}/${purchaseOrder.purchaseOrderNumber}`;
        await apiUtils.fetchPut<void>(url, mapToPurchaseOrderResource(purchaseOrder));
    };

    const search = async (criteria: PurchaseOrderCriteriaType, page?: number, pageSize?: number): Promise<PurchaseOrderFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<PurchaseOrderFetchType>(url, mapToPurchaseOrderCriteriaResource(criteria));
    };

    const destroy = async (purchaseOrderNumber: string): Promise<void> => {
        const url = `${endPoint}/${purchaseOrderNumber}`;
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
};