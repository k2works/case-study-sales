import Config from "../config.ts";
import Utils from "../utils.ts";
import {AuditFetchType, AuditType, mapToCriteriaResource, AuditCriteriaType} from "../../models/system/audit.ts";

export interface AuditServiceType {
    select: (page?: number, pageSize?: number) => Promise<AuditFetchType>;
    find: (id: number) => Promise<AuditType>;
    destroy: (id: number) => Promise<void>;
    search: (criteria: AuditCriteriaType, page?: number, pageSize?: number) => Promise<AuditFetchType>;
}

export const AuditService = (): AuditServiceType => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/audits`;

    const select = (page?: number, pageSize?: number): Promise<AuditFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return apiUtils.fetchGet(url);
    };

    const find = (id: number): Promise<AuditType> => {
        return apiUtils.fetchGet(`${endPoint}/${id}`);
    };

    const destroy = (id: number): Promise<void> => {
        return apiUtils.fetchDelete(`${endPoint}/${id}`);
    };

    const search = (criteria: AuditCriteriaType, page?: number, pageSize?: number): Promise<AuditFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return apiUtils.fetchPost(url, mapToCriteriaResource(criteria));
    };

    return {
        select,
        find,
        destroy,
        search,
    };
};