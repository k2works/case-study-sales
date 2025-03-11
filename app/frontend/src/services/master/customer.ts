import Config from "../config.ts";
import Utils from "../utils.ts";
import {
    CustomerType,
    CustomerFetchType,
    CustomerCriteriaType,
    CustomerCriteriaResourceType,
    mapToCustomerCriteriaType,
} from "../../models/master/partner";

export interface CustomerServiceType {
    select: (page?: number, pageSize?: number) => Promise<CustomerFetchType>;
    find: (customerCode: string, branchNumber: number) => Promise<CustomerType>;
    create: (customer: CustomerType) => Promise<void>;
    update: (customer: CustomerType) => Promise<void>;
    destroy: (customerCode: string, branchNumber: number) => Promise<void>;
    search: (criteria: CustomerCriteriaType, page?: number, pageSize?: number) => Promise<CustomerFetchType>;
}

export const CustomerService = (): CustomerServiceType => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/customers`;

    const select = async (page?: number, pageSize?: number): Promise<CustomerFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet(url);
    };

    const find = async (customerCode: string, branchNumber: number): Promise<CustomerType> => {
        const url = `${endPoint}/${customerCode}/${branchNumber}`;
        return await apiUtils.fetchGet(url);
    };

    const create = async (customer: CustomerType): Promise<void> => {
        await apiUtils.fetchPost(endPoint, customer);
    };

    const update = async (customer: CustomerType): Promise<void> => {
        const url = `${endPoint}/${customer.customerCode}`;
        await apiUtils.fetchPut(url, customer);
    };

    const destroy = async (customerCode: string, branchNumber: number): Promise<void> => {
        const url = `${endPoint}/${customerCode}/${branchNumber}`;
        await apiUtils.fetchDelete(url);
    };

    const search = async (
        criteria: CustomerCriteriaType,
        page?: number,
        pageSize?: number
    ): Promise<CustomerFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        const criteriaResource: CustomerCriteriaResourceType = mapToCustomerCriteriaType(criteria);
        return await apiUtils.fetchPost(url, criteriaResource);
    };

    return {
        select,
        find,
        create,
        update,
        destroy,
        search,
    };
};
