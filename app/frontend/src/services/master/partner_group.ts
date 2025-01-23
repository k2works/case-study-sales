import Config from "../config.ts";
import Utils from "../utils.ts";
import {
    PartnerGroupType,
    PartnerGroupFetchType,
    PartnerGroupCriteriaType,
    mapToPartnerGroupResource,
    mapToPartnerGroupCriteriaResource,
} from "../../models";

export interface PartnerGroupServiceType {
    select: (page?: number, pageSize?: number) => Promise<PartnerGroupFetchType>;
    find: (partnerGroupCode: string) => Promise<PartnerGroupType>;
    create: (partnerGroup: PartnerGroupType) => Promise<void>;
    update: (partnerGroup: PartnerGroupType) => Promise<void>;
    destroy: (partnerGroupCode: string) => Promise<void>;
    search: (criteria: PartnerGroupCriteriaType, page?: number, pageSize?: number) => Promise<PartnerGroupFetchType>;
}

export const PartnerGroupService = (): PartnerGroupServiceType => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/partner-groups`;

    const select = async (page?: number, pageSize?: number): Promise<PartnerGroupFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet(url);
    };

    const find = async (partnerGroupCode: string): Promise<PartnerGroupType> => {
        const url = `${endPoint}/${partnerGroupCode}`;
        return await apiUtils.fetchGet(url);
    };

    const create = async (partnerGroup: PartnerGroupType): Promise<void> => {
        const resource = mapToPartnerGroupResource(partnerGroup);
        await apiUtils.fetchPost(endPoint, resource);
    };

    const update = async (partnerGroup: PartnerGroupType): Promise<void> => {
        const url = `${endPoint}/${partnerGroup.partnerGroupCode.value}`;
        const resource = mapToPartnerGroupResource(partnerGroup);
        await apiUtils.fetchPut(url, resource);
    };

    const destroy = async (partnerGroupCode: string): Promise<void> => {
        const url = `${endPoint}/${partnerGroupCode}`;
        await apiUtils.fetchDelete(url);
    };

    const search = async (criteria: PartnerGroupCriteriaType, page?: number, pageSize?: number): Promise<PartnerGroupFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        const criteriaResource = mapToPartnerGroupCriteriaResource(criteria);
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