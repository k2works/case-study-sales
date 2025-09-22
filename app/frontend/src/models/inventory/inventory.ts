import { PageNationType } from "../../views/application/PageNation.tsx";
import { toISOStringLocal } from "../../components/application/utils.ts";

export enum StockCategoryEnumType {
    通常在庫 = "1",
    安全在庫 = "2",
    廃棄予定 = "3"
}

export enum QualityCategoryEnumType {
    良品 = "G",
    不良品 = "B",
    返品 = "R"
}

export type InventoryType = {
    warehouseCode: string;
    productCode: string;
    lotNumber: string;
    stockCategory: string;
    qualityCategory: string;
    actualStockQuantity: number;
    availableStockQuantity: number;
    lastShipmentDate?: string;
    productName?: string;
    warehouseName?: string;
    checked?: boolean;
};

export type InventoryCriteriaType = {
    warehouseCode?: string;
    productCode?: string;
    lotNumber?: string;
    stockCategory?: string;
    qualityCategory?: string;
    productName?: string;
    warehouseName?: string;
    hasStock?: boolean;
    isAvailable?: boolean;
};

export type InventorySearchCriteriaType = {
    warehouseCode?: string;
    productCode?: string;
    lotNumber?: string;
    stockCategory?: string;
    qualityCategory?: string;
    productName?: string;
    warehouseName?: string;
    hasStock?: string;
    isAvailable?: string;
};

export type InventoryFetchType = {
    list: InventoryType[];
} & PageNationType;

export type InventoryPageNationType = PageNationType<InventoryCriteriaType>;

export const mapToInventoryResource = (inventory: InventoryType): InventoryType => {
    return {
        warehouseCode: inventory.warehouseCode,
        productCode: inventory.productCode,
        lotNumber: inventory.lotNumber,
        stockCategory: inventory.stockCategory,
        qualityCategory: inventory.qualityCategory,
        actualStockQuantity: inventory.actualStockQuantity,
        availableStockQuantity: inventory.availableStockQuantity,
        lastShipmentDate: inventory.lastShipmentDate ? toISOStringLocal(new Date(inventory.lastShipmentDate)) : undefined,
        productName: inventory.productName,
        warehouseName: inventory.warehouseName
    };
};

export const mapToInventorySearchResource = (criteria: InventorySearchCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.warehouseCode) ? {} : { warehouseCode: criteria.warehouseCode }),
        ...(isEmpty(criteria.productCode) ? {} : { productCode: criteria.productCode }),
        ...(isEmpty(criteria.lotNumber) ? {} : { lotNumber: criteria.lotNumber }),
        ...(isEmpty(criteria.stockCategory) ? {} : { stockCategory: criteria.stockCategory }),
        ...(isEmpty(criteria.qualityCategory) ? {} : { qualityCategory: criteria.qualityCategory }),
        ...(isEmpty(criteria.productName) ? {} : { productName: criteria.productName }),
        ...(isEmpty(criteria.warehouseName) ? {} : { warehouseName: criteria.warehouseName }),
        ...(isEmpty(criteria.hasStock) ? {} : { hasStock: criteria.hasStock === "true" }),
        ...(isEmpty(criteria.isAvailable) ? {} : { isAvailable: criteria.isAvailable === "true" })
    };
};

export const mapToInventoryCriteriaResource = (criteria: InventoryCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.warehouseCode) ? {} : { warehouseCode: criteria.warehouseCode }),
        ...(isEmpty(criteria.productCode) ? {} : { productCode: criteria.productCode }),
        ...(isEmpty(criteria.lotNumber) ? {} : { lotNumber: criteria.lotNumber }),
        ...(isEmpty(criteria.stockCategory) ? {} : { stockCategory: criteria.stockCategory }),
        ...(isEmpty(criteria.qualityCategory) ? {} : { qualityCategory: criteria.qualityCategory }),
        ...(isEmpty(criteria.productName) ? {} : { productName: criteria.productName }),
        ...(isEmpty(criteria.warehouseName) ? {} : { warehouseName: criteria.warehouseName }),
        ...(isEmpty(criteria.hasStock) ? {} : { hasStock: criteria.hasStock }),
        ...(isEmpty(criteria.isAvailable) ? {} : { isAvailable: criteria.isAvailable })
    };
};