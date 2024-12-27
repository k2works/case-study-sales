import Config from "../config.ts";
import Utils from "../utils.ts";
import {DepartmentFetchType, DepartmentType, mapToDepartmentResource} from "../../models";
import {toISOStringWithTimezone} from "../../components/application/utils.ts";

export interface DepartmentServiceType {
    select: (page?: number, pageSize?: number) => Promise<DepartmentFetchType>;
    find: (deptCode: string, departmentStartDate: string) => Promise<DepartmentType[]>;
    create: (department: DepartmentType) => Promise<void>;
    update: (department: DepartmentType) => Promise<void>;
    destroy: (deptCode: string, departmentStartDate: string) => Promise<void>;
    search: (code: string, page: number, pageSize: number) => Promise<DepartmentType[]>;
}
export const DepartmentService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/departments`;

    const select = async (page?: number, pageSize?: number): Promise<DepartmentFetchType> => {
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

    const find = async (deptCode: string, departmentStartDate: string): Promise<DepartmentType[]> => {
        const startDate = toISOStringWithTimezone(new Date(departmentStartDate));
        const url = `${endPoint}/${deptCode}/${startDate}`;
        return await apiUtils.fetchGet(url);
    };

    const create = async (department: DepartmentType) => {
        return await apiUtils.fetchPost(endPoint, mapToDepartmentResource(department));
    };

    const update = async (department: DepartmentType) => {
        const startDate = toISOStringWithTimezone(new Date(department.departmentId.departmentStartDate.value));
        const url = `${endPoint}/${department.departmentId.deptCode.value}/${startDate}`;
        return await apiUtils.fetchPut(url, mapToDepartmentResource(department));
    };

    const search = async (code: string, page = 1, pageSize = 10): Promise<DepartmentType[]> => {
        const url = `${endPoint}/search?pageSize=${pageSize}&code=${code}&page=${page}`;
        return await apiUtils.fetchGet(url);
    };

    const destroy = async (deptCode: string, departmentStartDate: string): Promise<void> => {
        const startDate = toISOStringWithTimezone(new Date(departmentStartDate));
        const url = `${endPoint}/${deptCode}/${startDate}`;
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
}
