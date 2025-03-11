import Config from "../config.ts";
import Utils from "../utils.ts";
import {
    PartnerType,
    PartnerFetchType,
    PartnerCriteriaType,
    PartnerCriteriaResourceType,
    mapToPartnerResource,
    mapToPartnerCriteriaResource,
} from "../../models/master/partner";

export interface PartnerServiceType {
    select: (page?: number, pageSize?: number) => Promise<PartnerFetchType>;
    find: (partnerCode: string) => Promise<PartnerType>;
    create: (partner: PartnerType) => Promise<void>;
    update: (partner: PartnerType) => Promise<void>;
    destroy: (partnerCode: string) => Promise<void>;
    search: (criteria: PartnerCriteriaType, page?: number, pageSize?: number) => Promise<PartnerFetchType>;
}

export const PartnerService = (): PartnerServiceType => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/partners`;

    const select = async (page?: number, pageSize?: number): Promise<PartnerFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet(url);
    };

    const find = async (partnerCode: string): Promise<PartnerType> => {
        const url = `${endPoint}/${partnerCode}`;
        return await apiUtils.fetchGet(url);
    };

    const create = async (partner: PartnerType): Promise<void> => {
        const resource: PartnerType = mapToPartnerResource(partner);
        await apiUtils.fetchPost(endPoint, resource);
    };

    const update = async (partner: PartnerType): Promise<void> => {
        const url = `${endPoint}/${partner.partnerCode}`;
        const resource: PartnerType = mapToPartnerResource(partner);
        await apiUtils.fetchPut(url, resource);
    };

    const destroy = async (partnerCode: string): Promise<void> => {
        const url = `${endPoint}/${partnerCode}`;
        await apiUtils.fetchDelete(url);
    };

    const search = async (
        criteria: PartnerCriteriaType,
        page?: number,
        pageSize?: number
    ): Promise<PartnerFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        const criteriaResource: PartnerCriteriaResourceType = mapToPartnerCriteriaResource(criteria);
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
