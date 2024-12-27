import Config from "../config.ts";
import Utils from "../utils.ts";
import {
    mapToProductCriteriaResource,
    mapToProductResource,
    ProductCriteriaType,
    ProductFetchType,
    ProductType
} from "../models";
import {BomFetchType, mapToProductResource, ProductFetchType, ProductType} from "../../models";

export interface ProductServiceType {
    select: (page?: number, pageSize?: number) => Promise<ProductFetchType>;
    selectBoms: (page?: number, pageSize?: number) => Promise<ProductFetchType>;
    find: (productCode: string) => Promise<ProductType>;
    create: (product: ProductType) => Promise<void>;
    update: (product: ProductType) => Promise<void>;
    destroy: (productCode: string) => Promise<void>;
    search: (criteria:ProductCriteriaType, page?: number, pageSize?: number) => Promise<ProductFetchType>;
}

export const ProductService = (): ProductServiceType => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/products`;

    const select = async (page?: number, pageSize?: number): Promise<ProductFetchType> => {
        const url = Utils.buildUrlWithPaging(endPoint, page, pageSize);
        return await apiUtils.fetchGet(url);
    };

    const selectBoms = async (page?: number, pageSize?: number): Promise<ProductFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/boms`, page, pageSize);
        return await apiUtils.fetchGet(url);
    };

    const find = async (productCode: string): Promise<ProductType> => {
        const url = `${endPoint}/${productCode}`;
        return await apiUtils.fetchGet(url);
    };

    const create = async (product: ProductType) => {
        return await apiUtils.fetchPost(endPoint, mapToProductResource(product));
    };

    const update = async (product: ProductType) => {
        const url = `${endPoint}/${product.productCode.value}`;
        return await apiUtils.fetchPut(url, mapToProductResource(product));
    };

    const search = (criteria: ProductCriteriaType, page?: number, pageSize?: number): Promise<ProductFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return apiUtils.fetchPost(url, mapToProductCriteriaResource(criteria));
    };

    const destroy = async (productCode: string): Promise<void> => {
        const url = `${endPoint}/${productCode}`;
        await apiUtils.fetchDelete(url);
    };

    return {
        select,
        selectBoms,
        find,
        create,
        update,
        destroy,
        search
    };
};
