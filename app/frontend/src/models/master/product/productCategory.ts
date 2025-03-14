import {PageNationType} from "../../../views/application/PageNation.tsx";
import {mapToProductResource, ProductType} from "./productItem.ts";

export type ProductCategoryType = {
    productCategoryCode: string;
    productCategoryName: string;
    productCategoryHierarchy: number;
    productCategoryPath: string;
    lowestLevelDivision: number;
    products: ProductType[];
    checked: boolean;
};

export type ProductCategoryFetchType = {
    list: ProductCategoryType[];
} & PageNationType;

export type ProductCategoryCriteriaType = {
    productCategoryCode?: string;
    productCategoryName?: string;
    productCategoryPath?: string;
}

export const mapToProductCategoryResource = (productCategory: ProductCategoryType): ProductCategoryType => {
    return {
        ...productCategory,
        products: productCategory.products.map(product => mapToProductResource(product)),
    };
}

export const mapToProductCategoryCriteriaResource = (criteria: ProductCategoryCriteriaType): ProductCategoryCriteriaType => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.productCategoryCode) ? {} : {productCategoryCode: criteria.productCategoryCode}),
        ...(isEmpty(criteria.productCategoryName) ? {} : {productCategoryName: criteria.productCategoryName}),
        ...(isEmpty(criteria.productCategoryPath) ? {} : {productCategoryPath: criteria.productCategoryPath})
    };
}
