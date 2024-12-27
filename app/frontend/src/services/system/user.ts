import Config from "../config.ts";
import Utils from "../utils.ts";
import {mapToUserAccountResource, UserAccountType, UserFetchType} from "../../models";

export interface UserServiceType {
    select: (page?: number, pageSize?: number) => Promise<UserFetchType>;
    find: (userId: string) => Promise<UserAccountType>;
    create: (user: UserAccountType) => Promise<string>;
    update: (user: UserAccountType) => Promise<string>;
    destroy: (userId: string) => Promise<string>;
    search: (code: string, page: number, pageSize: number) => Promise<UserFetchType>;
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

    const find = async (userId: string) => {
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

    const search = async (code: string, page = 1, pageSize = 10): Promise<UserFetchType> => {
        const url = `${endPoint}/search?pageSize=${pageSize}&code=${code}&page=${page}`;

        return await apiUtils.fetchGet(url);
    };

    const destroy = async (userId: string) => {
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