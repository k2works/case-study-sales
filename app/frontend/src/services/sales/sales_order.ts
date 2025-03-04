import Config from "../config.ts";
import Utils from "../utils.ts";
import {
    SalesOrderCriteriaType,
    SalesOrderFetchType,
    SalesOrderType,
    mapToSalesOrderCriteriaResource,
    mapToSalesOrderResource
} from "../../models/sales/sales_order";

export interface SalesOrderServiceType {
    select: (page?: number, pageSize?: number) => Promise<SalesOrderFetchType>;
    find: (orderNumber: string) => Promise<SalesOrderType>;
    create: (salesOrder: SalesOrderType) => Promise<void>;
    update: (salesOrder: SalesOrderType) => Promise<void>;
    destroy: (orderNumber: string) => Promise<void>;
    search: (criteria: SalesOrderCriteriaType, page?: number, pageSize?: number) => Promise<SalesOrderFetchType>;
}

export const SalesOrderService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/sales-orders`;

    const select = async (page?: number, pageSize?: number): Promise<SalesOrderFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet(url);
    };

    const find = async (orderNumber: string): Promise<SalesOrderType> => {
        const url = `${endPoint}/${orderNumber}`;
        return await apiUtils.fetchGet(url);
    };

    const create = async (salesOrder: SalesOrderType) => {
        return await apiUtils.fetchPost(endPoint, mapToSalesOrderResource(salesOrder));
    };

    const update = async (salesOrder: SalesOrderType) => {
        const url = `${endPoint}/${salesOrder.orderNumber}`;
        return await apiUtils.fetchPut(url, mapToSalesOrderResource(salesOrder));
    };

    const search = async (criteria: SalesOrderCriteriaType, page?: number, pageSize?: number): Promise<SalesOrderFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost(url, mapToSalesOrderCriteriaResource(criteria));
    };

    const destroy = async (orderNumber: string): Promise<void> => {
        const url = `${endPoint}/${orderNumber}`;
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
