import Config from "../config.ts";
import Utils from "../utils.ts";
import {
    WarehouseCriteriaType,
    WarehouseFetchType,
    WarehouseType,
    mapToWarehouseCriteriaResource,
    mapToWarehouseResource
} from "../../models/master/warehouse.ts";

export interface WarehouseServiceType {
    select: (page?: number, pageSize?: number) => Promise<WarehouseFetchType>;
    find: (warehouseCode: string) => Promise<WarehouseType>;
    create: (warehouse: WarehouseType) => Promise<void>;
    update: (warehouse: WarehouseType) => Promise<void>;
    destroy: (warehouseCode: string) => Promise<void>;
    search: (criteria: WarehouseCriteriaType, page?: number, pageSize?: number) => Promise<WarehouseFetchType>;
}

export const WarehouseService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/warehouses`;

    const select = async (page?: number, pageSize?: number): Promise<WarehouseFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet<WarehouseFetchType>(url);
    };

    const find = async (warehouseCode: string): Promise<WarehouseType> => {
        const url = `${endPoint}/${warehouseCode}`;
        return await apiUtils.fetchGet<WarehouseType>(url);
    };

    const create = async (warehouse: WarehouseType): Promise<void> => {
        await apiUtils.fetchPost<void>(endPoint, mapToWarehouseResource(warehouse));
    };

    const update = async (warehouse: WarehouseType): Promise<void> => {
        const url = `${endPoint}/${warehouse.warehouseCode}`;
        await apiUtils.fetchPut<void>(url, mapToWarehouseResource(warehouse));
    };

    const search = async (criteria: WarehouseCriteriaType, page?: number, pageSize?: number): Promise<WarehouseFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<WarehouseFetchType>(url, mapToWarehouseCriteriaResource(criteria));
    };

    const destroy = async (warehouseCode: string): Promise<void> => {
        const url = `${endPoint}/${warehouseCode}`;
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