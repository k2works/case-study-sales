import Config from "../config.ts";
import Utils from "../utils.ts";
import {
    CustomerType,
    CustomerFetchType,
    CustomerCriteriaType,
    CustomerResourceType,
    CustomerCriteriaResourceType,
    mapToCustomerType,
    mapToCustomerCriteriaType, CustomerCodeType,
} from "../../models/master/partner";

export interface CustomerServiceType {
    select: (page?: number, pageSize?: number) => Promise<CustomerFetchType>;
    find: (customerCode: CustomerCodeType) => Promise<CustomerType>;
    create: (customer: CustomerType) => Promise<void>;
    update: (customer: CustomerType) => Promise<void>;
    destroy: (customerCode: CustomerCodeType) => Promise<void>;
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

    const find = async (customerCode: CustomerCodeType): Promise<CustomerType> => {
        const url = `${endPoint}/${customerCode.code.value}/${customerCode.branchNumber}`;
        return await apiUtils.fetchGet(url);
    };

    const create = async (customer: CustomerType): Promise<void> => {
        const resource: CustomerResourceType = mapToCustomerType(customer);
        await apiUtils.fetchPost(endPoint, resource);
    };

    const update = async (customer: CustomerType): Promise<void> => {
        const url = `${endPoint}/${customer.customerCode.code.value}`;
        const resource: CustomerResourceType = mapToCustomerType(customer);
        await apiUtils.fetchPut(url, resource);
    };

    const destroy = async (customerCode: CustomerCodeType): Promise<void> => {
        const url = `${endPoint}/${customerCode.code.value}/${customerCode.branchNumber}`;
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