import Config from "../config";
import Utils from "../utils";
import {
    SalesCriteriaType,
    SalesPageInfoType,
    SalesType,
    mapToSalesCriteriaResource,
    mapToSalesResource
} from "../../models/sales/sales";

export interface SalesServiceType {
    select: (page?: number, pageSize?: number) => Promise<SalesPageInfoType>;
    find: (salesNumber: string) => Promise<SalesType>;
    create: (sales: SalesType) => Promise<void>;
    update: (sales: SalesType) => Promise<void>;
    destroy: (salesNumber: string) => Promise<void>;
    search: (criteria: SalesCriteriaType, page?: number, pageSize?: number) => Promise<SalesPageInfoType>;
}

export const SalesService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/sales`;

    const select = async (page?: number, pageSize?: number): Promise<SalesPageInfoType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet<SalesPageInfoType>(url);
    };

    const find = async (salesNumber: string): Promise<SalesType> => {
        const url = `${endPoint}/${salesNumber}`;
        return await apiUtils.fetchGet<SalesType>(url);
    };

    const create = async (sales: SalesType): Promise<void> => {
        await apiUtils.fetchPost<void>(endPoint, mapToSalesResource(sales));
    };

    const update = async (sales: SalesType): Promise<void> => {
        const url = `${endPoint}/${sales.salesNumber}`;
        await apiUtils.fetchPut<void>(url, mapToSalesResource(sales));
    };

    const search = async (criteria: SalesCriteriaType, page?: number, pageSize?: number): Promise<SalesPageInfoType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<SalesPageInfoType>(url, mapToSalesCriteriaResource(criteria));
    };

    const destroy = async (salesNumber: string): Promise<void> => {
        const url = `${endPoint}/${salesNumber}`;
        await apiUtils.fetchDelete<void>(url);
    };

    const aggregate = async (): Promise<void> => {
        const url = `${endPoint}/aggregate`;
        await apiUtils.fetchPost<void>(url, null);
    };

    return {
        select,
        find,
        create,
        update,
        destroy,
        search,
        aggregate
    };
}