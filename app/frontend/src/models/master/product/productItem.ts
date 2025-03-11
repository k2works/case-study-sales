import {PageNationType} from "../../../views/application/PageNation.tsx";
import {PriceType, QuantityType} from "../shared.ts";



export type SubstituteProductType = {
    productCode: string;
    substituteProductCode: string;
    priority: number;
};

export type BomType = {
    productCode: string;
    componentCode: string;
    componentQuantity: QuantityType;
}

export type BomFetchType = {
    list: BomType[];
} & PageNationType;

export type CustomerSpecificSellingPriceType = {
    productCode: string;
    customerCode: string;
    sellingPrice: PriceType;
};

export type ProductType = {
    productCode: string;
    productFormalName: string;
    productAbbreviation: string;
    productNameKana: string;
    productType: string;
    sellingPrice: number;
    purchasePrice?: number;
    costOfSales: number;
    taxType: string;
    productClassificationCode?: string;
    miscellaneousType?: string;
    stockManagementTargetType?: string;
    stockAllocationType?: string;
    vendorCode: string;
    vendorBranchNumber?: number;
    substituteProduct: SubstituteProductType[];
    boms: BomType[];
    customerSpecificSellingPrices: CustomerSpecificSellingPriceType[];
    checked: boolean;
    addFlag: boolean;
    deleteFlag: boolean;
};

export enum ProductEnumType {
    商品 = "商品", 製品 = "製品", 部品 = "部品", 包材 = "包材", その他 = "その他"
}

export enum TaxEnumType {
    外税 = "外税", 内税 = "内税", 非課税 = "非課税", その他 = "その他"
}

export enum MiscellaneousEnumType {
    適用外 = "適用外", 適用 = "適用"
}

export enum StockManagementTargetEnumType {
    対象外 = "対象外", 対象 = "対象"
}

export enum StockAllocationEnumType {
    未引当 = "未引当", 引当済 = "引当済"
}

export type ProductFetchType = {
    list: ProductType[];
} & PageNationType;

export type ProductResourceType = ProductType;

export type ProductCriteriaType = {
    productCode?: string;
    productFormalName?: string;
    productAbbreviation?: string;
    productNameKana?: string;
    productCategoryCode?: string;
    vendorCode?: string;
    productType?: string;
    taxType?: string;
    miscellaneousType?: string;
    stockManagementTargetType?: string;
    stockAllocationType?: string;
}

export const mapToProductResource = (product: ProductType): ProductResourceType => {
    return {
        ...product
    };
}

export const mapToProductCriteriaResource = (criteria: ProductCriteriaType): ProductCriteriaType => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.productCode) ? {} : {productCode: criteria.productCode}),
        ...(isEmpty(criteria.productFormalName) ? {} : {productFormalName: criteria.productFormalName}),
        ...(isEmpty(criteria.productAbbreviation) ? {} : {productAbbreviation: criteria.productAbbreviation}),
        ...(isEmpty(criteria.productNameKana) ? {} : {productNameKana: criteria.productNameKana}),
        ...(isEmpty(criteria.productCategoryCode) ? {} : {productCategoryCode: criteria.productCategoryCode}),
        ...(isEmpty(criteria.vendorCode) ? {} : {supplierCode: criteria.vendorCode}),
        ...(isEmpty(criteria.productType) ? {} : {productType: criteria.productType}),
        ...(isEmpty(criteria.taxType) ? {} : {taxType: criteria.taxType}),
        ...(isEmpty(criteria.miscellaneousType) ? {} : {miscellaneousType: criteria.miscellaneousType}),
        ...(isEmpty(criteria.stockManagementTargetType) ? {} : {stockManagementTargetType: criteria.stockManagementTargetType}),
        ...(isEmpty(criteria.stockAllocationType) ? {} : {stockAllocationType: criteria.stockAllocationType})
    };
}
