import Config from "./config";
import Utils from "./utils";
import {AuditType, searchAuditCondition} from "../models/audit.ts";

export interface AuditServiceType {
    select: (page?: number, pageSize?: number) => Promise<any>;
    find: (id: number) => Promise<AuditType>;
    destroy: (id: number) => Promise<void>;
    search: (condition: searchAuditCondition) => Promise<AuditType[]>;
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

    const search = async (condition: searchAuditCondition): Promise<AuditType[]> => {
        const url = `${endPoint}/search`;
        const mapToAuditResource = (condition: searchAuditCondition) => {
            return {
                processFlag: condition.processFlag,
                process: {
                    processType: condition.processType,
                },
                type: condition.type,
            };
        };
        return await apiUtils.fetchPost(url, mapToAuditResource(condition));
    };

    return {
        select,
        find,
        destroy,
        search,
    };
};