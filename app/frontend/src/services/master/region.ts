import Config from "../config.ts";
import Utils from "../utils.ts";
import {
    RegionType,
    RegionFetchType,
    RegionCriteriaType,
    mapToRegionResource,
    mapToRegionCriteria,
} from "../../models/master/code";

export interface RegionServiceType {
    select: (page?: number, pageSize?: number) => Promise<RegionFetchType>;
    find: (regionCode: string) => Promise<RegionType>;
    create: (region: RegionType) => Promise<void>;
    update: (region: RegionType) => Promise<void>;
    destroy: (regionCode: string) => Promise<void>;
    search: (criteria: RegionCriteriaType, page?: number, pageSize?: number) => Promise<RegionFetchType>;
}

export const RegionService = (): RegionServiceType => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/regions`;

    const select = async (page?: number, pageSize?: number): Promise<RegionFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet(url);
    };

    const find = async (regionCode: string): Promise<RegionType> => {
        const url = `${endPoint}/${regionCode}`;
        return await apiUtils.fetchGet(url);
    };

    const create = async (region: RegionType): Promise<void> => {
        await apiUtils.fetchPost(endPoint, mapToRegionResource(region));
    };

    const update = async (region: RegionType): Promise<void> => {
        const url = `${endPoint}/${region.regionCode}`;
        await apiUtils.fetchPut(url, mapToRegionResource(region));
    };

    const destroy = async (regionCode: string): Promise<void> => {
        const url = `${endPoint}/${regionCode}`;
        await apiUtils.fetchDelete(url);
    };

    const search = async (criteria: RegionCriteriaType, page?: number, pageSize?: number): Promise<RegionFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost(url, mapToRegionCriteria(criteria));
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
