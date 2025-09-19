import {
    LocationNumberType,
    LocationNumberFetchType,
    LocationNumberCriteriaType,
    mapToLocationNumberResource,
    mapToLocationNumberCriteriaResource
} from "../../models/master/locationnumber.ts";
import Utils from "../utils.ts";
import Config from "../config.ts";

export interface LocationNumberServiceType {
    select: (page?: number, pageSize?: number) => Promise<LocationNumberFetchType>;
    find: (warehouseCode: string, locationNumberCode: string, productCode: string) => Promise<LocationNumberType>;
    save: (locationNumber: LocationNumberType) => Promise<void>;
    update: (locationNumber: LocationNumberType) => Promise<void>;
    remove: (warehouseCode: string, locationNumberCode: string, productCode: string) => Promise<void>;
    search: (criteria: LocationNumberCriteriaType, page?: number, pageSize?: number) => Promise<LocationNumberFetchType>;
    findByWarehouseCode: (warehouseCode: string) => Promise<LocationNumberType[]>;
    findByLocationNumberCode: (locationNumberCode: string) => Promise<LocationNumberType[]>;
}

export const LocationNumberService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/locationnumbers`;

    const select = async (page?: number, pageSize?: number): Promise<LocationNumberFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet<LocationNumberFetchType>(url);
    };

    const find = async (warehouseCode: string, locationNumberCode: string, productCode: string): Promise<LocationNumberType> => {
        const url = `${endPoint}/${warehouseCode}/${locationNumberCode}/${productCode}`;
        return await apiUtils.fetchGet<LocationNumberType>(url);
    };

    const save = async (locationNumber: LocationNumberType): Promise<void> => {
        const resource = mapToLocationNumberResource(locationNumber);
        await apiUtils.fetchPost(endPoint, resource);
    };

    const update = async (locationNumber: LocationNumberType): Promise<void> => {
        const resource = mapToLocationNumberResource(locationNumber);
        const url = `${endPoint}/${locationNumber.warehouseCode}/${locationNumber.locationNumberCode}/${locationNumber.productCode}`;
        await apiUtils.fetchPut(url, resource);
    };

    const remove = async (warehouseCode: string, locationNumberCode: string, productCode: string): Promise<void> => {
        const url = `${endPoint}/${warehouseCode}/${locationNumberCode}/${productCode}`;
        await apiUtils.fetchDelete(url);
    };

    const search = async (criteria: LocationNumberCriteriaType, page?: number, pageSize?: number): Promise<LocationNumberFetchType> => {
        const resource = mapToLocationNumberCriteriaResource(criteria);
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<LocationNumberFetchType>(url, resource);
    };

    const findByWarehouseCode = async (warehouseCode: string): Promise<LocationNumberType[]> => {
        const url = `${endPoint}/by-warehouse/${warehouseCode}`;
        return await apiUtils.fetchGet<LocationNumberType[]>(url);
    };

    const findByLocationNumberCode = async (locationNumberCode: string): Promise<LocationNumberType[]> => {
        const url = `${endPoint}/by-location/${locationNumberCode}`;
        return await apiUtils.fetchGet<LocationNumberType[]>(url);
    };

    return {
        select,
        find,
        save,
        update,
        remove,
        search,
        findByWarehouseCode,
        findByLocationNumberCode
    };
};