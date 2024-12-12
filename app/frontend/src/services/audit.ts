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

    const select = async (page?: number, pageSize?: number): Promise<AuditType[]> => {
        let url = endPoint;
        if (pageSize && page) {
            url = `${url}?pageSize=${pageSize}&page=${page}`;
        } else if (pageSize) {
            url = `${url}?pageSize=${pageSize}`;
        } else if (page) {
            url = `${url}?page=${page}`;
        }

        return await apiUtils.fetchGet(url);
    };

    const find = async (id: number): Promise<AuditType> => {
        const url = `${endPoint}/${id}`;
        return await apiUtils.fetchGet(url);
    };

    const destroy = async (id: number): Promise<void> => {
        const url = `${endPoint}/${id}`;
        return await apiUtils.fetchDelete(url);
    };

    const search = async (condition: SearchAuditConditionType, page?: number, pageSize?: number): Promise<AuditType[]> => {
        let url = `${endPoint}/search`;
        if (pageSize && page) {
            url = `${url}?pageSize=${pageSize}&page=${page}`;
        } else if (pageSize) {
            url = `${url}?pageSize=${pageSize}`;
        } else if (page) {
            url = `${url}?page=${page}`;
        }

        return await apiUtils.fetchPost(url, mapToAuditSearchResource(condition));
    };

    return {
        select,
        find,
        destroy,
        search,
    };
};