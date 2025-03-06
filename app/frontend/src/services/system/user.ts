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

    const select = async (page?: number, pageSize?: number): Promise<UserFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet<UserFetchType>(url);
    };

    const find = async (userId: string): Promise<UserAccountType> => {
        const url = `${endPoint}/${userId}`;
        return await apiUtils.fetchGet<UserAccountType>(url);
    };

    const create = async (user: UserAccountType): Promise<string> => {
        return await apiUtils.fetchPost<string>(endPoint, mapToUserAccountResource(user));
    };

    const update = async (user: UserAccountType): Promise<string> => {
        const url = `${endPoint}/${user.userId.value}`;
        return await apiUtils.fetchPut<string>(url, mapToUserAccountResource(user));
    };

    const search = async (code: string, page = 1, pageSize = 10): Promise<UserFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search?code=${code}`, page, pageSize);
        return await apiUtils.fetchGet<UserFetchType>(url);
    };

    const destroy = async (userId: string): Promise<string> => {
        const url = `${endPoint}/${userId}`;
        return await apiUtils.fetchDelete<string>(url);
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
