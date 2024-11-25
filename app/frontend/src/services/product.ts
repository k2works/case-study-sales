import Config from "./config";
import Utils from "./utils";
import {mapToProductResource, ProductType} from "../models";

export interface ProductServiceType {
    select: (page?: number, pageSize?: number) => Promise<any>;
    selectBoms: (page?: number, pageSize?: number) => Promise<any>;
    find: (productCode: string) => Promise<ProductType>;
    create: (product: ProductType) => Promise<void>;
    update: (product: ProductType) => Promise<void>;
    destroy: (productCode: string) => Promise<void>;
    search: (pageSize: number, productName: string, page: number) => Promise<ProductType[]>;
}

export const ProductService = (): ProductServiceType => {
    const config = Config();
    const apiUtils = Utils.apiUtils;
    const endPoint = `${config.apiUrl}/products`;

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

    const selectBoms = async (page?: number, pageSize?: number): Promise<any> => {
        let url = endPoint + "/boms";
        if (pageSize && page) {
            url = url + "?pageSize=" + pageSize + "&page=" + page;
        } else if (pageSize) {
            url = url + "?pageSize=" + pageSize;
        } else if (page) {
            url = url + "?page=" + page;
        }
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

    const destroy = async (productCode: string): Promise<void> => {
        const url = `${endPoint}/${productCode}`;
        await apiUtils.fetchDelete(url);
    };

    const search = async (pageSize = 10, productName: string, page = 1): Promise<ProductType[]> => {
        const url = `${endPoint}/search?pageSize=${pageSize}&productName=${encodeURIComponent(productName)}&page=${page}`;
        return await apiUtils.fetchGet(url);
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
