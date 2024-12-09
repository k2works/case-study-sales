import Config from "./config";
import Utils from "./utils";

export interface AuditServiceType {
    select: (page?: number, pageSize?: number) => Promise<any>;
    find: (id: number) => Promise<AuditServiceType>;
    destroy: (id: number) => Promise<void>;
    search: (pageSize: number, code: string, page: number) => Promise<AuditServiceType>;
}

export const AuditService = (): AuditServiceType => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/audits`;

    const select = async (page?: number, pageSize?: number): Promise<AuditServiceType[]> => {
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

    const find = async (id: number): Promise<AuditServiceType> => {
        const url = `${endPoint}/${id}`;
        return await apiUtils.fetchGet(url);
    };

    const destroy = async (id: number): Promise<void> => {
        const url = `${endPoint}/${id}`;
        return await apiUtils.fetchDelete(url);
    };

    const search = async (pageSize: number, code: string, page: number): Promise<AuditServiceType> => {
        const url = `${endPoint}/search?pageSize=${pageSize}&code=${code}&page=${page}`;

        return await apiUtils.fetchGet(url);
    };

    return {
        select,
        find,
        destroy,
        search,
    };
};