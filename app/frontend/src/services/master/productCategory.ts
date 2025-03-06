import Config from "../config.ts";
import Utils from "../utils.ts";
import {
    mapToProductCategoryCriteriaResource,
    mapToProductCategoryResource,
    ProductCategoryCriteriaType,
    ProductCategoryFetchType,
    ProductCategoryType
} from "../../models/master/product";

export interface ProductCategoryServiceType {
    select: (page?: number, pageSize?: number) => Promise<ProductCategoryFetchType>;
    find: (categoryCode: string) => Promise<ProductCategoryType>;
    create: (category: ProductCategoryType) => Promise<void>;
    update: (category: ProductCategoryType) => Promise<void>;
    destroy: (categoryCode: string) => Promise<void>;
    search: (criteria:ProductCategoryCriteriaType, page?: number, pageSize?: number) => Promise<ProductCategoryFetchType>;
}

export const ProductCategoryService = (): ProductCategoryServiceType => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/product/categories`;

    const select = async (page?: number, pageSize?: number): Promise<ProductCategoryFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet<ProductCategoryFetchType>(url);
    };

    const find = async (categoryCode: string): Promise<ProductCategoryType> => {
        const url = `${endPoint}/${categoryCode}`;
        return await apiUtils.fetchGet<ProductCategoryType>(url);
    };

    const create = async (category: ProductCategoryType): Promise<void> => {
        await apiUtils.fetchPost<void>(endPoint, mapToProductCategoryResource(category));
    };

    const update = async (category: ProductCategoryType): Promise<void> => {
        const url = `${endPoint}/${category.productCategoryCode.value}`;
        await apiUtils.fetchPut<void>(url, mapToProductCategoryResource(category));
    };

    const search = async (criteria: ProductCategoryCriteriaType, page?: number, pageSize?: number): Promise<ProductCategoryFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<ProductCategoryFetchType>(url, mapToProductCategoryCriteriaResource(criteria));
    };

    const destroy = async (categoryCode: string): Promise<void> => {
        const url = `${endPoint}/${categoryCode}`;
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
