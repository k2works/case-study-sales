import Config from "../config.ts";
import Utils from "../utils.ts";
import {
    EmployeeCriteriaType,
    EmployeeFetchType,
    EmployeeType,
    mapToEmployeeCriteriaResource,
    mapToEmployeeResource
} from "../../models";

export interface EmployeeServiceType {
    select: (page?: number, pageSize?: number) => Promise<EmployeeFetchType>;
    find: (empCode: string) => Promise<EmployeeType>;
    create: (employee: EmployeeType) => Promise<void>;
    update: (employee: EmployeeType) => Promise<void>;
    destroy: (empCode: string) => Promise<void>;
    search: (criteria:EmployeeCriteriaType, page?: number, pageSize?: number) => Promise<EmployeeFetchType>;
}
export const EmployeeService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/employees`;


    const select = async (page?: number, pageSize?: number): Promise<EmployeeFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet<EmployeeFetchType>(url);
    };

    const find = async (empCode: string): Promise<EmployeeType> => {
        const url = `${endPoint}/${empCode}`;
        return await apiUtils.fetchGet<EmployeeType>(url);
    };

    const create = async (employee: EmployeeType): Promise<void> => {
        await apiUtils.fetchPost<void>(endPoint, mapToEmployeeResource(employee));
    };

    const update = async (employee: EmployeeType): Promise<void> => {
        const url = `${endPoint}/${employee.empCode}`;
        await apiUtils.fetchPut<void>(url, mapToEmployeeResource(employee));
    };

    const search = async (criteria: EmployeeCriteriaType, page?: number, pageSize?: number): Promise<EmployeeFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<EmployeeFetchType>(url, mapToEmployeeCriteriaResource(criteria));
    };

    const destroy = async (empCode: string): Promise<void> => {
        const url = `${endPoint}/${empCode}`;
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
};
