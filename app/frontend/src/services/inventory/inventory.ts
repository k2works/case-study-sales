import Config from "../config";
import Utils from "../utils";
import {
    InventoryCriteriaType,
    InventoryFetchType,
    InventoryType,
    InventorySearchCriteriaType,
    mapToInventoryCriteriaResource,
    mapToInventoryResource,
    mapToInventorySearchResource
} from "../../models/inventory/inventory.ts";

export interface UploadResultType {
    message: string;
    details: Array<{ [key: string]: string }>;
}

export interface RuleCheckResultType {
    message: string;
    details: Array<{ [key: string]: string }>;
}

export interface InventoryServiceType {
    select: (page?: number, pageSize?: number) => Promise<InventoryFetchType>;
    find: (warehouseCode: string, productCode: string, lotNumber: string, stockCategory: string, qualityCategory: string) => Promise<InventoryType>;
    create: (inventory: InventoryType) => Promise<void>;
    update: (inventory: InventoryType) => Promise<void>;
    destroy: (warehouseCode: string, productCode: string, lotNumber: string, stockCategory: string, qualityCategory: string) => Promise<void>;
    search: (criteria: InventoryCriteriaType, page?: number, pageSize?: number) => Promise<InventoryFetchType>;
    upload: (file: File) => Promise<UploadResultType[]>;
    check: () => Promise<RuleCheckResultType[]>;
}

export const InventoryService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/inventory`;

    const select = async (page?: number, pageSize?: number): Promise<InventoryFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet<InventoryFetchType>(url);
    };

    const find = async (warehouseCode: string, productCode: string, lotNumber: string, stockCategory: string, qualityCategory: string): Promise<InventoryType> => {
        const url = `${endPoint}/${warehouseCode}/${productCode}/${lotNumber}/${stockCategory}/${qualityCategory}`;
        return await apiUtils.fetchGet<InventoryType>(url);
    };

    const create = async (inventory: InventoryType): Promise<void> => {
        await apiUtils.fetchPost<void>(endPoint, mapToInventoryResource(inventory));
    };

    const update = async (inventory: InventoryType): Promise<void> => {
        const url = `${endPoint}/${inventory.warehouseCode}/${inventory.productCode}/${inventory.lotNumber}/${inventory.stockCategory}/${inventory.qualityCategory}`;
        await apiUtils.fetchPut<void>(url, mapToInventoryResource(inventory));
    };

    const search = async (criteria: InventoryCriteriaType, page?: number, pageSize?: number): Promise<InventoryFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<InventoryFetchType>(url, mapToInventoryCriteriaResource(criteria));
    };

    const destroy = async (warehouseCode: string, productCode: string, lotNumber: string, stockCategory: string, qualityCategory: string): Promise<void> => {
        const url = `${endPoint}/${warehouseCode}/${productCode}/${lotNumber}/${stockCategory}/${qualityCategory}`;
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