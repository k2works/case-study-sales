import Config from "../config.ts";
import Utils from "../utils.ts";
import {
    mapToProductCriteriaResource,
    mapToProductResource,
    ProductCriteriaType,
    ProductFetchType,
    ProductType
} from "../../models/master/product";

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
        return await apiUtils.fetchGet<ProductFetchType>(url);
    };

    const selectBoms = async (page?: number, pageSize?: number): Promise<ProductFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/boms`, page, pageSize);
        return await apiUtils.fetchGet<ProductFetchType>(url);
    };

    const find = async (productCode: string): Promise<ProductType> => {
        const url = `${endPoint}/${productCode}`;
        return await apiUtils.fetchGet<ProductType>(url);
    };

    const create = async (product: ProductType): Promise<void> => {
        await apiUtils.fetchPost<void>(endPoint, mapToProductResource(product));
    };

    const update = async (product: ProductType): Promise<void> => {
        const url = `${endPoint}/${product.productCode.value}`;
        await apiUtils.fetchPut<void>(url, mapToProductResource(product));
    };

    const search = async (criteria: ProductCriteriaType, page?: number, pageSize?: number): Promise<ProductFetchType> => {
        const url = Utils.buildUrlWithPaging(`${endPoint}/search`, page, pageSize);
        return await apiUtils.fetchPost<ProductFetchType>(url, mapToProductCriteriaResource(criteria));
    };

    const destroy = async (productCode: string): Promise<void> => {
        const url = `${endPoint}/${productCode}`;
        await apiUtils.fetchDelete<void>(url);
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
