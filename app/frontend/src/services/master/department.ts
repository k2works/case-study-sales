import Config from "../config.ts";
import Utils from "../utils.ts";
import {
    DepartmentCriteriaType,
    DepartmentFetchType,
    DepartmentType,
    mapToDepartmentCriteriaResource,
    mapToDepartmentResource
} from "../../models";
import {toISOStringWithTimezone} from "../../components/application/utils.ts";

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
        return await apiUtils.fetchGet<DepartmentFetchType>(url);
    };

    const find = async (deptCode: string, departmentStartDate: string): Promise<DepartmentType[]> => {
        const startDate = toISOStringWithTimezone(new Date(departmentStartDate));
        const url = `${endPoint}/${deptCode}/${startDate}`;
        return await apiUtils.fetchGet<DepartmentType[]>(url);
    };

    const create = async (department: DepartmentType): Promise<void> => {
        await apiUtils.fetchPost<void>(endPoint, mapToDepartmentResource(department));
    };

    const update = async (department: DepartmentType): Promise<void> => {
        const startDate = toISOStringWithTimezone(new Date(department.startDate));
        const url = `${endPoint}/${department.departmentCode}/${startDate}`;
        await apiUtils.fetchPut<void>(url, mapToDepartmentResource(department));
    };

    const search = async (criteria: DepartmentCriteriaType, page?: number, pageSize?: number): Promise<DepartmentFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<DepartmentFetchType>(url, mapToDepartmentCriteriaResource(criteria));
    };

    const destroy = async (deptCode: string, departmentStartDate: string): Promise<void> => {
        const startDate = toISOStringWithTimezone(new Date(departmentStartDate));
        const url = `${endPoint}/${deptCode}/${startDate}`;
        await apiUtils.fetchDelete<void>(url);
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
