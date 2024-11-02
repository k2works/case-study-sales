import Config from "./config";
import Utils from "./utils";
import {EmployeeType, mapToEmployeeResource} from "../models";

export const EmployeeService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/employees`;


    const select = async (page?: number, pageSize?: number): Promise<any> => {
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

    const find = async (empCode: string): Promise<EmployeeType> => {
        const url = `${endPoint}/${empCode}`;
        return await apiUtils.fetchGet(url);
    };

    const create = async (employee: EmployeeType) => {
        return await apiUtils.fetchPost(endPoint, mapToEmployeeResource(employee));
    };

    const update = async (employee: EmployeeType) => {
        const url = `${endPoint}/${employee.empCode.value}`;
        return await apiUtils.fetchPut(url, mapToEmployeeResource(employee));
    };

    const search = async (pageSize = 10, empName: string, page = 1): Promise<EmployeeType[]> => {
        const url = `${endPoint}/search?pageSize=${pageSize}&empName=${encodeURIComponent(empName)}&page=${page}`;
        return await apiUtils.fetchGet(url);
    };

    const destroy = async (empCode: string): Promise<void> => {
        const url = `${endPoint}/${empCode}`;
        await apiUtils.fetchDelete(url);
    };

    return {
        select,
        find,
        create,
        update,
        destroy,
        search
    };
};
