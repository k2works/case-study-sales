import Config from "../config";
import Utils from "../utils";
import {
    ShippingCriteriaType,
    ShippingPageInfoType,
    ShippingType,
    mapToShippingCriteriaResource,
    mapToShippingResource
} from "../../models/sales/shipping";

// 出荷ルール確認結果の型定義
export interface ShippingRuleCheckResponse {
    message: string;
    details: string[];
}

export interface ShippingServiceType {
    select: (page?: number, pageSize?: number) => Promise<ShippingPageInfoType>;
    find: (orderNumber: string) => Promise<ShippingType>;
    save: (shipping: ShippingType) => Promise<void>;
    search: (criteria: ShippingCriteriaType, page?: number, pageSize?: number) => Promise<ShippingPageInfoType>;
    orderShipping: (criteria: ShippingCriteriaType) => Promise<void>;
    checkRule: () => Promise<ShippingRuleCheckResponse>;
}

export const ShippingService = () => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/shipping`;

    const select = async (page?: number, pageSize?: number): Promise<ShippingPageInfoType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet<ShippingPageInfoType>(url);
    };

    const find = async (orderNumber: string): Promise<ShippingType> => {
        const url = `${endPoint}/${orderNumber}`;
        return await apiUtils.fetchGet<ShippingType>(url);
    };

    const save = async (shipping: ShippingType): Promise<void> => {
        await apiUtils.fetchPost<void>(endPoint, mapToShippingResource(shipping));
    };

    const search = async (criteria: ShippingCriteriaType, page?: number, pageSize?: number): Promise<ShippingPageInfoType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<ShippingPageInfoType>(url, mapToShippingCriteriaResource(criteria));
    };

    const orderShipping = async (criteria: ShippingCriteriaType): Promise<void> => {
        const url = `${endPoint}/order-shipping`;
        await apiUtils.fetchPost<void>(url, mapToShippingCriteriaResource(criteria));
    };

    const checkRule = async (): Promise<ShippingRuleCheckResponse> => {
        const url = `${endPoint}/check`;
        return await apiUtils.fetchPost<ShippingRuleCheckResponse>(url, {});
    };

    return {
        select,
        find,
        save,
        search,
        orderShipping,
        checkRule
    };
}
