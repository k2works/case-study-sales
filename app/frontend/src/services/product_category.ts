import Config from "./config";
import Utils from "./utils";
import {
    mapToProductCategoryCriteriaResource,
    mapToProductCategoryResource, ProductCategoryCriteriaType, ProductCategoryFetchType,
    ProductCategoryType
} from "../models";

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
        return await apiUtils.fetchGet(url);
    };

    const find = async (categoryCode: string): Promise<ProductCategoryType> => {
        const url = `${endPoint}/${categoryCode}`;
        return await apiUtils.fetchGet(url);
    };

    const create = async (category: ProductCategoryType) => {
        return await apiUtils.fetchPost(endPoint, mapToProductCategoryResource(category));
    };

    const update = async (category: ProductCategoryType) => {
        const url = `${endPoint}/${category.productCategoryCode.value}`;
        return await apiUtils.fetchPut(url, mapToProductCategoryResource(category));
    };

    const search = (criteria: ProductCategoryCriteriaType, page?: number, pageSize?: number): Promise<ProductCategoryFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return apiUtils.fetchPost(url, mapToProductCategoryCriteriaResource(criteria));
    };

    const destroy = async (categoryCode: string): Promise<void> => {
        const url = `${endPoint}/${categoryCode}`;
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
};
