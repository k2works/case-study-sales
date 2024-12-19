import Config from "./config";
import Utils from "./utils";
import {AuditType, mapToAuditSearchResource, SearchAuditConditionType} from "../models/audit.ts";

export interface AuditServiceType {
    select: (page?: number, pageSize?: number) => Promise<any>;
    find: (id: number) => Promise<AuditType>;
    destroy: (id: number) => Promise<void>;
    search: (condition: SearchAuditConditionType, page?: number, pageSize?: number) => Promise<any>;
}

export const AuditService = (): AuditServiceType => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/audits`;

    const buildUrlWithPaging = (baseUrl: string, page?: number, pageSize?: number): string => {
        const params = new URLSearchParams();
        if (pageSize) params.append("pageSize", pageSize.toString());
        if (page) params.append("page", page.toString());
        return params.toString() ? `${baseUrl}?${params.toString()}` : baseUrl;
    };

    const select = (page?: number, pageSize?: number): Promise<AuditType[]> => {
        const url = buildUrlWithPaging(endPoint, page, pageSize);
        return apiUtils.fetchGet(url);
    };

    const find = (id: number): Promise<AuditType> => {
        return apiUtils.fetchGet(`${endPoint}/${id}`);
    };

    const destroy = (id: number): Promise<void> => {
        return apiUtils.fetchDelete(`${endPoint}/${id}`);
    };

    const search = (condition: SearchAuditConditionType, page?: number, pageSize?: number): Promise<AuditType[]> => {
        const url = buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return apiUtils.fetchPost(url, mapToAuditSearchResource(condition));
    };

    return {
        select,
        find,
        destroy,
        search,
    };
};