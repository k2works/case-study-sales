import {PageNationType} from "../../views/application/PageNation.tsx";

export type WarehouseType = {
    warehouseCode: string;
    warehouseName: string;
    warehouseCategory?: string;
    postalCode?: string;
    prefecture?: string;
    address1?: string;
    address2?: string;
    checked?: boolean;
}

export type WarehouseFetchType = {
    list: WarehouseType[];
} & PageNationType;

export type WarehouseCriteriaType = {
    warehouseCode?: string;
    warehouseName?: string;
}

export type WarehouseSearchCriteriaType = {
    warehouseCode?: string;
    warehouseName?: string;
}

// 表示用の倉庫区分をAPI用のコードに変換
const convertWarehouseCategoryToCode = (displayValue?: string): string | undefined => {
    if (!displayValue) return undefined;

    const categoryMap: { [key: string]: string } = {
        "通常倉庫": "N",
        "得意先": "C",
        "仕入先": "S",
        "部門倉庫": "D",
        "製品倉庫": "P",
        "原材料倉庫": "M"
    };

    return categoryMap[displayValue];
};

// API用のコードを表示用の倉庫区分に変換
const convertCodeToWarehouseCategory = (code?: string): string | undefined => {
    if (!code) return undefined;

    const codeMap: { [key: string]: string } = {
        "N": "通常倉庫",
        "C": "得意先",
        "S": "仕入先",
        "D": "部門倉庫",
        "P": "製品倉庫",
        "M": "原材料倉庫"
    };

    return codeMap[code];
};

export const mapToWarehouseResource = (warehouse: WarehouseType): WarehouseType => {
    return {
        ...warehouse,
        warehouseCategory: convertWarehouseCategoryToCode(warehouse.warehouseCategory)
    };
};

export const mapFromWarehouseResource = (warehouse: WarehouseType): WarehouseType => {
    return {
        ...warehouse,
        warehouseCategory: convertCodeToWarehouseCategory(warehouse.warehouseCategory)
    };
};

export const mapToWarehouseCriteriaResource = (criteria: WarehouseCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    type Resource = {
        warehouseCode?: string;
        warehouseName?: string;
    };
    const resource: Resource = {
        ...(isEmpty(criteria.warehouseCode) ? {} : { warehouseCode: criteria.warehouseCode }),
        ...(isEmpty(criteria.warehouseName) ? {} : { warehouseName: criteria.warehouseName }),
    };

    return resource;
};

export const mapToWarehouseSearchResource = (searchCriteria: WarehouseSearchCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    type Resource = {
        warehouseCode?: string;
        warehouseName?: string;
    };
    const resource: Resource = {
        ...(isEmpty(searchCriteria.warehouseCode) ? {} : { warehouseCode: searchCriteria.warehouseCode }),
        ...(isEmpty(searchCriteria.warehouseName) ? {} : { warehouseName: searchCriteria.warehouseName }),
    };

    return resource;
};