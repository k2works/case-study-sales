import Config from "../config";
import Utils from "../utils";
import {
    PurchaseCriteriaType,
    PurchaseFetchType,
    PurchaseType,
    mapToPurchaseCriteriaResource,
    mapToPurchaseResource,
} from "../../models/procurement/purchase.ts";

export interface RuleCheckResultType {
    message: string;
    details: Array<{ [key: string]: string }>;
}

export interface PurchaseServiceType {
    select: (page?: number, pageSize?: number) => Promise<PurchaseFetchType>;
    find: (purchaseNumber: string) => Promise<PurchaseType>;
    create: (purchase: PurchaseType) => Promise<void>;
    update: (purchase: PurchaseType) => Promise<void>;
    destroy: (purchaseNumber: string) => Promise<void>;
    search: (criteria: PurchaseCriteriaType, page?: number, pageSize?: number) => Promise<PurchaseFetchType>;
    check: () => Promise<RuleCheckResultType[]>;
}

export const PurchaseService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/purchases`;

    const select = async (page?: number, pageSize?: number): Promise<PurchaseFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet<PurchaseFetchType>(url);
    };

    const find = async (purchaseNumber: string): Promise<PurchaseType> => {
        const url = `${endPoint}/${purchaseNumber}`;
        return await apiUtils.fetchGet<PurchaseType>(url);
    };

    const create = async (purchase: PurchaseType): Promise<void> => {
        await apiUtils.fetchPost<void>(endPoint, mapToPurchaseResource(purchase));
    };

    const update = async (purchase: PurchaseType): Promise<void> => {
        const url = `${endPoint}/${purchase.purchaseNumber}`;
        await apiUtils.fetchPut<void>(url, mapToPurchaseResource(purchase));
    };

    const search = async (criteria: PurchaseCriteriaType, page?: number, pageSize?: number): Promise<PurchaseFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<PurchaseFetchType>(url, mapToPurchaseCriteriaResource(criteria));
    };

    const destroy = async (purchaseNumber: string): Promise<void> => {
        const url = `${endPoint}/${purchaseNumber}`;
        await apiUtils.fetchDelete<void>(url);
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
        check
    };
};
