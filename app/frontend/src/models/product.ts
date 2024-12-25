import {PageNationType} from "../views/application/PageNation.tsx";

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

type PriceType = {
    amount: number;
    currency: string;
};

type QuantityType = {
    amount: number;
    unit: string;
}

type SupplierCodeType = {
    value: string;
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
    supplierCode: SupplierCodeType;
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
    supplierCode: string;
    supplierBranchNumber?: number;
    substituteProduct?: SubstituteProductType[];
    boms?: BomType[];
    customerSpecificSellingPrices?: CustomerSpecificSellingPriceType[];
    addFlag: boolean;
    deleteFlag: boolean;
}

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

export type ProductCategoryCriteriaType = {
    productCategoryCode?: string;
    productCategoryName?: string;
    productCategoryPath?: string;
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
        supplierCode: product.supplierCode.value,
        supplierBranchNumber: product.supplierCode.branchNumber,
        substituteProduct: product.substituteProduct,
        boms: product.boms,
        customerSpecificSellingPrices: product.customerSpecificSellingPrices,
        addFlag: product.addFlag,
        deleteFlag: product.deleteFlag
    };
}

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

export const mapToProductCategoryCriteriaResource = (criteria: ProductCategoryCriteriaType): ProductCategoryCriteriaType => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.productCategoryCode) ? {} : {productCategoryCode: criteria.productCategoryCode}),
        ...(isEmpty(criteria.productCategoryName) ? {} : {productCategoryName: criteria.productCategoryName}),
        ...(isEmpty(criteria.productCategoryPath) ? {} : {productCategoryPath: criteria.productCategoryPath})
    };
}
