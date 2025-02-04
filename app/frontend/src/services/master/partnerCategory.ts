import Config from "../config.ts";
import Utils from "../utils.ts";
import {
    PartnerCategoryType,
    PartnerCategoryFetchType,
    PartnerCategoryCriteriaType,
    mapToPartnerCategoryTypeResource,
    mapToPartnerCategoryCriteriaResource
} from "../../models/master/partner";

export interface PartnerCategoryServiceType {
    select: (page?: number, pageSize?: number) => Promise<PartnerCategoryFetchType>;
    find: (partnerCategoryId: string) => Promise<PartnerCategoryType>;
    create: (partnerCategory: PartnerCategoryType) => Promise<void>;
    update: (partnerCategory: PartnerCategoryType) => Promise<void>;
    destroy: (partnerCategoryId: string) => Promise<void>;
    search: (criteria: PartnerCategoryCriteriaType, page?: number, pageSize?: number) => Promise<PartnerCategoryFetchType>;
}

export const PartnerCategoryService = (): PartnerCategoryServiceType => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/partner-categories`;

    const select = async (page?: number, pageSize?: number): Promise<PartnerCategoryFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet(url);
    };

    const find = async (partnerCategoryId: string): Promise<PartnerCategoryType> => {
        const url = `${endPoint}/${partnerCategoryId}`;
        return await apiUtils.fetchGet(url);
    };

    const create = async (partnerCategory: PartnerCategoryType): Promise<void> => {
        const resource = mapToPartnerCategoryTypeResource(partnerCategory);
        await apiUtils.fetchPost(endPoint, resource);
    };

    const update = async (partnerCategory: PartnerCategoryType): Promise<void> => {
        const url = `${endPoint}/${partnerCategory.partnerCategoryTypeCode}`;
        const resource = mapToPartnerCategoryTypeResource(partnerCategory);
        await apiUtils.fetchPut(url, resource);
    };

    const destroy = async (partnerCategoryId: string): Promise<void> => {
        const url = `${endPoint}/${partnerCategoryId}`;
        await apiUtils.fetchDelete(url);
    };

    const search = async (criteria: PartnerCategoryCriteriaType, page?: number, pageSize?: number): Promise<PartnerCategoryFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        const criteriaResource = mapToPartnerCategoryCriteriaResource(criteria);
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