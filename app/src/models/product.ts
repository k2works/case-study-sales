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

export type ProductCategoryResourceType = {
    productCategoryCode: string;
    productCategoryName?: string;
    productCategoryHierarchy: number;
    productCategoryPath?: string;
    lowestLevelDivision?: number;
    products?: ProductResourceType[];
};

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
