import Config from "./config";
import Utils from "./utils";
import {UserAccountType} from "../types";

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
        return await apiUtils.fetchPost(endPoint, {
            userId: user.userId.value,
            password: user.password?.value,
            firstName: user.name.firstName,
            lastName: user.name.lastName,
            roleName: user.roleName
        });
    };

    const update = async (user: UserAccountType) => {
        const url = `${endPoint}/${user.userId.value}`;
        return await apiUtils.fetchPut(url, {
            userId: user.userId.value,
            password: user.password?.value,
            firstName: user.name.firstName,
            lastName: user.name.lastName,
            roleName: user.roleName
        });
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