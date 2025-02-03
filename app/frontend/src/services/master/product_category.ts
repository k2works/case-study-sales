import Config from "../config.ts";
import Utils from "../utils.ts";
import {mapToProductCategoryResource, ProductCategoryType} from "../../models/master/product";

export interface ProductCategoryServiceType {
    select: (page?: number, pageSize?: number) => Promise<any>;
    find: (categoryCode: string) => Promise<ProductCategoryType>;
    create: (category: ProductCategoryType) => Promise<void>;
    update: (category: ProductCategoryType) => Promise<void>;
    destroy: (categoryCode: string) => Promise<void>;
    search: (categoryName: string, page: number, pageSize: number) => Promise<ProductCategoryType[]>;
}

export const ProductCategoryService = (): ProductCategoryServiceType => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/product/categories`;

    const select = async (page?: number, pageSize?: number): Promise<any> => {
        let url = endPoint;
        if (pageSize && page) {
            url = url + "?pageSize=" + pageSize + "&page=" + page;
        } else if (pageSize) {
            url = url + "?pageSize=" + pageSize;
        } else if (page) {
            url = url + "?page=" + page;
        }
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

    const destroy = async (categoryCode: string): Promise<void> => {
        const url = `${endPoint}/${categoryCode}`;
        await apiUtils.fetchDelete(url);
    };

    const search = async (categoryName: string, page = 1, pageSize = 10): Promise<ProductCategoryType[]> => {
        const url = `${endPoint}/search?pageSize=${pageSize}&categoryName=${encodeURIComponent(categoryName)}&page=${page}`;
        return await apiUtils.fetchGet(url);
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
