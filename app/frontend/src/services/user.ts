import Config from "./config.ts";
import Utils from "./utils.ts";
import {mapToUserAccountResource, UserAccountType} from "../models";

export interface UserServiceType {
    select: (page?: number, pageSize?: number) => Promise<any>;
    find: (userId: String) => Promise<any>;
    create: (user: UserAccountType) => Promise<any>;
    update: (user: UserAccountType) => Promise<any>;
    destroy: (userId: String) => Promise<any>;
    search: (pageSize: number, code: string, page: number) => Promise<any>;
}
export const UserService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/users`;

    const select = async (page?: number, pageSize?: number) => {
        let url = endPoint;
        if (pageSize && page) {
            url = url + "?pageSize=" + pageSize + "&page=" + page;
        } else if (pageSize) {
            url = url + "?pageSize=" + pageSize;
        } else if (page) {
            url = url + "?page=" + page;
        }

        return await apiUtils.fetchGet(url);
    };

    const find = async (userId: String) => {
        const url = `${endPoint}/${userId}`;
        return await apiUtils.fetchGet(url);
    };

    const create = async (user: UserAccountType) => {
        return await apiUtils.fetchPost(endPoint, mapToUserAccountResource(user));
    };

    const update = async (user: UserAccountType) => {
        const url = `${endPoint}/${user.userId.value}`;
        return await apiUtils.fetchPut(url, mapToUserAccountResource(user));
    };

    const search = async (pageSize = 10, code: string, page = 1): Promise<any> => {
        let url = `${endPoint}/search?pageSize=${pageSize}&code=${code}&page=${page}`;

        return await apiUtils.fetchGet(url);
    };

    const destroy = async (userId: String) => {
        const url = `${endPoint}/${userId}`;
        return await apiUtils.fetchDelete(url);
    };

    return {
        select,
        find,
        create,
        update,
        destroy,
        search
    };
}
