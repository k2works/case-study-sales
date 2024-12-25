import Config from "./config";
import Utils from "./utils";
import {
    DepartmentCriteriaType,
    DepartmentFetchType,
    DepartmentType,
    mapToDepartmentCriteriaResource,
    mapToDepartmentResource
} from "../models";
import {toISOStringWithTimezone} from "../components/application/utils.ts";

export interface DepartmentServiceType {
    select: (page?: number, pageSize?: number) => Promise<DepartmentFetchType>;
    find: (deptCode: string, departmentStartDate: string) => Promise<DepartmentType[]>;
    create: (department: DepartmentType) => Promise<void>;
    update: (department: DepartmentType) => Promise<void>;
    destroy: (deptCode: string, departmentStartDate: string) => Promise<void>;
    search: (criteria:DepartmentCriteriaType, page?: number, pageSize?: number) => Promise<DepartmentFetchType>;
}
export const DepartmentService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/departments`;

    const select = async (page?: number, pageSize?: number): Promise<DepartmentFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
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

    const search = (criteria: DepartmentCriteriaType, page?: number, pageSize?: number): Promise<DepartmentFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return apiUtils.fetchPost(url, mapToDepartmentCriteriaResource(criteria));
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
