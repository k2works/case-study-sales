import {PageNationType} from "../../../views/application/PageNation.tsx";
import {mapToProductResource, ProductResourceType, ProductType} from "./productItem.ts";

type ProductCategoryCode = {
    value: string;
};

export type ProductCategoryType = {
    productCategoryCode: ProductCategoryCode;
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

export type ProductCategoryResourceType = {
    productCategoryCode: string;
    productCategoryName?: string;
    productCategoryHierarchy: number;
    productCategoryPath?: string;
    lowestLevelDivision?: number;
    products?: ProductResourceType[];
};

export const mapToProductCategoryResource = (productCategory: ProductCategoryType): ProductCategoryResourceType => {
    return {
        productCategoryCode: productCategory.productCategoryCode.value,
        productCategoryName: productCategory.productCategoryName,
        productCategoryHierarchy: productCategory.productCategoryHierarchy,
        productCategoryPath: productCategory.productCategoryPath,
        lowestLevelDivision: productCategory.lowestLevelDivision,
        products: productCategory.products.map(product => mapToProductResource(product))
    };
}
