import Config from "../config.ts";
import Utils from "../utils.ts";
import {
    VendorType,
    VendorFetchType,
    VendorCriteriaType,
    VendorResourceType,
    VendorCriteriaResourceType,
    mapToVendorResourceType,
    mapToCriteriaResourceType, VendorCodeType,
} from "../../models/master/partner";

export interface VendorServiceType {
    select: (page?: number, pageSize?: number) => Promise<VendorFetchType>;
    find: (vendorCode: VendorCodeType) => Promise<VendorType>;
    create: (vendor: VendorType) => Promise<void>;
    update: (vendor: VendorType) => Promise<void>;
    destroy: (vendorCode: VendorCodeType) => Promise<void>;
    search: (criteria: VendorCriteriaType, page?: number, pageSize?: number) => Promise<VendorFetchType>;
}

export const VendorService = (): VendorServiceType => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/vendors`;

    const select = async (page?: number, pageSize?: number): Promise<VendorFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet(url);
    };

    const find = async (vendorCode: VendorCodeType): Promise<VendorType> => {
        const url = `${endPoint}/${vendorCode.code.value}/${vendorCode.branchNumber}`;
        return await apiUtils.fetchGet(url);
    };

    const create = async (vendor: VendorType): Promise<void> => {
        const resource: VendorResourceType = mapToVendorResourceType(vendor);
        await apiUtils.fetchPost(endPoint, resource);
    };

    const update = async (vendor: VendorType): Promise<void> => {
        const url = `${endPoint}/${vendor.vendorCode.code.value}`;
        const resource: VendorResourceType = mapToVendorResourceType(vendor);
        await apiUtils.fetchPut(url, resource);
    };

    const destroy = async (vendorCode: VendorCodeType): Promise<void> => {
        const url = `${endPoint}/${vendorCode.code.value}/${vendorCode.branchNumber}`;
        await apiUtils.fetchDelete(url);
    };

    const search = async (
        criteria: VendorCriteriaType,
        page?: number,
        pageSize?: number
    ): Promise<VendorFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        const criteriaResource: VendorCriteriaResourceType = mapToCriteriaResourceType(criteria);
        return await apiUtils.fetchPost(url, criteriaResource);
    };

    return {
        select,
        find,
        create,
        update,
        destroy,
        search,
    };
};