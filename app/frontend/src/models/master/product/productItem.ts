import {PageNationType} from "../../../views/application/PageNation.tsx";
import {PriceType, QuantityType} from "../shared.ts";
import {PartnerCodeType} from "../partner";

type ProductCodeType = {
    value: string;
    businessType: string;
    itemType: string;
    livestockType: string;
    serialNumber: number;
};

type ProductNameType = {
    productFormalName: string;
    productAbbreviation: string;
    productNameKana: string;
};

type VendorCodeType = {
    code: PartnerCodeType;
    branchNumber: number;
};

export type SubstituteProductType = {
    productCode: ProductCodeType;
    substituteProductCode: ProductCodeType;
    priority: number;
};

export type BomType = {
    productCode: ProductCodeType;
    componentCode: ProductCodeType;
    componentQuantity: QuantityType;
}

export type BomFetchType = {
    list: BomType[];
} & PageNationType;

export type CustomerSpecificSellingPriceType = {
    productCode: ProductCodeType;
    customerCode: string;
    sellingPrice: PriceType;
};

export type ProductType = {
    productCode: ProductCodeType;
    productName: ProductNameType;
    productType: string;
    sellingPrice: PriceType;
    purchasePrice: PriceType;
    costOfSales: PriceType;
    taxType: string;
    productCategoryCode: { value: string };
    miscellaneousType: string;
    stockManagementTargetType: string;
    stockAllocationType: string;
    vendorCode: VendorCodeType;
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

export type ProductResourceType = {
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
    substituteProduct?: SubstituteProductType[];
    boms?: BomType[];
    customerSpecificSellingPrices?: CustomerSpecificSellingPriceType[];
    addFlag: boolean;
    deleteFlag: boolean;
}

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
        productCode: product.productCode.value,
        productFormalName: product.productName.productFormalName,
        productAbbreviation: product.productName.productAbbreviation,
        productNameKana: product.productName.productNameKana,
        productType: product.productType,
        sellingPrice: product.sellingPrice.amount,
        purchasePrice: product.purchasePrice.amount,
        costOfSales: product.costOfSales.amount,
        taxType: product.taxType,
        productClassificationCode: product.productCategoryCode.value,
        miscellaneousType: product.miscellaneousType,
        stockManagementTargetType: product.stockManagementTargetType,
        stockAllocationType: product.stockAllocationType,
        vendorCode: product.vendorCode.code.value,
        vendorBranchNumber: product.vendorCode.branchNumber,
        substituteProduct: product.substituteProduct,
        boms: product.boms,
        customerSpecificSellingPrices: product.customerSpecificSellingPrices,
        addFlag: product.addFlag,
        deleteFlag: product.deleteFlag
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